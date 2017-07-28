package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.CategoryListAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IApi;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.MCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static IApi api;
    List<MCategory> mCategories;


    private final String SLUG = "slug";
    private final String NAME = "name";
    private final String FAVORITE = "favorite";

    private Singleton singleton = Singleton.currentState();

    private ListView listView;
    private CategoryListAdapter adapter;

    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_categories_v2);

        api = ApiController.getApi();
        mCategories = new ArrayList<>();


        api.getCategory().enqueue(new Callback<List<MCategory>>() {
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
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(by.vkus.alexandrzanko.mobile_6vkusov.R.string.categories_title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ваша корзина пуста", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView userLogo = (CircleImageView) headerView.findViewById(R.id.menu_iv_user);
        TextView userName = (TextView) headerView.findViewById(R.id.menu_name);
        TextView userEmail = (TextView) headerView.findViewById(R.id.menu_email);
        TextView userPoints = (TextView) headerView.findViewById(R.id.menu_points);

        if(singleton.getUser().getStatus().equals(STATUS.REGISTER)){
            JSONObject userJson = singleton.getUser().getProfile();
            try {
                String email = userJson.getString("email");
                String name = userJson.getString("firstName");
                String points = String.valueOf(singleton.getUser().getPoints());
                userName.setText(name);
                userEmail.setText(email);
                userPoints.setText(points + " баллов");
                String urlIcon = this.getResources().getString(by.vkus.alexandrzanko.mobile_6vkusov.R.string.api_base) + userJson.getString("img_path")+"/"+ userJson.getString("avatar");
                Picasso.with(this)
                        .load(urlIcon)
                        .placeholder(R.drawable.user) //показываем что-то, пока не загрузится указанная картинка
                        .error(R.drawable.user) // показываем что-то, если не удалось скачать картинку
                        .into(userLogo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            userPoints.setVisibility(View.VISIBLE);
            userLogo.setVisibility(View.VISIBLE);
            userEmail.setVisibility(View.VISIBLE);
        }else{
            userLogo.setVisibility(View.GONE);
            userName.setText("Войти");
            userPoints.setVisibility(View.GONE);
            userEmail.setVisibility(View.GONE);
            navigationView.getMenu().getItem(4).getSubMenu().getItem(1).setVisible(false);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_food) {

        } else if (id == R.id.nav_free_food) {

        } else if (id == R.id.nav_our_bonuses) {

        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loginButtonClick(View view) {
        STATUS status = singleton.getUser().getStatus();
        Intent intent = status == STATUS.GENERAL? new Intent(this, LoginActivity.class): new Intent(this,ProfileActivity.class);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        this.startActivity(intent);
        this.finish();
    }
}
