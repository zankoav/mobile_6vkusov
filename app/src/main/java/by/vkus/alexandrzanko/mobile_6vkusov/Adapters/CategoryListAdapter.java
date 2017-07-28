package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import by.vkus.alexandrzanko.mobile_6vkusov.Models.MCategory;
import by.vkus.alexandrzanko.mobile_6vkusov.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alexandrzanko on 3/7/17.
 */

public class CategoryListAdapter extends BaseAdapter {

    private final String TAG = this.getClass().getSimpleName();

    private ArrayList<MCategory> listData, listData2;
    private LayoutInflater layoutInflater;
    private Context context;

    public CategoryListAdapter(Context context, List<MCategory> categories) {
        this.listData = getMainCategories(categories);
        this.listData2 = getSecondaryCategories(categories);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }

    private ArrayList<MCategory> getMainCategories(List<MCategory> categories){
        ArrayList<MCategory> mainCategories = new ArrayList<>();
        for (MCategory category : categories) {
            if (category.getType().equals(1)){
                mainCategories.add(category);
            }
        }
        return mainCategories;
    }

    private ArrayList<MCategory> getSecondaryCategories(List<MCategory> categories){
        ArrayList<MCategory> secondaryCategories = new ArrayList<>();
        for (MCategory category : categories) {
            if (category.getType().equals(2)){
                secondaryCategories.add(category);
            }
        }
        return secondaryCategories;
    }


    @Override
    public int getCount() {
        return (listData.size() + listData2.size());
    }

    @Override
    public Object getItem(int position) {
        if(position < listData.size()){
            return listData.get(position);
        }else{
            return listData2.get(position - listData.size());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < listData.size()) {
            ViewHolder holder;
            convertView = layoutInflater.inflate(R.layout.category_type_1, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.name);
            holder.imageView = (ImageView)convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
            MCategory category = listData.get(position);

            holder.titleView.setText(category.getName());
            Picasso.with(context)
                    .load(category.getImage())
                    .placeholder(R.drawable.category) //показываем что-то, пока не загрузится указанная картинка
                    .error(R.drawable.category) // показываем что-то, если не удалось скачать картинку
                    .into(holder.imageView);
        }else{
            ViewHolder2 holder;
            convertView = layoutInflater.inflate(R.layout.category_type_2, null);
            holder = new ViewHolder2();
            holder.titleView = (TextView) convertView.findViewById(R.id.cat2_title);
            convertView.setTag(holder);
            MCategory category = listData2.get(position - listData.size());
            holder.titleView.setText(category.getName());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        ImageView imageView;
    }

    static class ViewHolder2 {
        TextView titleView;
    }
}
