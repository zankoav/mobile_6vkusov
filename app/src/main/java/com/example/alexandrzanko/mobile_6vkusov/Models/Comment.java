package com.example.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 08/12/16.
 */

public class Comment {

    private Integer type;
    private String name;
    private String text;
    private String time;
    private String urlLogo;

    public Comment(String time, Integer type, String name, String text, String title, String logo) {
        this.time = time;
        this.name = name;
        this.type = type;
        this.text = text;
        this.urlLogo = logo;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time.substring(0, time.length() - 6);
    }


}
