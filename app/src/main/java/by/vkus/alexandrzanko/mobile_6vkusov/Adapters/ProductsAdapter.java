package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Activities.Restaurant.ProductActivity;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Product;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.ProductItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Variant;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.Basket;
import by.vkus.alexandrzanko.mobile_6vkusov.Users.STATUS;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by alexandrzanko on 01/12/16.
 */

public class ProductsAdapter extends BaseAdapter{

    private ArrayList<Product> listData;
    private LayoutInflater layoutInflater;
    private final String slug;
    private Context context;
    private Basket basket;
    private SingletonV2 singletonV2;
    private String category;

    public ProductsAdapter(Context context, ArrayList<Product> listData, String category) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.slug = ((ProductActivity)context).getSlug();
        this.singletonV2 = SingletonV2.currentState();
        this.basket = singletonV2.getUser().getBasket();
        this.category = category;
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
        holder.productName.setText(product.get_name());
        holder.productDescription.setText(product.get_description());

        Picasso.with(context)
                    .load(product.get_icon())
                    .placeholder(R.drawable.product) //показываем что-то, пока не загрузится указанная картинка
                    .error(R.drawable.product) // показываем что-то, если не удалось скачать картинку
                    .into(holder.productImg);

        if (category.equals("Еда за баллы")){
            if (SingletonV2.currentState().getUser().getStatus() == STATUS.GENERAL){
                holder.addToBasketButton.setEnabled(false);
                holder.addToBasketButton.setBackgroundResource(R.drawable.shape_corner);
                holder.addToBasketButton.setTextSize(10);
                holder.addToBasketButton.setText("Зарегистрируйтесь");
            }else{
                int points = SingletonV2.currentState().getUser().getPoints();
                if (SingletonV2.currentState().getUser().getBasket().isFreeFoodExist()){
                    holder.addToBasketButton.setEnabled(false);
                    holder.addToBasketButton.setBackgroundResource(R.drawable.shape_corner);
                }
                if (points < product.get_points()){
                    holder.addToBasketButton.setEnabled(false);
                    holder.addToBasketButton.setBackgroundResource(R.drawable.shape_corner);
                    holder.addToBasketButton.setText("Мало баллов");
                }
            }
            holder.productDescription.setText(product.get_points() + " баллов");
            holder.addToBasketButton.setVisibility(View.VISIBLE);
        }else{
            final ArrayList<Variant> variants = product.get_variants();
            VariantsAdapter adapter = new VariantsAdapter(this.context, variants,holder.addToBasketButton);
            if (variants.size() > 1){
                if (isEmptyVariants(variants)){
                    holder.addToBasketButton.setVisibility(View.GONE);
                }else{
                    holder.addToBasketButton.setVisibility(View.VISIBLE);
                }
            }else{
                holder.addToBasketButton.setVisibility(View.VISIBLE);
            }
            holder.variantsListView.setAdapter(adapter);
            float density = context.getResources().getDisplayMetrics().density;
            ViewGroup.LayoutParams params = holder.variantsListView.getLayoutParams();
            params.height = Math.round(60 * density * variants.size());
            holder.variantsListView.setLayoutParams(params);
        }

        holder.addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SingletonV2.currentState().getUser().getStatus() == STATUS.REGISTER){

                    basket.addProductItemRegister(product,slug);
                    if (SingletonV2.currentState().getUser().getBasket().isFreeFoodExist()){
                        notifyDataSetChanged();
                    }

                }else{
                    for(int i =0; i < product.get_variants().size();i++){
                        if ( product.get_variants().get(i).get_count() > 0){
                            ProductItem productItem = new ProductItem(product.get_id(), product.get_name(), product.get_icon(), product.get_description(), product.get_points(), product.get_category(), product.get_variants().get(i));
                            basket.addProductItem(productItem, slug);
                        }
                    }
                    ((ProductActivity)context).updateCountOrdersMenu();
                }

            }
        });

        return convertView;
    }

    private boolean isEmptyVariants(ArrayList<Variant> variants){
        boolean isEmpty = true;
        for(int i = 0; i<variants.size(); i++ ){
            if(variants.get(i).get_count() > 0){
                return false;
            }
        }
        return isEmpty;
    }

    static class ViewHolder {
        ImageView productImg;
        TextView  productName;
        TextView  productDescription;
        ListView variantsListView;
        Button addToBasketButton;
    }
}
