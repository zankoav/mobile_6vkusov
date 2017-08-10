package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantsActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.CategoryListAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class CategoriesActivityV2 extends BaseMenuActivity {

    private ListView listView;
    private CategoryListAdapter adapter;
    private List<MCategory> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_categories);
        this.loadCategories();
        this.initViews(this.getString(R.string.categories_title));
        listView = (ListView) findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.category_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                MCategory category = (MCategory)listView.getItemAtPosition(position);
                Intent intent = new Intent(CategoriesActivityV2.this, RestaurantsActivityV2.class);
                String slug = category.getSlug();
                intent.putExtra(RestaurantsActivityV2.EXTRA_SLUG, slug);
                intent.putExtra(RestaurantsActivityV2.EXTRA_CATEGORY_NAME, category.getName());
                startActivity(intent);
                finish();
            }
        });
    }

    public void loadCategories(){
        ApiController.getApi().getCategory().enqueue(new Callback<List<MCategory>>() {
            @Override
            public void onResponse(Call<List<MCategory>> call, Response<List<MCategory>> response) {
                if(response.code() == 200){
                    mCategories = response.body();
                    adapter = new CategoryListAdapter(CategoriesActivityV2.this, mCategories);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<MCategory>> call, Throwable t) {
                Toast.makeText(CategoriesActivityV2.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
