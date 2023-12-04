package com.example.proyectusfinal.Models;

public class Slider {
    private int Image;
    private String slideName;

    public Slider(int image,String slideName) {
        Image = image;
        this.slideName = slideName;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getSlideName() {
        return slideName;
    }

    public void setSlideName(String slideName) {
        this.slideName = slideName;
    }
}
