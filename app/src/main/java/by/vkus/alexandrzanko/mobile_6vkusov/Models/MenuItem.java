package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import android.graphics.drawable.Drawable;

/**
 * Created by alexandrzanko on 25/11/16.
 */

public class MenuItem {

    private Drawable img;
    private String title;

    public MenuItem(Drawable img, String title) {
        this.img = img;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getImgId() {
        return img;
    }
}
