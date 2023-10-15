package org.datapool.dto.api.internal;

public class CreateProjectRq {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public CreateProjectRq setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateProjectRq setDescription(String description) {
        this.description = description;
        return this;
    }
}
