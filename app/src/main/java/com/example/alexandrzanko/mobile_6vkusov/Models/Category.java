package com.example.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 3/7/17.
 */

public class Category{

    private String slug;
    private String title;
    private String urlImg;


    public Category( String title, String slug, String urlImg) {
        this.slug = slug;
        this.title = title;
        this.urlImg = urlImg;
    }

    public String getSlug() {return slug;}

    public String getUrlImg() {
        return urlImg;
    }

    public String getTitle() {
        return title;
    }
}
