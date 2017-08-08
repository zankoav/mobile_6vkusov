package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    /**
     * Created by alexandrzanko on 29/11/16.
     */

    public class InfoFragment extends Fragment {


        private final String TAG = this.getClass().getSimpleName();
        private TextView textView;
        private String slug;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ApiController.getApi().getInfoByRestaurantSlug(slug).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.code() == 200){
                        String description = response.body();
                        if (description != null ){
                            textView.setText(description);
                        }
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_restaurant_info_layout, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            textView = (TextView)getView().findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.textView);
            textView.setMovementMethod(new ScrollingMovementMethod());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView.setNestedScrollingEnabled(true);
            }
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }
    }

