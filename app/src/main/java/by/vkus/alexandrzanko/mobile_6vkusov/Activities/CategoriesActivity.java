package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.CategoryListAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Category;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private final String SLUG = "slug";
    private final String NAME = "name";
    private final String FAVORITE = "favorite";

    private Singleton singleton = Singleton.currentState();

    private ArrayList<Category> mainCategories;
    private ArrayList<Category> secondaryCategories;

    private ListView listView;
    private CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_categories);

        addToolBarToScreen();
        mainCategories = singleton.getStore().getMainCategories();
        secondaryCategories = singleton.getStore().getSecondaryCategories();
        listView = (ListView) findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.category_list);
        adapter = new CategoryListAdapter(CategoriesActivity.this, mainCategories, secondaryCategories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Category category = (Category)listView.getItemAtPosition(position);
                Intent intent = new Intent(CategoriesActivity.this, RestaurantsCardActivity.class);
                String slug = category.getSlug();
                intent.putExtra(SLUG, slug);
                intent.putExtra(NAME, category.getTitle());
                intent.putExtra(FAVORITE, false);
                startActivity(intent);
            }
        });
    }

    private void addToolBarToScreen() {
        Toolbar toolbar = (Toolbar)findViewById(by.vkus.alexandrzanko.mobile_6vkusov.R.id.toolbar);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.categories_title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
