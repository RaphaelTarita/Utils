package com.tara.util.persistence.node.state;

import com.tara.util.id.UID;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class NodeState {
    private static final int DEFAULT_MAXHISTORYCOUNT = 1024;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    private LinkedList<String> history = new LinkedList<>();
    private final int maxHistoryCount;
    private long discardedEntries = 0;

    private final UID nodeID;
    private NodeStateEnum state;
    private String message;
    private LocalDateTime timestamp;

    private void record() {
        if (maxHistoryCount != -1 && history.size() >= maxHistoryCount) {
            history.removeFirst();
            discardedEntries++;
        }
        history.add(toString());
    }

    public NodeState(UID nodeID, int maxHistoryCount) {
        timestamp = LocalDateTime.now();
        this.nodeID = nodeID;
        state = NodeStateEnum.UNKNOWN;
        message = "state init";
        this.maxHistoryCount = maxHistoryCount;
    }

    public NodeState(UID nodeID) {
        this(nodeID, DEFAULT_MAXHISTORYCOUNT);
    }

    public void update(NodeStateEnum state, String message) {
        record();
        this.timestamp = LocalDateTime.now();
        this.state = state;
        this.message = message;
    }

    public void update(NodeStateEnum state) {
        update(state, "(no message)");
    }

    public void update(String message) {
        update(state, message);
    }

    public NodeStateEnum getEnumeratedState() {
        return state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[updated ")
          .append(FMT.format(timestamp))
          .append("] ")
          .append(state.padded())
          .append(": ")
          .append(message);

        return sb.toString();
    }

    public String getHistory() {
        StringBuilder sb = new StringBuilder();
        sb.append("history of node #id(")
          .append(nodeID.mapUID())
          .append("):\n\n");

        long counter = discardedEntries;
        for (String str : history) {
            sb.append(counter++)
              .append(".\t")
              .append(str)
              .append('\n');
        }

        sb.append("\nCurrent state: ")
          .append(toString());

        return sb.toString();
    }
}
