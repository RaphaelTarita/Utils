package com.tara.util.persistence.http.socket;

import com.tara.util.persistence.http.response.Result;
import com.tara.util.persistence.node.NodeAction;
import com.tara.util.persistence.node.config.HTTPConfig;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SocketIO implements Closeable {
    private final Socket socket;
    private final NodeAction action;
    private final BufferedReader in;
    private final PrintWriter out;
    private Thread sender;
    private final ExecutorService receiver;
    private final Consumer<NodeAction> afterSend;
    private final Consumer<NodeAction> afterReceive;
    private final ExceptionHandler<IOException> handler;
    private Result result;
    private boolean expectResponse;
    private boolean closed = false;

    private void sendInternal(String payload) {
        out.println(payload);
    }

    private String receiveInternal() throws IOException {
        StringBuilder res = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            res.append(line)
                .append('\n');
        }
        return res.toString();
    }

    public SocketIO(
        HTTPConfig config,
        NodeAction action,
        Consumer<NodeAction> afterSend,
        Consumer<NodeAction> afterReceive,
        ExceptionHandler<IOException> exceptionHandler
    ) {
        this.action = action;
        this.afterSend = afterSend;
        this.afterReceive = afterReceive;
        this.handler = exceptionHandler;
        Socket socketCandidate = null;
        BufferedReader inCandidate = null;
        PrintWriter outCandidate = null;
        try {
            socketCandidate = new Socket(config.ip(), config.port());
            inCandidate = new BufferedReader(new InputStreamReader(socketCandidate.getInputStream()));
            outCandidate = new PrintWriter(socketCandidate.getOutputStream(), true);
        } catch (IOException ex) {
            handler.handle(ex);
        }

        socket = socketCandidate;
        in = inCandidate;
        out = outCandidate;
        receiver = Executors.newSingleThreadExecutor();
        result = null;
        expectResponse = false;
    }

    public void send(String payload) {
        if (sender != null) {
            try {
                sender.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        expectResponse = true;
        sender = new Thread(
            () -> {
                sendInternal(payload);
                afterSend.accept(action);
            },
            "TCP Socket sender"
        );
        sender.start();
    }

    public Result receive() {
        if (expectResponse) {
            result = new Result(
                receiver.submit(() -> {
                    try {
                        String res = receiveInternal();
                        afterReceive.accept(action);
                        return res;
                    } catch (IOException ex) {
                        handler.handle(ex);
                        return null;
                    }
                })
            );
            expectResponse = false;
        }
        return result;
    }

    public void join() {
        try {
            if (sender != null) {
                sender.join();
            }
            receiver.shutdown();
            if (!receiver.awaitTermination(1L, TimeUnit.HOURS)) {
                throw new RuntimeException("Termination of TCP Socket receiver thread took too long");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() throws IOException {
        join();
        socket.close();
        in.close();
        out.close();
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}