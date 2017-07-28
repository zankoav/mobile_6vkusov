package by.vkus.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 3/7/17.
 */

public class Category{

    private String slug;
    private String name;
    private String image;
    private int type;


    public Category(String name, String slug, String image, int type) {
        this.slug = slug;
        this.name = name;
        this.image = image;
        this.type = type;

    }

    public int getType() {return type;}

    public String getSlug() {return slug;}

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
