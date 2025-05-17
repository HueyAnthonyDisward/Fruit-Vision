package com.example.fruitvision;

public class Fruit {
    private String name;
    private String order;
    private String family;
    private String genus;
    private int imageResId;
    private String details;

    public Fruit(String name, String order, String family, String genus, int imageResId, String details) {
        this.name = name;
        this.order = order;
        this.family = family;
        this.genus = genus;
        this.imageResId = imageResId;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getOrder() {
        return order;
    }

    public String getFamily() {
        return family;
    }

    public String getGenus() {
        return genus;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDetails() {
        return details;
    }
}