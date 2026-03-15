package com.apps.task_manager.resource;

import com.apps.task_manager.dto.ExecutionOrderDTO;
import com.apps.task_manager.dto.TaskDTO;
import com.apps.task_manager.dto.ErrorResponseDTO;
import com.apps.task_manager.exception.NoSuchTaskException;
import com.apps.task_manager.helpers.TaskHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service/tasks")
public class TaskResource {

    private final TaskHelper helper;

    @Autowired
    public TaskResource(TaskHelper helper){
        this.helper = helper;
    }

    @PostMapping("")
    public ResponseEntity createTask(@RequestBody TaskDTO taskDTO){
        taskDTO = helper.createTask(taskDTO);
        return new ResponseEntity(taskDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{task_id}")
    public ResponseEntity getTask(@PathVariable("task_id") String taskId){
        TaskDTO taskDTO = helper.getTask(taskId);
        return new ResponseEntity(taskDTO, HttpStatus.OK);
    }

    @PutMapping("/{task_id}")
    public ResponseEntity updateTask(@PathVariable("task_id") String taskId, @RequestBody TaskDTO taskDTO){
        taskDTO = helper.updateTask(taskId, taskDTO);
        return new ResponseEntity(taskDTO, HttpStatus.OK);
    }

    @GetMapping("/{task_id}/execution-order")
    public ResponseEntity getExecutionOrder(@PathVariable("task_id") String taskId){
        ExecutionOrderDTO executionOrderDTO = helper.getExecutionOrder(taskId);
        return new ResponseEntity(executionOrderDTO, HttpStatus.OK);
    }
}
