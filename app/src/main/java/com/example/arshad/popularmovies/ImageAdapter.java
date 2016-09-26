package com.example.arshad.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private static class ViewHolder {
        ImageView imageView;
        String imageURL;
        Bitmap bitmap;

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

        viewHolder = (ViewHolder)view.getTag();
        viewHolder.imageURL = imageURLArray.get(position);
        new DownloadAsyncTask().execute(viewHolder);
        return view;
    }


    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            // TODO Auto-generated method stub
            //load image directly
            ViewHolder viewHolder = params[0];
            try {
                URL mimageURL = new URL(viewHolder.imageURL);
                HttpURLConnection connection = (HttpURLConnection) mimageURL.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream input = connection.getInputStream();
                Log.v("get image from: ", mimageURL.toString());
                viewHolder.bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // TODO: handle exception
                Log.e("error", "Downloading Image Failed");
                viewHolder.bitmap = null;
            }
            try {
                return viewHolder;
            } catch (Exception e) {
                Log.e("error", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ViewHolder result) {
            // TODO Auto-generated method stub
            if (result.bitmap == null) {
                result.imageView.setImageResource(R.drawable.postthumb_loading);
            } else {
                result.imageView.setImageBitmap(result.bitmap);
            }
        }



    }



}