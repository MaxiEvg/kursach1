package com.kursovaya.receptorganaizer;

public class Recipe {
    private int id;
    private String title;
    private String description;
    private String imagePath;

    public Recipe() {}

    public Recipe(String title, String description, String imagePath) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
