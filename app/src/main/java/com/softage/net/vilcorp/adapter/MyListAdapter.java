package com.softage.net.vilcorp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.softage.net.vilcorp.R;


public class MyListAdapter extends BaseAdapter {

    Context context;
    int[] icons;
    String[] icon_name;
    LayoutInflater inflater;

    int selectedPosition = -1;

    public MyListAdapter(Context context, int[] icons, String[] icon_name) {

        this.context = context;
        this.icons = icons;
        this.icon_name = icon_name;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return icon_name[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.nav_listview_item, parent, false);

            holder = new ViewHolder();

            holder.imageView = convertView.findViewById(R.id.list_Image);
            holder.textView = convertView.findViewById(R.id.list_text);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(icons[i]);
        holder.textView.setText(icon_name[i]);

        if (i == selectedPosition) {

            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.vil_red));
            holder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.vil_red));

        } else {

            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.dark_custom));
            holder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.dark_custom));
        }

        return convertView;
    }
}