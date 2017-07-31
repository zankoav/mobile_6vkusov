package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.os.Bundle;
import by.vkus.alexandrzanko.mobile_6vkusov.R;

public class OurPints extends BaseMenuActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_pints);
        this.initViews(this.getString(R.string.our_points_title));
    }

}
