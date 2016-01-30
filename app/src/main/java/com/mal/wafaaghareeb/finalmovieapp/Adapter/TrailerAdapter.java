package com.mal.wafaaghareeb.finalmovieapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mal.wafaaghareeb.finalmovieapp.DataModel.Trailers;
import com.mal.wafaaghareeb.finalmovieapp.R;
import com.squareup.picasso.Picasso;


public class TrailerAdapter extends ArrayAdapter<Trailers>
{

    public TrailerAdapter(Context context,  Trailers[] objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        final Trailers tralirs = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tralierview ,parent , false);
        }
        String url = "http://img.youtube.com/vi/"+ tralirs.getKey() + "/0.jpg";

        ImageView imageView = (ImageView)convertView.findViewById(R.id.tralier_image);
        TextView name = (TextView)convertView.findViewById(R.id.TralierTitle);

        Picasso.with(getContext()).load(url).into(imageView);
        name.setText(tralirs.getName());




        return  convertView;
    }
}
