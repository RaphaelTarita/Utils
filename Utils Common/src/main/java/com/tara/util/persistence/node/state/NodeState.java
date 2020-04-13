package com.tara.util.persistence.node.state;

import com.tara.util.helper.date.DateHelper;
import com.tara.util.mirror.Mirrorable;
import com.tara.util.mirror.Mirrors;

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
        message = "<init>";
        prev = null;
        this.formatter = formatter;
    }

    public NodeState() {
        this(DateTimeFormatter.ofPattern("yyyy.MM.dd, HH:mm:ss.SSS"));
    }

    public void update(NodeStateEnum state, String message) {
        prev = mirror();

        timestamp = now();
        this.state = state;
        this.message = message;
    }

    public void update(NodeStateEnum state) {
        update(state, "");
    }

    public void update(String message) {
        update(state, message);
    }

    public NodeStateEnum getState() {
        return state;
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
            .append("]\t")
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
        res.message = message;
        res.timestamp = Mirrors.mirror(timestamp);
        res.prev = prev == null
            ? null
            : prev.mirror();
        return res;
    }
}
