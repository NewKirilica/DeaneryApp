package com.example.deaneryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> data;

    public CustomAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_list, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView text = convertView.findViewById(R.id.text);

        String item = data.get(position);
        text.setText(item);

        if (item.contains("❌")) {
            icon.setImageResource(android.R.drawable.ic_delete);
        } else if (item.contains("📘")) {
            icon.setImageResource(android.R.drawable.ic_menu_agenda);
        } else {
            icon.setImageResource(android.R.drawable.ic_menu_info_details);
        }

        return convertView;
    }
}