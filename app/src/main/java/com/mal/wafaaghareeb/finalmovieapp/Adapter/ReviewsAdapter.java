package com.mal.wafaaghareeb.finalmovieapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mal.wafaaghareeb.finalmovieapp.DataModel.Reviews;
import com.mal.wafaaghareeb.finalmovieapp.R;


public class ReviewsAdapter extends ArrayAdapter<Reviews>
{

    public ReviewsAdapter(Context context, Reviews[] objects)
    {
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        final Reviews reviews = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reviewview ,parent , false);
        }


        TextView name = (TextView)convertView.findViewById(R.id.auther);
        name.setText(reviews.getAuthor());

        TextView the_review = (TextView)convertView.findViewById(R.id.review);
        the_review.setText(reviews.getContent());

        return  convertView;
    }
}
