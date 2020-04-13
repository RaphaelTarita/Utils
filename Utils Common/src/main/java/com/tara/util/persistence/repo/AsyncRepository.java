package com.tara.util.persistence.repo;

import com.tara.util.async.tasks.SchedulerConfig;
import com.tara.util.async.tasks.Task;
import com.tara.util.async.tasks.TaskScheduler;
import com.tara.util.async.tasks.criteria.TimeCriterion;
import com.tara.util.async.tasks.procedure.ProcedureException;
import com.tara.util.id.TaskID;
import com.tara.util.id.UID;
import com.tara.util.persistence.node.NodeType;
import com.tara.util.persistence.node.ResourceNode;
import com.tara.util.persistence.node.config.DBConfig;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.node.config.HTTPConfig;
import com.tara.util.persistence.node.config.NodeConfig;
import com.tara.util.persistence.node.state.NodeStateEnum;

import java.util.HashMap;
import java.util.Map;

public class AsyncRepository<VO> extends AbstractRepository<VO> {
    private static final long RETRY = 200;
    private static final long RECOVER = 1000;
    private final TaskScheduler taskScheduler;
    private final long expiryMillis;
    private final Map<UID, TaskID> idMap = new HashMap<>();

    private TaskScheduler initScheduler() {
        SchedulerConfig config = SchedulerConfig.builder()
            .withRetryCycle(RETRY)
            .withRecoverCycle(RECOVER)
            .withThreadName(target.getSimpleName() + " Repository Update Scheduler")
            .build();

        return new TaskScheduler(config);
    }

    public AsyncRepository(Class<VO> target, long expiryMillis, DBConfig dbConf, HTTPConfig httpConf, FileConfig fileConf) {
        super(target, dbConf, httpConf, fileConf);
        this.expiryMillis = expiryMillis;
        taskScheduler = initScheduler();
    }

    public AsyncRepository(Class<VO> target, long expiryMillis, DBConfig dbConf, HTTPConfig httpConf) {
        super(target, dbConf, httpConf);
        this.expiryMillis = expiryMillis;
        taskScheduler = initScheduler();
    }

    public AsyncRepository(Class<VO> target, long expiryMillis, DBConfig dbConf, FileConfig fileConf) {
        super(target, dbConf, fileConf);
        this.expiryMillis = expiryMillis;
        taskScheduler = initScheduler();
    }

    public AsyncRepository(Class<VO> target, long expiryMillis, HTTPConfig httpConf, FileConfig fileConf) {
        super(target, httpConf, fileConf);
        this.expiryMillis = expiryMillis;
        taskScheduler = initScheduler();
    }

    public AsyncRepository(Class<VO> target, long expiryMillis, DBConfig dbConf) {
        super(target, dbConf);
        this.expiryMillis = expiryMillis;
        taskScheduler = initScheduler();
    }

    public AsyncRepository(Class<VO> target, long expiryMillis, HTTPConfig httpConf) {
        super(target, httpConf);
        this.expiryMillis = expiryMillis;
        taskScheduler = initScheduler();
    }

    public AsyncRepository(Class<VO> target, long expiryMillis, FileConfig fileConf) {
        super(target, fileConf);
        this.expiryMillis = expiryMillis;
        taskScheduler = initScheduler();
    }

    public AsyncRepository(Class<VO> target, long expiryMillis) {
        super(target);
        this.expiryMillis = expiryMillis;
        taskScheduler = initScheduler();
    }

    @Override
    public void registerNode(UID id, NodeType type, NodeConfig config) {
        if (repo.isEmpty()) {
            taskScheduler.start();
        }

        super.registerNode(id, type, config);

        final ResourceNode<VO> node = repo.get(id);
        Task nodeUpdateTask = new Task(
            id.mapUID(),
            () -> {
                updateSingle(node);
                if (node.getState().getState() == NodeStateEnum.ERROR) {
                    throw new ProcedureException(node.getState().getMessage());
                }
            },
            new TimeCriterion(expiryMillis)
        );
        idMap.put(id, nodeUpdateTask.id());
        nodeUpdateTask.schedule();
        taskScheduler.addTask(nodeUpdateTask);
    }

    @Override
    public void registerNode(UID id, NodeType type) {
        registerNode(id, type, resolveDefaultConfig(type));
    }

    @Override
    public void unregisterNode(UID id) {
        super.unregisterNode(id);
        TaskID taskID = idMap.get(id);
        taskScheduler.removeTask(taskID);

        if (repo.isEmpty()) {
            try {
                taskScheduler.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void update(Priority priority) {
        repo.forEach((id, node) -> updateSingle(node, priority));
    }
}
