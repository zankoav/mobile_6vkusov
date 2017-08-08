package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.CommentsAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MComment;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

/**
 * Created by alexandrzanko on 29/11/16.
 */

public class CommentsFragment extends Fragment {

    private String slug;
    private CommentsAdapter adapter;
    private List<MComment> comments;
    static ListView listView;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiController.getApi().getCommentsByRestaurantSlug(slug).enqueue(new Callback<List<MComment>>() {
            @Override
            public void onResponse(Call<List<MComment>> call, Response<List<MComment>> response) {
                if(response.code() == 200){
                    comments = response.body();
                    if(comments != null){
                        adapter  = new CommentsAdapter(CommentsFragment.this.getActivity(), comments);
                        listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MComment>> call, Throwable t) {
                Log.i(TAG, "onFailure: CommentsFragment");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_comments_layout, container, false);
        listView = (ListView) rootView.findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.listComments);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
        return rootView;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
