package com.example.alexandrzanko.mobile_6vkusov.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexandrzanko.mobile_6vkusov.Activities.Restaurant.ProductActivity;
import com.example.alexandrzanko.mobile_6vkusov.Models.Product;
import com.example.alexandrzanko.mobile_6vkusov.Models.Variant;
import com.example.alexandrzanko.mobile_6vkusov.R;
import com.example.alexandrzanko.mobile_6vkusov.Singleton;
import com.example.alexandrzanko.mobile_6vkusov.Users.Basket;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 01/12/16.
 */

public class ProductsAdapter extends BaseAdapter implements LoadJson {

    private final String BASKET_ADD = "https://6vkusov.by/api/cart_add";

    private ArrayList<Product> listData;
    private LayoutInflater layoutInflater;
    private final String slug;
    private Context context;
    private Basket basket;
    private Singleton singleton;

    public ProductsAdapter(Context context, ArrayList<Product> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.slug = ((ProductActivity)context).getSlug();
        this.singleton = Singleton.currentState();
        this.basket = singleton.getUser().getBasket();
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.product_item_layout, null);
        holder = new ViewHolder();

        holder.productImg           = (ImageView) convertView.findViewById(R.id.product_icon);
        holder.productName          = (TextView) convertView.findViewById(R.id.product_name);
        holder.productDescription   = (TextView) convertView.findViewById(R.id.product_description);
        holder.addToBasketButton    = (Button) convertView.findViewById(R.id.product_add_basket);
        holder.variantsListView     = (ListView) convertView.findViewById(R.id.product_variants);

        convertView.setTag(holder);
        final Product product = listData.get(position);
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());

        final ArrayList<Variant> variants = product.getVariants();
        VariantsAdapter adapter = new VariantsAdapter(this.context, variants, holder.variantsListView ,holder.addToBasketButton);
        holder.variantsListView.setAdapter(adapter);
        float density = context.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams params = holder.variantsListView.getLayoutParams();
        params.height = Math.round(50 * density * variants.size());
        holder.variantsListView.setLayoutParams(params);
        Bitmap img = product.getImg();
        String imgUrl = product.getImageUrl();

        if(img != null){
            holder.productImg.setImageBitmap(img);
        }else if(imgUrl !=  null){
            //new ImageViewDownloadTask(holder.productImg).execute(imgUrl);
        }

        holder.addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (basket.getSlug() == null){
//                    basket.setSlug(slug);
//                    addOrderToBasket(variants, product, holder.variantsListView);
//                }else{
//                    if (basket.getSlug().equals(slug)){
//                        addOrderToBasket(variants, product, holder.variantsListView);
//                    }else{
//                        if (basket.getCountProducts() == 0){
//                            singleton.getStore().getUser().initBasket();
//                            basket = singleton.getStore().getUser().getBasket();
//                            basket.setSlug(slug);
//                            addOrderToBasket(variants, product, holder.variantsListView);
//                            ((ProductActivity) context).updateCountOrdersMenu();
//                        }else {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                            builder.setTitle("Внимание!");
//                            builder.setMessage("При выборе еды из другого ресторана ваша корзина очистится!");
//                            builder.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int arg1) {
//                                    singleton.getStore().getUser().initBasket();
//                                    basket = singleton.getStore().getUser().getBasket();
//                                    basket.setSlug(slug);
//                                    addOrderToBasket(variants, product, holder.variantsListView);
//                                    ((ProductActivity) context).updateCountOrdersMenu();
//                                }
//                            });
//                            builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int arg1) {
//                                }
//                            });
//                            builder.setCancelable(true);
//                            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                                public void onCancel(DialogInterface dialog) {
//                                }
//                            });
//
//                            AlertDialog alert = builder.create();
//                            alert.show();
//                        }
//                    }
//                }
                holder.addToBasketButton.setVisibility(View.GONE);
                ((ProductActivity)context).updateCountOrdersMenu();
            }
        });
        return convertView;
    }

    private void addOrderToBasket(ArrayList<Variant> variants, Product product, ListView listView){
//        JSONArray vars = new JSONArray();
//        for(int i = 0; i < variants.size(); i++){
//            int count = getCountProducts(i, listView);
//            if (count > 0){
//                Variant variant = variants.get(i);
//                JSONObject item = new JSONObject();
//                try {
//                    item.put("id", variant.getId());
//                    item.put("count", count);
//                    vars.put(item);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                basket.addOrder(new Order(product, variants.get(i), count));
//            }
//        }
//        JSONObject params = new JSONObject();
//        try {
//
//            params.put("id", product.getId());
//            params.put("variants", vars);
//            params.put("session", singleton.getStore().getUser().get_session());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        System.out.println(params.toString());
//        new JsonHelperLoad(BASKET_ADD,params,this,null).execute();

    }

    private int getCountProducts(int position, ListView listView){
        int count = 0;
//        View view = listView.getChildAt(position);
//        TextView tv = (TextView) view.findViewById(R.id.product_count);
//        if (tv != null){
//            count = Integer.parseInt(tv.getText().toString());
//            tv.setText("0");
//        }
        return count;
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        System.out.println(obj.toString());
    }

    static class ViewHolder {
        ImageView productImg;
        TextView  productName;
        TextView  productDescription;
        ListView variantsListView;
        Button addToBasketButton;
    }
}
