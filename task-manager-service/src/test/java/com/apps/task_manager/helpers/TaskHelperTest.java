package com.apps.task_manager.helpers;

import com.apps.task_manager.dto.TaskDTO;
import com.apps.task_manager.enums.TaskStatus;
import com.apps.task_manager.exception.InvalidOperationException;
import com.apps.task_manager.exception.NoSuchTaskException;
import com.apps.task_manager.model.Task;
import com.apps.task_manager.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskHelperTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private Task task;

    @InjectMocks
    private TaskHelper helper = new TaskHelper();

    @Test
    void testCreateTaskSuccess(){
        TaskDTO taskDTO = new TaskDTO("task title", "task description", new HashSet<>());
        task.setId(1L);
        when(repository.save(any())).thenReturn(task);
        when(task.getTitle()).thenReturn("title");
        when(task.getDescription()).thenReturn("desc");
        when(task.getDependencies()).thenReturn(new HashSet<>());
        when(task.getId()).thenReturn(1L);
        when(task.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(task.getStatus()).thenReturn(0);

        taskDTO = helper.createTask(taskDTO);
        Assertions.assertNotNull(taskDTO);
        Assertions.assertEquals(1L, taskDTO.getId());
    }

    @Test
    void testCreateTaskFailureInvalidDependency(){
        Set<Long> dependencies = new HashSet<>();
        dependencies.add(1L);
        TaskDTO taskDTO = new TaskDTO("task title", "task description", dependencies);
        when(repository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchTaskException.class, ()->helper.createTask(taskDTO));
    }

    @Test
    void testGetTaskSuccess(){
        Set<Task> dependencies = new HashSet<>();
        dependencies.add(task);
        when(repository.findById(any())).thenReturn(Optional.of(task));
        when(task.getTitle()).thenReturn("title");
        when(task.getDescription()).thenReturn("desc");
        when(task.getDependencies()).thenReturn(dependencies);
        when(task.getId()).thenReturn(1L);
        when(task.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(task.getStatus()).thenReturn(0);

        TaskDTO taskDTO = helper.getTask("1");
        Assertions.assertNotNull(taskDTO);
        Assertions.assertEquals(1L, taskDTO.getId());
    }

    @Test
    void testGetTaskFailureInvalidTaskId(){
        when(repository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchTaskException.class, ()->helper.getTask("1"));
    }

    @Test
    void testUpdateTaskSuccess(){
        Set<Task> dependencies = new HashSet<>();
        dependencies.add(task);
        Set<Long> dependenciesIds = new HashSet<>();
        dependenciesIds.add(2L);
        TaskDTO taskDTO = new TaskDTO("task title", "task description", dependenciesIds);
        taskDTO.setStatus(TaskStatus.DONE);
        when(repository.findById(any())).thenReturn(Optional.of(task));
        when(repository.save(any())).thenReturn(task);
        when(task.getTitle()).thenReturn("title");
        when(task.getDescription()).thenReturn("desc");
        when(task.getId()).thenReturn(1L);
        when(task.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(task.getStatus()).thenReturn(2);

        taskDTO = helper.updateTask("1", taskDTO);
        Assertions.assertNotNull(taskDTO);
        Assertions.assertEquals(1L, taskDTO.getId());
    }

    @Test
    void testUpdateTaskFailureInvalidStatusUpdateBlocked(){
        Set<Task> dependencies = new HashSet<>();
        dependencies.add(task);
        Set<Long> dependenciesIds = new HashSet<>();
        dependenciesIds.add(2L);
        TaskDTO taskDTO = new TaskDTO("task title", "task description", dependenciesIds);
        taskDTO.setStatus(TaskStatus.IN_PROGRESS);
        when(repository.findById(any())).thenReturn(Optional.of(task));
        when(task.getStatus()).thenReturn(0);
        when(task.getDependencies()).thenReturn(dependencies);

        Assertions.assertThrows(InvalidOperationException.class, ()->helper.updateTask("1", taskDTO));
    }

    @Test
    void testUpdateTaskFailureInvalidStatusUpdate(){
        Set<Task> dependencies = new HashSet<>();
        dependencies.add(task);
        Set<Long> dependenciesIds = new HashSet<>();
        dependenciesIds.add(2L);
        TaskDTO taskDTO = new TaskDTO("task title", "task description", dependenciesIds);
        taskDTO.setStatus(TaskStatus.TO_DO);
        when(repository.findById(any())).thenReturn(Optional.of(task));
        when(task.getStatus()).thenReturn(2);

        Assertions.assertThrows(InvalidOperationException.class, ()->helper.updateTask("1", taskDTO));
    }
}
