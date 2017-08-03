package by.vkus.alexandrzanko.mobile_6vkusov.Settings;

/**
 * Created by alexandrzanko on 8/3/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsApp {

    @SerializedName("image_path")
    @Expose
    private Image_path image_path;

    public Image_path getImage_path() {
        return image_path;
    }

    public void setImage_path(Image_path image_path) {
        this.image_path = image_path;
    }


}
