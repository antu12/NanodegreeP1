package com.example.arshad.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arshad on 9/23/16.
 */

public class ImageAdapter extends BaseAdapter {
    private ArrayList<String> imageURLArray;
    private Context mContext;
    private LayoutInflater inflater;

    public ImageAdapter(Context context, ArrayList<String> imageArray) {
        // TODO Auto-generated constructor stub
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageURLArray = imageArray;
    }

    public static class ViewHolder {
        ImageView imageView;

    }

    @Override
    public int getCount() {
        return imageURLArray.size();
    }

    @Override
    public Object getItem(int position) {
        return imageURLArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        ViewHolder viewHolder;
        if(view == null) {
            view = inflater.inflate(R.layout.grid_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.grid_item_image);
            view.setTag(viewHolder);
        }
        String imageURL = imageURLArray.get(position);
        viewHolder = (ViewHolder)view.getTag();
        Picasso.with(mContext).load(imageURL).into(viewHolder.imageView);
        Log.v("image: ", imageURL);
        return view;
    }
}