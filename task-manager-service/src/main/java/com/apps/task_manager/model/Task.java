package com.apps.task_manager.model;

import com.apps.task_manager.enums.TaskStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="task")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private int status;
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dependency",
    joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "dependencies"))
    Set<Task> dependencies = new LinkedHashSet<>();

    public Task(){}

    public Task(String title, String description, int status, LocalDateTime createdAt, Set<Task> dependencies){
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.dependencies = dependencies;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDependencies(Set<Task> dependencies) {
        this.dependencies = dependencies;
    }

    public Set<Task> getDependencies(){
        return dependencies;
    }

    public void addDependency(Task task){
        this.dependencies.add(task);
    }
}
