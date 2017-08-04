package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.LocalStorage;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStore;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alexandrzanko on 7/31/17.
 */

public abstract class BaseMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    public final String TAG = this.getClass().getSimpleName();

    private BaseMenuActivity instance;
    private CounterFab fab;

    public BaseMenuActivity(){
        super();
        this.instance = this;
    }

    public void setFabCount(int count){
        fab.setCount(count);
    }

    public void increaseFabCount(){
        fab.increase();
    }

    public void decreaseFabCount(){
        fab.decrease();
    }

    public void hideBasket(){
        fab.setVisibility(View.GONE);
    }

    public void showBasket(){
        fab.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        int count = Singleton.currentState().getIUser().getBasket().getCountItems();
        fab.setCount(count);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView userLogo = (CircleImageView) headerView.findViewById(R.id.menu_iv_user);
        TextView userName = (TextView) headerView.findViewById(R.id.menu_name);
        TextView userEmail = (TextView) headerView.findViewById(R.id.menu_email);
        TextView userPoints = (TextView) headerView.findViewById(R.id.menu_points);
        IUser user = Singleton.currentState().getIUser();
        if(user.getStatus().equals(STATUS.REGISTER)){
            userEmail.setText(user.getEmail());
            userName.setText(user.getFirstName());
            userPoints.setText(user.getPoints().toString() + " баллов");
            String avatarUrl =  ApiController.BASE_URL +
                    Singleton.currentState().getSettingsApp().getImage_path().getUser() +
                    user.getAvatar();
            Picasso.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.user) //показываем что-то, пока не загрузится указанная картинка
                    .error(R.drawable.user) // показываем что-то, если не удалось скачать картинку
                    .into(userLogo);
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
            if (!TAG.equals("CategoriesActivity")){
                Intent intent = new Intent(this, CategoriesActivity.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_free_food) {
            if (!TAG.equals("FreeFoodActivity")) {
                Intent intent = new Intent(this, FreeFoodActivity.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_our_bonuses) {
            if (!TAG.equals("OurPints")) {
                Intent intent = new Intent(this, OurPints.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_favorites) {
            if (!TAG.equals("FavoritesRestaurantsActivity")) {
                Intent intent = new Intent(this, FavoritesRestaurantsActivity.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_info) {
            if (!TAG.equals("InfoActivity")) {
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Вы хотите выйдти?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    SessionStore store = Singleton.currentState().getSessionStore();
                    store.clearKeyStorage(store.USER_SESSION);
                    Intent intent = new Intent(instance, MainActivity.class);
                    startActivity(intent);
                    instance.finish();
                }
            });
            builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                }
            });
            builder.setCancelable(true);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {

                }
            });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initViews(String title) {

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        fab = (CounterFab) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Singleton.currentState().getIUser().getBasket().getCountItems();
                if (count>0){
                    Intent intent = new Intent(instance, BasketActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar.make(view, "Ваша корзина пуста", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();


    }

    public void loginButtonClick(View view) {
        STATUS status = Singleton.currentState().getIUser().getStatus();
        Intent intent = status == STATUS.GENERAL? new Intent(this, LoginActivity.class): new Intent(this,ProfileActivity.class);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        this.startActivity(intent);
        this.finish();
    }

}
