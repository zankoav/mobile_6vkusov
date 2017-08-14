package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.support.v4.app.FragmentManager;

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
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Order.MOrder;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Order.MOrderFood;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.Validation;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by alexandrzanko on 5/2/17.
 */

public class OrderAdapter extends BaseAdapter{

    private List<MOrder> listData;
    private LayoutInflater layoutInflater;
    public OrderFragment context;
    private OrderAdapter self = this;
    public Button send;
    public MOrder current;

    private final String TAG = this.getClass().getSimpleName();


    public OrderAdapter(OrderFragment context, List<MOrder> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context.getContext());
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public MOrder getItem(int position) {
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

        final MOrder item = listData.get(position);

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
        String totalPrice = Validation.twoNumbersAfterAfterPoint(item.getTotal_price());
        holder.orderPrice.setText(totalPrice);
        String date = SingletonV2.currentState().getDate_time(item.getCreated());
        holder.orderDate.setText(date);
        holder.orderName.setText(item.getRestaurant_name());

        Glide.with(context.getContext())
                .load(item.getRestaurant_icon())
                .placeholder(R.drawable.rest_icon)
                .error(R.drawable.rest_icon)
                .into(holder.restImg);

        final List<MOrderFood> items = item.getFood();
        OrderFoodAdapter adapter = new OrderFoodAdapter(this.context.getContext(), items);
        holder.itemsListView.setAdapter(adapter);
        float density = context.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams params = holder.itemsListView.getLayoutParams();
        params.height = Math.round(30 * density * items.size());
        holder.itemsListView.setLayoutParams(params);
        return convertView;
    }

//    @Override
//    public void loadComplete(JSONObject obj, String sessionName) {
//        Log.i(TAG, "loadComplete: obj = " + obj.toString());
//        if (obj != null){
//            String status = null;
//            try {
//                status = obj.getString("status");
//                if (status.equals("successful")){
//                    current.setComment_exists(true);
//                    this.notifyDataSetChanged();
//                }else{
//                    send.setEnabled(true);
//                }
//            } catch (JSONException e) {
//                send.setEnabled(true);
//                e.printStackTrace();
//            }
//        }else{
//            send.setEnabled(true);
//        }
//    }

    static class ViewHolder {
        ImageView restImg;
        TextView  orderName, orderDate, orderPrice;
        ListView itemsListView;
        Button commentBtn;
    }
}