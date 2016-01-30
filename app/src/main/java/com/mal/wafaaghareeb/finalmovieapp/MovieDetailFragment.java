package com.mal.wafaaghareeb.finalmovieapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mal.wafaaghareeb.finalmovieapp.Adapter.ReviewsAdapter;
import com.mal.wafaaghareeb.finalmovieapp.Adapter.TrailerAdapter;
import com.mal.wafaaghareeb.finalmovieapp.DataModel.Movies;
import com.mal.wafaaghareeb.finalmovieapp.DataModel.Reviews;
import com.mal.wafaaghareeb.finalmovieapp.DataModel.Trailers;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */


    /**
     * The dummy content this fragment is presenting.
     */

    ImageView Small_Poster;
    TextView Poster_name, Year, Rate, Description;
    ListView listViewTrailer, listViewReview;

    TrailerAdapter trailerAdapter;
    ReviewsAdapter reviewsAdapter;

    Movies[] movieDataModelsFavorite;
    Movies movieDataModel;
    Trailers[] tralirs;
    Reviews[] reviews;



    Gson gson = new Gson();
    SharedPreferences sharedPreferences;
    String movieId;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }
String strObj;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("movieDataModel")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
      //      mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
             strObj =   getArguments().getString("movieDataModel");
        }else{
             strObj = getActivity().getIntent().getStringExtra("movieDataModel");
        }
        movieDataModel = gson.fromJson(strObj, Movies.class);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = this.getActivity();
        android.support.v7.widget.Toolbar appBarLayout = (android.support.v7.widget.Toolbar) activity.findViewById(R.id.toolbar);
        if (appBarLayout != null) {
            if (savedInstanceState != null) {


                appBarLayout.setTitle(savedInstanceState.getString("Title"));
            } else {
                appBarLayout.setTitle(movieDataModel.getTitle());
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("Title", movieDataModel.getTitle());

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movie_detail, container, false);


        String strObj2 = getActivity().getIntent().getStringExtra("traliers");
        String strObj3 = getActivity().getIntent().getStringExtra("reviews");


        tralirs = gson.fromJson(strObj2, Trailers[].class);
        reviews = gson.fromJson(strObj3, Reviews[].class);
        movieId = movieDataModel.getId().toString();


//------------------ detail data---------------------------------------------------------------------------

        Poster_name = (TextView) root.findViewById(R.id.title_poster);
        Poster_name.setText(movieDataModel.getOriginalTitle());
        Year = (TextView) root.findViewById(R.id.year);
        Year.setText(movieDataModel.getReleaseDate());

        Rate = (TextView) root.findViewById(R.id.Rate);
        Rate.setText(movieDataModel.getVoteAverage().toString() + " " + "/" + " " + "10");

        Description = (TextView) root.findViewById(R.id.description);
        Description.setText(movieDataModel.getOverview());

        Small_Poster = (ImageView) root.findViewById(R.id.small_poster);
        String url = "http://image.tmdb.org/t/p/" + "w185" + movieDataModel.getPosterPath();
        Picasso.with(getContext()).load(url).into(Small_Poster);
        //----------------------------------Favourite----------------------------------------------


        final Button Favorite = (Button) root.findViewById(R.id.favorite);

        Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("favourate_items", Context.MODE_PRIVATE);
                String favData = sharedPreferences.getString("FAV_ARRAY", null);
                final ArrayList<Movies> movieDataModelAL;
                if (favData != null) {
                    movieDataModelsFavorite = gson.fromJson(favData, Movies[].class);

                    movieDataModelAL = new ArrayList<>(Arrays.asList(movieDataModelsFavorite));
                } else {
                    movieDataModelAL = new ArrayList<Movies>();
                }
                movieDataModelAL.add(movieDataModel);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                movieDataModelsFavorite = movieDataModelAL.toArray(new Movies[movieDataModelAL.size()]);

                editor.putString("FAV_ARRAY", gson.toJson(movieDataModelAL));
                editor.apply();
                Favorite.setText("your favorite film");
                Toast.makeText(getActivity(), sharedPreferences.getString("FAV_ARRAY", "ll"), Toast.LENGTH_SHORT).show();
                Favorite.setEnabled(false);
            }
        });

//------------------------Trailer ---------------------------------------------------------------


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // Request a string response from the provided URL.

        StringRequest stringRequest = null;
        String url1 = null;
        String Trailer_base_url = "http://api.themoviedb.org/3/movie/" + movieId + "/videos";
        String API_PARAM = "api_key";

        Uri builtUri = Uri.parse(Trailer_base_url).buildUpon()
                .appendQueryParameter(API_PARAM, "060108cec244efa8e6b05b5ac1cb3194").build();

        try {

            url1 = String.valueOf(new URL(String.valueOf(builtUri)));

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        listViewTrailer= (ListView) root.findViewById(R.id.list_traliers);

        stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray jsonArray = null;
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    jsonArray = jsonobject.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                tralirs = gson.fromJson(jsonArray.toString(), Trailers[].class);

                trailerAdapter = new TrailerAdapter(getActivity(), tralirs);
                listViewTrailer.setAdapter(trailerAdapter);


                listViewTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.setDataAndType(Uri.parse("http://img.youtube.com/vi/\"+ tralirs.getKey() + \"/0.jpg"), "video/*");

                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + tralirs[position].getKey())));

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        //------------------------Reviews ------------------------------------------------------------------


        RequestQueue queue2 = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        String url2 = null;

        url2 = "http://api.themoviedb.org/3/movie/" + movieDataModel.getId().toString() + "/reviews";
        String api_key2 = "api_key";
        Uri builtUri2 = Uri.parse(url2).buildUpon().appendQueryParameter(api_key2, "060108cec244efa8e6b05b5ac1cb3194")
                .build();
        try {
            url2 = String.valueOf(new URL(String.valueOf(builtUri2)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        listViewReview = (ListView) root.findViewById(R.id.list_reviews);
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray jasonArray = null;
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    jasonArray = jsonobject.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                reviews = gson.fromJson(jasonArray.toString(), Reviews[].class);
                reviewsAdapter = new ReviewsAdapter(getContext(), reviews);

                listViewReview.setAdapter(reviewsAdapter);
                listViewReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue2.add(stringRequest2);





        return root;
    }
}
