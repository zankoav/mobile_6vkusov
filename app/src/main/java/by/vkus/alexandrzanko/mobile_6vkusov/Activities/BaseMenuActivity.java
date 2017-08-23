package by.vkus.alexandrzanko.mobile_6vkusov.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.AuthActivities.LoginActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.ProfileActivities.ProfileActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.RestaurantsActivityV2;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Interfaces.IUser;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SessionStoreV2;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexandrzanko on 7/31/17.
 */

public abstract class BaseMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    public final String TAG = this.getClass().getSimpleName();

    private CounterFab fab;

    public CounterFab getFab(){
        return fab;
    }

    public void hideBasket(){
        if(fab != null){
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SingletonV2.currentState().getIUser().getStatus().equals(STATUS.REGISTER)){
            ApiController.getApi().getCountOrderItemsByUser(SingletonV2.currentState().getIUser().getSession()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.code() == 200){
                        Log.i(TAG, "onResponse: " + response.body());
                        fab.setCount(response.body());
                    }else if(response.code() == 302){
                        fab.setCount(0);
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                }
            });

        }else{
            SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
            String variants = store.getStringValueStorage(store.USER_GENERAL_CURRENT_ORDER_VARIANTS);
            if (variants != null){
                try {
                    int count = 0;
                    JSONArray array = new JSONArray(variants);
                    for(int i =0 ; i < array.length(); i++){
                        JSONObject variant  = array.getJSONObject(i);
                        count += variant.getInt("count");
                    }
                    fab.setCount(count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        RelativeLayout clickView = (RelativeLayout)navigationView.getHeaderView(0);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClick();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView userLogo = (CircleImageView) headerView.findViewById(R.id.menu_iv_user);
        TextView userName = (TextView) headerView.findViewById(R.id.menu_name);
        TextView userEmail = (TextView) headerView.findViewById(R.id.menu_email);
        TextView userPoints = (TextView) headerView.findViewById(R.id.menu_points);
        IUser user = SingletonV2.currentState().getIUser();
        if(user.getStatus().equals(STATUS.REGISTER)){
            userEmail.setText(user.getEmail());
            userName.setText(user.getFirstName());
            userPoints.setText(user.getPoints().toString() + " баллов");
            Picasso.with(this)
                    .load(user.getAvatar())
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
            if (!TAG.equals("CategoriesActivityV2")){
                Intent intent = new Intent(this, CategoriesActivityV2.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_free_food) {
            if (!TAG.equals("FreeFoodActivityV2")) {
                Intent intent = new Intent(this, FreeFoodActivityV2.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_our_bonuses) {
            if (!TAG.equals("OurPintsV2")) {
                Intent intent = new Intent(this, OurPintsV2.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_favorites) {
            Intent intent = new Intent(this, RestaurantsActivityV2.class);
            intent.putExtra(RestaurantsActivityV2.EXTRA_FAVORITE, true);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_info) {
            if (!TAG.equals("InfoActivityV2")) {
                Intent intent = new Intent(this, InfoActivityV2.class);
                startActivity(intent);
                this.finish();
            }
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Вы хотите выйдти?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    SessionStoreV2 store = SingletonV2.currentState().getSessionStoreV2();
                    store.clearKeyStorage(store.USER_SESSION);
                    Intent intent = new Intent(BaseMenuActivity.this, MainActivityV2.class);
                    startActivity(intent);
                    BaseMenuActivity.this.finish();
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
                if (fab.getCount() > 0){
                    Intent intent = new Intent(BaseMenuActivity.this, BasketActivity.class);
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

    public void loginButtonClick() {
        STATUS status = SingletonV2.currentState().getIUser().getStatus();
        Intent intent = status == STATUS.GENERAL? new Intent(this, LoginActivityV2.class): new Intent(this,ProfileActivityV2.class);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        this.startActivity(intent);
        this.finish();
    }

}
