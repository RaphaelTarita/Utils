package com.tara.util.persistence.node;

import com.tara.util.id.UID;
import com.tara.util.persistence.http.request.HTTPConvert;
import com.tara.util.persistence.http.response.Result;
import com.tara.util.persistence.http.socket.SocketIO;
import com.tara.util.persistence.json.FormatException;
import com.tara.util.persistence.json.JSONConvert;
import com.tara.util.persistence.node.config.HTTPConfig;
import com.tara.util.persistence.node.state.NodeStateEnum;

import java.io.Closeable;
import java.io.IOException;

public class HTTPNode<VO> extends AbstractNode<VO, HTTPConfig> implements Closeable {
    private SocketIO pushSocket;
    private SocketIO fetchSocket;

    public HTTPNode(UID id, Class<VO> target, HTTPConfig config) {
        super(config, id, target);
    }

    public HTTPNode(Class<VO> target, HTTPConfig config) {
        super(config, target);
    }

    public HTTPNode(UID id, Class<VO> target) {
        super(HTTPConfig.defaultConf(), id, target);
    }

    public HTTPNode(Class<VO> target) {
        super(HTTPConfig.defaultConf(), target);
    }

    private void handlePushResponse() {
        Result pushResponse = pushSocket.receive();
        if (pushResponse == null || pushResponse.wasUsed()) {
            return;
        }

        if (pushResponse.successful()) {
            state.update(NodeAction.PUSH, NodeStateEnum.SYNC, "push successful, server responded with " + pushResponse.getResponseCode());
        } else if (pushResponse.hasException()) {
            exc(pushResponse.getException(), NodeAction.PUSH);
        } else {
            state.update(NodeAction.PUSH, NodeStateEnum.ERROR, "server responded with " + pushResponse.getResponseCode() + " after push");
        }

        pushResponse.setUsed();
    }

    private void handleFetchResponse() {
        Result fetchResponse = fetchSocket.receive();
        if (fetchResponse == null || fetchResponse.wasUsed()) {
            return;
        }

        if (!fetchResponse.successful()) {
            if (fetchResponse.hasException()) {
                exc(fetchResponse.getException(), NodeAction.FETCH);
            } else {
                state.update(NodeAction.FETCH, NodeStateEnum.ERROR, "HTTP request failed: " + fetchResponse.getResponseCode());
            }
            return;
        }

        if (!fetchResponse.hasBody()) {
            state.update(NodeAction.FETCH, NodeStateEnum.ERROR, "expected response body, but got nothing.");
            return;
        }

        gateway.bindEmpty();
        try {
            JSONConvert.fromJSON(gateway, fetchResponse.getBody());
            state.update(NodeAction.FETCH, NodeStateEnum.REMOTE, "fetch successful, server responded with " + fetchResponse.getResponseCode());
        } catch (FormatException ex) {
            exc(ex, NodeAction.FETCH);
        }

        fetchResponse.setUsed();
    }

    private void handleResponses() {
        handlePushResponse();
        handleFetchResponse();
    }

    @Override
    public void push() {
        String payload = gateway.detached()
            ? HTTPConvert.deleteRequest(nodeID, config, gateway.idName())
            : HTTPConvert.putRequest(nodeID, gateway, config);

        if (!pushSocket.isConnected() || pushSocket.isClosed()) {
            reloadConfig();
        }

        handleResponses();

        pushSocket.send(payload);
    }

    @Override
    public void fetch() {
        String payload = HTTPConvert.getRequest(nodeID, config, gateway.idName());

        if (!fetchSocket.isConnected() || fetchSocket.isClosed()) {
            reloadConfig();
        }

        handleResponses();

        fetchSocket.send(payload);
    }

    @Override
    public VO checkout() {
        handleFetchResponse();
        return super.checkout();
    }

    @Override
    protected void reloadConfig() {
        pushSocket = reloadConfig(NodeAction.PUSH, pushSocket);
        fetchSocket = reloadConfig(NodeAction.FETCH, fetchSocket);
    }

    private SocketIO reloadConfig(NodeAction on, SocketIO socket) {
        try {
            if (socket != null) {
                socket.close();
            }
            return new SocketIO(
                config,
                on,
                action -> state.update(action, "Sent HTTP request"),
                action -> state.update(
                    action,
                    action == NodeAction.PUSH
                        ? NodeStateEnum.SYNC
                        : action == NodeAction.FETCH
                        ? NodeStateEnum.REMOTE
                        : NodeStateEnum.ERROR,
                    "Received HTTP response"
                ),
                ex -> exc(ex, NodeAction.other("HTTP request"))
            );
        } catch (IOException ex) {
            exc(ex, NodeAction.other("creation of TCP socket"));
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        pushSocket.close();
        fetchSocket.close();
    }
}