package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.AlertCommentsDialog;
import by.vkus.alexandrzanko.mobile_6vkusov.Fragments.OrderFragment;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.OrderItem;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.OrderItemFood;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.LoadJson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class OrderAdapter extends BaseAdapter  implements LoadJson {

    private ArrayList<OrderItem> listData;
    private LayoutInflater layoutInflater;
    public OrderFragment context;
    private OrderAdapter self = this;
    public Button send;
    public OrderItem current;

    private final String TAG = this.getClass().getSimpleName();


    public OrderAdapter(OrderFragment context, ArrayList<OrderItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context.getContext());
        this.context = context;
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
        final OrderAdapter.ViewHolder holder;

        convertView = layoutInflater.inflate(R.layout.order_item_layout, null);
        holder = new OrderAdapter.ViewHolder();

        holder.restImg = (ImageView) convertView.findViewById(R.id.restaurants_icon);
        holder.orderName = (TextView) convertView.findViewById(R.id.restaurants_name);
        holder.orderDate = (TextView) convertView.findViewById(R.id.order_date);
        holder.orderPrice = (TextView) convertView.findViewById(R.id.price);
        holder.itemsListView = (ListView) convertView.findViewById(R.id.foodListView);
        holder.commentBtn = (Button) convertView.findViewById(R.id.button_comment);

        convertView.setTag(holder);

        final OrderItem item = listData.get(position);

        if (item != null && item.getRestaurant() != null) {
            if (!item.isComment_exists()) {
                holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send = holder.commentBtn;
                        current = item;
                        send.setEnabled(false);
                        FragmentManager manager = context.getActivity().getSupportFragmentManager();
                        AlertCommentsDialog myDialogFragment = new AlertCommentsDialog();
                        myDialogFragment.setAdapter(self);
                        myDialogFragment.setOrder(item);
                        myDialogFragment.show(manager, "dialog");
                    }
                });
            } else {
                holder.commentBtn.setEnabled(false);
                holder.commentBtn.setVisibility(View.GONE);
            }

            holder.orderPrice.setText(item.getTotal_price() + "");
            holder.orderDate.setText(item.getCreated());
            holder.orderName.setText(item.getRestaurant().get_name());

            Picasso.with(context.getContext())
                    .load(item.getRestaurant().get_iconURL())
                    .placeholder(R.drawable.rest_icon) //показываем что-то, пока не загрузится указанная картинка
                    .error(R.drawable.rest_icon) // показываем что-то, если не удалось скачать картинку
                    .into(holder.restImg);


            final ArrayList<OrderItemFood> items = item.getFoods();
            OrderFoodAdapter adapter = new OrderFoodAdapter(this.context.getContext(), items);
            holder.itemsListView.setAdapter(adapter);
            float density = context.getResources().getDisplayMetrics().density;
            ViewGroup.LayoutParams params = holder.itemsListView.getLayoutParams();
            params.height = Math.round(30 * density * items.size());
            holder.itemsListView.setLayoutParams(params);
        }

        return convertView;
    }

    @Override
    public void loadComplete(JSONObject obj, String sessionName) {
        Log.i(TAG, "loadComplete: obj = " + obj.toString());
        if (obj != null){
            String status = null;
            try {
                status = obj.getString("status");
                if (status.equals("successful")){
                    current.setComment_exists(true);
                    this.notifyDataSetChanged();
                }else{
                    send.setEnabled(true);
                }
            } catch (JSONException e) {
                send.setEnabled(true);
                e.printStackTrace();
            }
        }else{
            send.setEnabled(true);
        }
    }

    static class ViewHolder {
        ImageView restImg;
        TextView  orderName, orderDate, orderPrice;
        ListView itemsListView;
        Button commentBtn;
    }
}