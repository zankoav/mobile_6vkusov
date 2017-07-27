package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.CategoryListAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Category;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.UserInterface;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private final String SLUG = "slug";
    private final String NAME = "name";
    private final String FAVORITE = "favorite";

    private Singleton singleton = Singleton.currentState();

    private ArrayList<Category> mainCategories;
    private ArrayList<Category> secondaryCategories;

    private ListView listView;
    private CategoryListAdapter adapter;

    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.vkus.alexandrzanko.mobile_6vkusov.R.layout.activity_categories_v2);

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
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
