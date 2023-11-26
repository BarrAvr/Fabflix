package edu.uci.ics.fabflixmobile.ui.singlemovie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.databinding.ActivitySinglemovieBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleMovieActivity extends AppCompatActivity {
    private TextView movieTitle;
    private TextView year;
    private TextView director;
    private TextView genres;
    private TextView stars;


    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    private final String host = "34.221.232.164";
//    private final String port = "8080";
    private final String port = "8443";
//    private final String domain = "cs122b_project2_login_cart_example_war";
    private final String domain = "team-sravan-project1";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySinglemovieBinding binding = ActivitySinglemovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        movieTitle = binding.movieTitle;
        year = binding.year;
        director = binding.director;
        genres = binding.genres;
        stars = binding.stars;

        Intent intent = getIntent();
        String movieId = intent.getStringExtra("movieId");
        Log.d("ID:", movieId);
        String url = "/api/single-movie?id="+movieId;
        Log.d("URL:", url);

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest singleMovieSearchRequest = new StringRequest(
                Request.Method.GET,
//                baseURL + "/api/single-movie?id=tt0188378",
                baseURL + url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Log.d("FirstValue", jsonArray.get(0).toString());
                        JSONObject movieJSONObject = jsonArray.getJSONObject(0);
//                        String titleString = "test";
                        String titleString = movieJSONObject.getString("movie_title");
                        String yearString = "Year: " + movieJSONObject.getInt("movie_year");
                        String directorString = "Director: " + movieJSONObject.getString("movie_director");
//                        movieTitle.setText("Test");
                        movieTitle.setText(titleString);
                        year.setText(yearString);
                        director.setText(directorString);
                        String genresString = "Genres: ";
                        String starsString = "Stars: ";
                        JSONArray starsJsonArray = jsonArray.getJSONArray(1);
                        JSONArray genresJsonArray = jsonArray.getJSONArray(2);
                        if(starsJsonArray.length() > 0){
                            starsString += starsJsonArray.getJSONObject(0).getString("star_name");
                        }
                        for(int i = 1; i < starsJsonArray.length(); i++){
                            try {
                                starsString += ", " + starsJsonArray.getJSONObject(i).getString("star_name");
                            } catch (JSONException e) {
                                Log.d("RetrieveSingleMovieStarError", e.toString());
                            }
                        }
                        if(genresJsonArray.length() > 0){
                            genresString += genresJsonArray.getJSONObject(0).getString("genre_name");
                        }
                        for(int i = 1; i < genresJsonArray.length(); i++){
                            try {
                                genresString += ", " + genresJsonArray.getJSONObject(i).getString("genre_name");
                            } catch (JSONException e) {
                                Log.d("RetrieveSingleMovieGenreError", e.toString());
                            }
                        }
                        stars.setText(starsString);
                        genres.setText(genresString);
                    } catch (JSONException e) {
                        Log.d("JSONError", e.toString());
                    }
                },
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                });
        queue.add(singleMovieSearchRequest);
    }
}