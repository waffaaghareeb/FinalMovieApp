package com.mal.wafaaghareeb.finalmovieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mal.wafaaghareeb.finalmovieapp.Adapter.ImageAdapter;
import com.mal.wafaaghareeb.finalmovieapp.DataModel.Movies;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    Movies[] movieDataModel;
    Gson gson = new Gson();
    GridView gridView;
    ImageAdapter imageAdapter;
    SharedPreferences sharedPreferences;
    String sort_by;

    public static  boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        gridView=(GridView) findViewById(R.id.movie_grid);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue( MovieListActivity.this);
        // Request a string response from the provided  URL.
        StringRequest stringRequest = null;

        String Movie_base_url="http://api.themoviedb.org/3/discover/movie?";
        String SORT_BY = "sort_by";
        String API_PARAM="api_key";
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences( MovieListActivity.this);
        sort_by = sharedPreferences.getString(getString(R.string.pref_sorting_key),
                getString(R.string.pref_sorting_default_value));

        if(!sort_by.equals(getString(R.string.favo_sorting_key))){
            Uri builtUri= Uri.parse(Movie_base_url).buildUpon()
                    .appendQueryParameter(SORT_BY , sort_by)
                    .appendQueryParameter(API_PARAM, "060108cec244efa8e6b05b5ac1cb3194").build();
            String url0= null;
            try {

                url0 = String.valueOf(new URL(String.valueOf(builtUri)));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }


            stringRequest = new StringRequest(Request.Method.GET, url0, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    Log.d("k", response);
                    JSONArray jsonArray = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("results");
                        Log.d("da", jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    movieDataModel = gson.fromJson(jsonArray.toString(), Movies[].class);
                    Log.d("mo", movieDataModel.length + "");
                    imageAdapter = new ImageAdapter( MovieListActivity.this, movieDataModel);
                    gridView.setAdapter(imageAdapter);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            if (mTwoPane) {
                                Bundle arguments = new Bundle();
                                arguments.putString("movieDataModel",gson.toJson(movieDataModel[position]));


                                MovieDetailFragment fragment = new MovieDetailFragment();
                                fragment.setArguments(arguments);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.movie_detail_container, fragment)
                                        .commit();
                            } else {
                                Context context = v.getContext();
                                Intent intent = new Intent(context, MovieDetailActivity.class);

                                intent.putExtra("movieDataModel", gson.toJson(movieDataModel[position]));
                                context.startActivity(intent);
                            }
                        }
                    });

                }
            }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //   Toast.makeText( MovieListActivity.this, "faild", Toast.LENGTH_SHORT).show();
                        }
                    });
// Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

        else
        {
            SharedPreferences sh = MovieListActivity.this.getSharedPreferences("favourate_items", Context.MODE_PRIVATE);
            String arrayOfMovie=sh.getString("FAV_ARRAY", null);
            if (arrayOfMovie!=null) {
                movieDataModel = gson.fromJson(arrayOfMovie, Movies[].class);
                imageAdapter = new ImageAdapter( MovieListActivity.this, movieDataModel);
                gridView.setAdapter(imageAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putString("movieDataModel", gson.toJson(movieDataModel[position]));
                       

                            MovieDetailFragment fragment = new MovieDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.movie_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, MovieDetailActivity.class);

                            intent.putExtra("movieDataModel", gson.toJson(movieDataModel[position]));
                            context.startActivity(intent);
                        }
                    }
                });

            }

            //Toast.makeText( MovieListActivity.this, "waiting fav codee", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
