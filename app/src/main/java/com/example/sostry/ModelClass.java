package com.example.sostry;

public class ModelClass {

    private int imageResourse;
    private String title , body , num;

    public ModelClass(int imageResourse, String title, String body , String num) {
        this.imageResourse = imageResourse;
        this.title = title;
        this.body = body;
        this.num = num;
    }

    public String  getnum()
    {
        return num;
    }

    public void changeText111(String text)
    {
        title = text;
    }

    public int getImageResourse() {
        return imageResourse;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
