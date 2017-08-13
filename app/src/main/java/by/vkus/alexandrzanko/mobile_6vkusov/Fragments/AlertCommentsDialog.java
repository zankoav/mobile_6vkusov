package by.vkus.alexandrzanko.mobile_6vkusov.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import by.vkus.alexandrzanko.mobile_6vkusov.Adapters.OrderAdapter;
import by.vkus.alexandrzanko.mobile_6vkusov.ApiController;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.Order.MOrder;
import by.vkus.alexandrzanko.mobile_6vkusov.Models.OrderItem;
import by.vkus.alexandrzanko.mobile_6vkusov.R;
import by.vkus.alexandrzanko.mobile_6vkusov.SingletonV2;
import by.vkus.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader.JsonHelperLoad;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexandrzanko on 5/4/17.
 */

public class AlertCommentsDialog extends DialogFragment {

    private final String TAG = this.getClass().getSimpleName();

    public void setOrder(MOrder order) {
        this.order = order;
    }

    public void setAdapter(OrderAdapter adapter) {
        this.adapter = adapter;
    }

    private MOrder order;
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
                        int orderId =  order.getId();
                        String session = SingletonV2.currentState().getIUser().getSession();
                        int typeComment = like;
                        String message = input.getText().toString();
                        ApiController.getApi().sendCommentByOrder(session,orderId,typeComment,message).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.code() == 200) {
                                    order.setComment_exists(true);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(adapter.context.getActivity(),"Спасибо! Отзыв отправлен на валидацию", Toast.LENGTH_LONG).show();
                                }else if(response.code() == 308){
                                    Toast.makeText(adapter.context.getActivity(),"Ожидает валидации", Toast.LENGTH_LONG).show();
                                }else{
                                    Log.i(TAG, "onResponse: code = " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i(TAG, "onFailure: ");
                            }
                        });
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
