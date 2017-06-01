package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONObject;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class SettingsFragment extends Fragment implements LoadJson {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.fragment_restaurant_comments_layout, container, false);
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {

    }
}
