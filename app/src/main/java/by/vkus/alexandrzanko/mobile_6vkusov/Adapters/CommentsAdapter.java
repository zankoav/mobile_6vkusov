package by.vkus.alexandrzanko.mobile_6vkusov.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import by.vkus.alexandrzanko.mobile_6vkusov.Models.MComment;
import by.vkus.alexandrzanko.mobile_6vkusov.R;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alexandrzanko on 08/12/16.
 */

public class CommentsAdapter extends BaseAdapter {
    private List<MComment> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CommentsAdapter(Context context, List<MComment> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
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
        ViewHolder holder;
        convertView = layoutInflater.inflate(R.layout.comment_item_layout, null);
        holder = new ViewHolder();
        holder.userFirstName     = (TextView) convertView.findViewById(R.id.user_name);
        holder.timeCreateComment = (TextView) convertView.findViewById(R.id.comment_time);
        holder.textComment       = (TextView) convertView.findViewById(R.id.text_comment);
        holder.likesTitle        = (TextView) convertView.findViewById(R.id.likes_title);
        holder.userIcon          = (CircleImageView) convertView.findViewById(R.id.user_icon);
        holder.likeIcon          = (ImageView) convertView.findViewById(R.id.likes_icon);
        convertView.setTag(holder);
        MComment comment = listData.get(position);

        holder.userFirstName.setText(comment.getUser());
        holder.textComment.setText(comment.getText());
        holder.timeCreateComment.setText(comment.getDate_time());
        Integer type = comment.getType();
        Drawable icon = null;
        if(type == 1){
            icon = ContextCompat.getDrawable(context, R.drawable.ic_thumbs_up);
            holder.likesTitle.setText("Советую");
        }else{
            icon = ContextCompat.getDrawable(context, R.drawable.ic_thumb_down);
            holder.likesTitle.setText("Не советую");
        }
        if(icon != null) {
            holder.likeIcon.setImageDrawable(icon);
        }

        Glide.with(context)
                .load(comment.getAvatar())
                .into(holder.userIcon);

//        Picasso.with(context)
//                .load(comment.getAvatar())
//                .placeholder(R.drawable.user) //показываем что-то, пока не загрузится указанная картинка
//                .error(R.drawable.user) // показываем что-то, если не удалось скачать картинку
//                .into(holder.userIcon);

        return convertView;
    }


    static class ViewHolder {
        TextView  userFirstName, timeCreateComment, textComment, likesTitle;
        CircleImageView userIcon;
        ImageView likeIcon;
    }

}