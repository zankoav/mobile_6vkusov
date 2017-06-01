package by.vkus.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 3/7/17.
 */

public class Category{

    private String slug;
    private String title;
    private String urlImg;
    private int type;


    public Category( String title, String slug, String urlImg, int type) {
        this.slug = slug;
        this.title = title;
        this.urlImg = urlImg;
        this.type = type;

    }

    public int getType() {return type;}

    public String getSlug() {return slug;}

    public String getUrlImg() {
        return urlImg;
    }

    public String getTitle() {
        return title;
    }
}
