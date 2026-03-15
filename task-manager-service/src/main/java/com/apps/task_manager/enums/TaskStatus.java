package com.apps.task_manager.enums;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus {
    TO_DO(0),
    IN_PROGRESS(1),
    DONE(2);

    private final int status;

    private static final Map<Integer, TaskStatus> statusMap = new HashMap<>();

    static {
        for(TaskStatus status : TaskStatus.values()){
            statusMap.put(status.status, status);
        }
    }
    TaskStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }

    public static TaskStatus fromValue(int value){
        return statusMap.get(value);
    }
}
