package com.apps.task_manager.helpers;

import com.apps.task_manager.dto.ExecutionOrderDTO;
import com.apps.task_manager.dto.TaskDTO;
import com.apps.task_manager.enums.TaskStatus;
import com.apps.task_manager.exception.DuplicateTaskException;
import com.apps.task_manager.exception.InvalidDependencyException;
import com.apps.task_manager.exception.InvalidOperationException;
import com.apps.task_manager.exception.NoSuchTaskException;
import com.apps.task_manager.model.Task;
import com.apps.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskHelper {

    @Autowired
    private TaskRepository taskRepository;

    private static final String NO_SUCH_TASK_EXISTS_EXCEPTION_MESSAGE = "No such task exists. task_id: ";

    private static final String INVALID_STATUS_UPDATE = "Invalid task status updation";

    private static final String INVALID_DEPENDENCY_MESSAGE = "Invalid dependency added";

    public TaskDTO createTask(TaskDTO taskDTO){
        if(isValidTask(taskDTO)){
            Set<Task> dependencies = taskDTO.getDependencies().stream().map(taskId -> taskRepository.findById(taskId).get()).collect(Collectors.toSet());
            Task task = new Task(taskDTO.getTitle(), taskDTO.getDescription(),
                    TaskStatus.TO_DO.getStatus(), LocalDateTime.now(), dependencies);
            task = taskRepository.save(task);
            taskDTO = new TaskDTO(task.getTitle(), task.getDescription(), task.getDependencies().stream().map(t -> t.getId()).collect(Collectors.toSet()));
            taskDTO.setId(task.getId());
            taskDTO.setCreatedAt(task.getCreatedAt());
            taskDTO.setStatus(TaskStatus.fromValue(task.getStatus()));
            return taskDTO;
        } else {
            throw new NoSuchTaskException(NO_SUCH_TASK_EXISTS_EXCEPTION_MESSAGE);
        }
    }

    public TaskDTO getTask(String taskId){
        Task task = taskRepository.findById(Long.valueOf(taskId)).orElseThrow(() ->
                new NoSuchTaskException(NO_SUCH_TASK_EXISTS_EXCEPTION_MESSAGE + taskId));
        TaskDTO taskDTO = new TaskDTO(task.getTitle(), task.getDescription(), new HashSet<>());
        taskDTO.setId(task.getId());
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setStatus(TaskStatus.fromValue(task.getStatus()));
        Set<Long> dependencies = task.getDependencies().stream().map(t -> t.getId()).collect(Collectors.toSet());
        taskDTO.setDependencies(dependencies);
        return taskDTO;
    }

    public TaskDTO updateTask(String taskId, TaskDTO taskDTO){
        taskDTO.setId(Long.valueOf(taskId));
        Task task = taskRepository.findById(Long.valueOf(taskId)).orElseThrow(() ->
                new NoSuchTaskException(NO_SUCH_TASK_EXISTS_EXCEPTION_MESSAGE + taskId));
        if(isValidTask(taskDTO)){
            if(!isTaskBlocked(task, taskDTO) && checkValidStatusUpdate(task, taskDTO)){
                if(!isCyclePresent(task, taskDTO)){
                    task.setTitle(taskDTO.getTitle());
                    task.setDescription(taskDTO.getDescription());
                    task.setStatus(taskDTO.getStatus().getStatus());
                    task.setDependencies(taskDTO.getDependencies().stream().map(tid -> taskRepository.findById(tid).get()).collect(Collectors.toSet()));
                    task = taskRepository.save(task);

                    taskDTO = new TaskDTO(task.getTitle(), task.getDescription(), new HashSet<>());
                    taskDTO.setId(task.getId());
                    taskDTO.setCreatedAt(task.getCreatedAt());
                    taskDTO.setStatus(TaskStatus.fromValue(task.getStatus()));
                    Set<Long> dependencies = task.getDependencies().stream().map(t -> t.getId()).collect(Collectors.toSet());
                    taskDTO.setDependencies(dependencies);
                    return taskDTO;
                }else{
                    throw new InvalidDependencyException(INVALID_DEPENDENCY_MESSAGE);
                }
            }else{
                throw new InvalidOperationException(INVALID_STATUS_UPDATE);
            }

        } else {
            throw new NoSuchTaskException(NO_SUCH_TASK_EXISTS_EXCEPTION_MESSAGE);
        }
    }

    public boolean isTaskBlocked(Task task, TaskDTO taskDTO){
        if(taskDTO.getStatus() != TaskStatus.TO_DO){
            Set<Task> dependencies = task.getDependencies();
            for(Task dependency : dependencies){
                if(dependency.getStatus() != TaskStatus.DONE.getStatus()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCyclePresent(Task task, TaskDTO taskDTO){
        Task clonedTask = new Task(task.getTitle(), task.getDescription(),
                task.getStatus(), task.getCreatedAt(), task.getDependencies());
        for(long taskId : taskDTO.getDependencies()){
            Task newDependency = taskRepository.findById(taskId).get();
            task.addDependency(newDependency);
        }
        Set<Task> visited = new HashSet<>();
        Set<Task> recStack = new HashSet<>();
        if(checkCycleInDependencies(clonedTask, visited, recStack)){
            return true;
        }
        return false;
    }

    public boolean checkCycleInDependencies(Task task, Set<Task> visited, Set<Task> recStack){
        if(recStack.contains(task)){
            return true;
        }
        if(visited.contains(task)){
            return false;
        }
        visited.add(task);
        recStack.add(task);
        for(Task dependency : task.getDependencies()){
            if(checkCycleInDependencies(dependency, visited, recStack)){
                return true;
            }
        }
        recStack.remove(task);
        return false;
    }

    public ExecutionOrderDTO getExecutionOrder(String taskId){
        Task task = taskRepository.findById(Long.valueOf(taskId)).orElseThrow(() ->
                new NoSuchTaskException(NO_SUCH_TASK_EXISTS_EXCEPTION_MESSAGE + taskId));
        Stack<Task> taskStack = new Stack<>();
        Set<Task> tasksChecked = new HashSet<>();
        Set<Task> executionOrder = new LinkedHashSet<>();
        Set<TaskDTO> executionOrderDTO = new LinkedHashSet<>();
        for(Task dependency : task.getDependencies()){
            if(!tasksChecked.contains(dependency)){
                getExecutionOrderRecursive(dependency, taskStack, tasksChecked);
            }
        }
        while(!taskStack.isEmpty()){
            executionOrder.add(taskStack.pop());
        }
        List<Task> executionOrderList = new ArrayList<>(executionOrder);
        Collections.reverse(executionOrderList);
        for(Task orderedTask : executionOrderList){
            TaskDTO taskDTO = new TaskDTO(orderedTask.getTitle(), orderedTask.getDescription(),
                    orderedTask.getDependencies().stream().map(t -> t.getId()).collect(Collectors.toSet()));
            taskDTO.setId(orderedTask.getId());
            taskDTO.setStatus(TaskStatus.fromValue(orderedTask.getStatus()));
            taskDTO.setCreatedAt(orderedTask.getCreatedAt());
            executionOrderDTO.add(taskDTO);
        }
        TaskDTO taskDTO = new TaskDTO(task.getTitle(), task.getDescription(),
                task.getDependencies().stream().map(t -> t.getId()).collect(Collectors.toSet()));
        taskDTO.setId(task.getId());
        taskDTO.setStatus(TaskStatus.fromValue(task.getStatus()));
        taskDTO.setCreatedAt(task.getCreatedAt());
        return new ExecutionOrderDTO(taskDTO, executionOrderDTO);
    }

    private void getExecutionOrderRecursive(Task task, Stack<Task> taskStack, Set<Task> tasksChecked){
        for(Task dependency : task.getDependencies()){
            if(!tasksChecked.contains(dependency)){
                getExecutionOrderRecursive(dependency, taskStack, tasksChecked);
            }
        }
        tasksChecked.add(task);
        taskStack.push(task);
    }

    private boolean isValidTask(TaskDTO taskDTO){
        long taskId = taskDTO.getId();
        Set<Long> dependencies = taskDTO.getDependencies();
        for(long dependency : dependencies){
            taskRepository.findById(dependency).orElseThrow(() ->
                    new NoSuchTaskException(NO_SUCH_TASK_EXISTS_EXCEPTION_MESSAGE + dependency));
        }
        if(taskId != 0){
            if(dependencies.contains(taskId)){
                throw new DuplicateTaskException("Dependency contains same task ID");
            }
        }
        return true;
    }

    private boolean checkValidStatusUpdate(Task task, TaskDTO taskDTO){
        if(task.getStatus() != taskDTO.getStatus().getStatus()){
            if(task.getStatus() == TaskStatus.TO_DO.getStatus() && taskDTO.getStatus().getStatus() == TaskStatus.IN_PROGRESS.getStatus()){
                return true;
            } else if(task.getStatus() == TaskStatus.IN_PROGRESS.getStatus() && taskDTO.getStatus().getStatus() == TaskStatus.DONE.getStatus()){
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
