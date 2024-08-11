package com.example.to_do_app;

public class Task {
    // Fields
    private String id;
    private String title;
    private String description;

    // Default constructor for DataSnapshot.getValue(Task.class)
    public Task() {
    }

    // Parameterized Constructor
    public Task(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters for task id, title, description
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
