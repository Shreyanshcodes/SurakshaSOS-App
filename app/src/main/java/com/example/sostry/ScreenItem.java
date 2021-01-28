package com.example.sostry;

public class ScreenItem {

    String Title,Description;
    int Screenimage;

    public ScreenItem(String title, String description, int screenimage) {
        Title = title;
        Description = description;
        Screenimage = screenimage;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setScreenimage(int screenimage) {
        Screenimage = screenimage;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getScreenimage() {
        return Screenimage;
    }
}
