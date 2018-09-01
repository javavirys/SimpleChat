package com.example.javavirys.socketiochat.list;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.javavirys.socketiochat.R;
import com.example.javavirys.socketiochat.list.models.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesAdapter extends BaseAdapter {

    private Context context;
    private List<Message> array = new ArrayList<>();

    @BindView(R.id.item_card) CardView card;
    @BindView(R.id.item_name) TextView name;
    @BindView(R.id.item_msg)  TextView msg;
    @BindView(R.id.item_time)  TextView time;

    public MessagesAdapter(Context context){
        this.context = context;
    }

    public void add(Message e){
        array.add(e);
    }

    public void addAll(Collection<? extends Message> collection){
        array.addAll(collection);
        notifyDataSetChanged();
    }

    public void clear(){
        array.clear();
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Message getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Message> getArray() {
        return array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.list_item,null,false);
        ButterKnife.bind(this,view);

        Message message = getItem(position);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = message.getGravity();
        card.setLayoutParams(params);

        name.setText(message.getName());
        msg.setText(message.getMsg());
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy hh:mm", Locale.getDefault());
        time.setText(format.format(new Date(message.getTime())));
        return view;
    }
}
