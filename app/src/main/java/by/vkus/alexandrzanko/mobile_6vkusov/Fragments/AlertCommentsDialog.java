package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.OrderAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.OrderItem;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.Singleton;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexandrzanko on 5/4/17.
 */

public class AlertCommentsDialog extends DialogFragment {

    private final String TAG = this.getClass().getSimpleName();

    public void setOrder(OrderItem order) {
        this.order = order;
    }

    public void setAdapter(OrderAdapter adapter) {
        this.adapter = adapter;
    }

    private OrderItem order;
    private int like = 1;
    private OrderAdapter adapter;
    private AlertCommentsDialog self = this;

    public AlertCommentsDialog() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] catNamesArray = {"Нравится", "Не нравится"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(16,6,16,6);
        input.setLayoutParams(layoutParams);
        input.setHint("Отзыв");

        builder.setTitle("Оставьте комментарий")
                .setSingleChoiceItems(catNamesArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        like = item == 0 ? 1 : 2;
                    }
                })
                .setView(input)
                .setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        JSONObject params = new JSONObject();
                        String url = getContext().getResources().getString(R.string.api_send_comment);
                        try {
                            params.put("id", order.getId());
                            params.put("session", Singleton.currentState().getUser().getProfile().getString("session"));
                            params.put("type", like);
                            params.put("text", input.getText());
                            new JsonHelperLoad(url,params, self.adapter, null).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        self.adapter.send.setEnabled(true);
                    }
                });

        return builder.create();
    }

}
