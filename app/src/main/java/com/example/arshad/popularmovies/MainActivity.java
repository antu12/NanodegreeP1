package com.example.arshad.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String api_key="c666b3c1cbc279a9c62d496263897871";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    ImageAdapter imageAdapter;
    ArrayList<String> movieList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageAdapter = new ImageAdapter(getApplicationContext(), movieList);

        GridView gridView = (GridView) findViewById(R.id.gridview_movies);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = (String) imageAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMovieList();

    }

    //making the detail available
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovieList(){
        fetchMovieTask movieTask = new fetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sortBy = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popularity));
        movieTask.execute(sortBy,api_key);
    }

    public class fetchMovieTask extends AsyncTask<String, Void, String[]> {



        private final String LOG_TAG = fetchMovieTask.class.getSimpleName();

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null){
                movieList.clear();
                for (String movieStr : result){
                    movieList.add(movieStr);
                }
            }
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getMovieInfoFromJson(String moviesJsonStr) throws JSONException {
            final String TMDB_RESULTS = "results";
//            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_POSTER_PATH = "poster_path";
//            final String TMDB_OVERVIEW = "overview";
//            final String TMDB_VOTE_AVERAGE = "vote_average";
//            final String TMDB_RELEASE_DATE = "release_date";

            // Get the array containing hte movies found
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJson.getJSONArray(TMDB_RESULTS);

//            String title ="";
            String poster = "";
//            String details ="";
//            String rating = "";
//            String date ="";
            // Create array of Movie objects that stores data from the JSON string
            String[] movies = new String[resultsArray.length()];

            for (int i=0;i<resultsArray.length();i++){
                JSONObject movieInfo = resultsArray.getJSONObject(i);

//                title = movieInfo.getString(TMDB_ORIGINAL_TITLE);
                poster = movieInfo.getString(TMDB_POSTER_PATH);
//                details = movieInfo.getString(TMDB_OVERVIEW);
//                rating = movieInfo.getString(TMDB_VOTE_AVERAGE);
//                date = movieInfo.getString(TMDB_RELEASE_DATE);
                if (poster.equals("")){
                    // next
                }else {
                    movies[i] = "https://image.tmdb.org/t/p/w185" + poster;
                }
            }
            for (String s : movies) {
                Log.v(LOG_TAG, "Movie entry: " + s);
            }
            return movies;
        }

        @Override
        protected String[] doInBackground(String... params) {
            //if no movie found
            if (params.length==0){
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String type = "movie";
            String category = "popular";

            try {
                final String movie_url = "https://api.themoviedb.org/3/discover/movie?";
                final String url_apikey = "api_key";
                final String sortBy = "sort_by";

                Uri buildUri = Uri.parse(movie_url).buildUpon()
                        .appendQueryParameter(url_apikey,params[1])
                        .appendQueryParameter(sortBy,params[0])
                        .build();
                URL url = new URL(buildUri.toString());
                Log.v(LOG_TAG, "Built URI " + buildUri.toString());
                // Create the request to ThemovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Json string: "+moviesJsonStr);
            }catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieInfoFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }
    }



}
