package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.os.Bundle;

import by.vkus.alexandrzanko.mobile_6vkusov.R;

public class InfoActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initViews(this.getString(R.string.info_title));
    }
}
