package com.mal.wafaaghareeb.finalmovieapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mal.wafaaghareeb.finalmovieapp.DataModel.Movies;
import com.mal.wafaaghareeb.finalmovieapp.R;
import com.squareup.picasso.Picasso;


public class ImageAdapter extends ArrayAdapter<Movies> {


    public ImageAdapter(Context context, Movies[] objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        Movies movieDataModels = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_content, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_poster);
        String url = "http://image.tmdb.org/t/p/" + "w500" + movieDataModels.getPosterPath();
        Log.d("m", url);
        Picasso.with(getContext()).load(url).into(imageView);

        return  convertView;
    }
}
