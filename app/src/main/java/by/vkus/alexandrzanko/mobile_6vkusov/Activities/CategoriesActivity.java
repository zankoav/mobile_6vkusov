package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.CategoryListAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends BaseMenuActivity {

    private final String SLUG = "slug";
    private final String NAME = "name";
    private final String FAVORITE = "favorite";

    private ListView listView;
    private CategoryListAdapter adapter;
    private List<MCategory> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_categories);
        mCategories = new ArrayList<>();
        this.loadCategories();
        this.initViews(this.getString(R.string.categories_title));
        listView = (ListView) findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.category_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                MCategory category = (MCategory)listView.getItemAtPosition(position);
                Intent intent = new Intent(CategoriesActivity.this, RestaurantsCardActivity.class);
                String slug = category.getSlug();
                intent.putExtra(SLUG, slug);
                intent.putExtra(NAME, category.getName());
                intent.putExtra(FAVORITE, false);
                startActivity(intent);
            }
        });
    }

    public void loadCategories(){
        ApiController.getApi().getCategory().enqueue(new Callback<List<MCategory>>() {
            @Override
            public void onResponse(Call<List<MCategory>> call, Response<List<MCategory>> response) {
                mCategories.addAll(response.body());
                adapter = new CategoryListAdapter(CategoriesActivity.this, mCategories);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<MCategory>> call, Throwable t) {
                Toast.makeText(CategoriesActivity.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
