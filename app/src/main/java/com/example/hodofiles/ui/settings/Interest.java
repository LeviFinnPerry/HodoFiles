package com.example.hodofiles.ui.settings;

public class Interest {

    private String name;
    private int imageResId;

    public Interest(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}

