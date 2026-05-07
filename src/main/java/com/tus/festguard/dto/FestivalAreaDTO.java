package com.tus.festguard.dto;

public class FestivalAreaDTO {
    private String name;
    private String description;
    private String areaType;

    public FestivalAreaDTO() {}

    public FestivalAreaDTO(String name, String description, String areaType) {
        this.name = name;
        this.description = description;
        this.areaType = areaType;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAreaType() { return areaType; }
    public void setAreaType(String areaType) { this.areaType = areaType; }
}
