package com.apps.task_manager.dto;

import com.apps.task_manager.model.Task;

import java.io.Serializable;
import java.util.Set;

public class ExecutionOrderDTO implements Serializable {
    TaskDTO task;
    Set<TaskDTO> executionOrder;

    public ExecutionOrderDTO(){}

    public ExecutionOrderDTO(TaskDTO task, Set<TaskDTO> executionOrder){
        this.task = task;
        this.executionOrder = executionOrder;
    }

    public Set<TaskDTO> getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Set<TaskDTO> executionOrder) {
        this.executionOrder = executionOrder;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }
}
