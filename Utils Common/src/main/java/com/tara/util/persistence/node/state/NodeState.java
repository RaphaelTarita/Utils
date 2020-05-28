package com.tara.util.persistence.node.state;

import com.tara.util.helper.date.DateHelper;
import com.tara.util.mirror.Mirrorable;
import com.tara.util.mirror.Mirrors;
import com.tara.util.persistence.node.NodeAction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NodeState implements Mirrorable<NodeState> {
    static {
        Mirrors.registerSupplier(
            LocalDateTime.class,
            date -> DateHelper.convertLDT(DateHelper.copyDate(DateHelper.reconvLDT(date)))
        );
    }

    private NodeStateEnum state;

    private NodeAction action;
    private String message;
    private LocalDateTime timestamp;
    private NodeState prev;
    private final DateTimeFormatter formatter;

    private static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public NodeState(DateTimeFormatter formatter) {
        timestamp = now();
        state = NodeStateEnum.EMPTY;
        action = NodeAction.other("init");
        message = "<init>";
        prev = null;
        this.formatter = formatter;
    }

    public NodeState() {
        this(DateTimeFormatter.ofPattern("yyyy.MM.dd, HH:mm:ss.SSS"));
    }

    public void update(NodeAction on, NodeStateEnum state, String message) {
        prev = mirror();

        timestamp = now();
        this.state = state;
        this.action = on;
        this.message = message;
    }

    public void update(String on, NodeStateEnum state, String message) {
        update(NodeAction.other(on), state, message);
    }

    public void update(NodeAction on, NodeStateEnum state) {
        update(on, state, "");
    }

    public void update(String on, NodeStateEnum state) {
        update(NodeAction.other(on), state);
    }

    public void update(NodeAction on, String message) {
        update(on, state, message);
    }

    public void update(String on, String message) {
        update(NodeAction.other(on), message);
    }

    public NodeStateEnum getState() {
        return state;
    }

    public NodeAction getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    private void printHistory0(StringBuilder builder) {
        builder.append(timestamp.format(formatter))
            .append(": [")
            .append(state.toString())
            .append("]\ton '")
            .append(action)
            .append("' ")
            .append(message.isEmpty() ? "<no message>" : message);
        if (prev != null) {
            builder.append('\n');
            prev.printHistory0(builder);
        }
    }

    public String printHistory() {
        StringBuilder res = new StringBuilder();
        printHistory0(res);
        return res.toString();
    }

    @Override
    public NodeState mirror() {
        NodeState res = new NodeState();
        res.state = state;
        res.action = action;
        res.message = message;
        res.timestamp = Mirrors.mirror(timestamp);
        res.prev = prev == null
            ? null
            : prev.mirror();
        return res;
    }
}