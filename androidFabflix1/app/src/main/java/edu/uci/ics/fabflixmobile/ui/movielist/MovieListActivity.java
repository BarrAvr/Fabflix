package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;
import edu.uci.ics.fabflixmobile.databinding.ActivityMovielistBinding;
import edu.uci.ics.fabflixmobile.databinding.MovielistRowBinding;
import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import edu.uci.ics.fabflixmobile.ui.mainpage.MainPageActivity;
import edu.uci.ics.fabflixmobile.ui.singlemovie.SingleMovieActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieListActivity extends AppCompatActivity {

    private final String host = "34.221.232.164";
    //    private final String port = "8080";
    private final String port = "8443";
    //    private final String domain = "cs122b_project2_login_cart_example_war";
    private final String domain = "team-sravan-project1";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;
    private final String type = "general";
    private final String star = "";
    private final String year = "";
    private final String director = "";
    private final String sortBy = "title";
    private final String titleOrder = "asc";
    private final String ratingOrder = "desc";
    private final int pageCount = 10;
    private String textInput = "";

    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovielistBinding binding = ActivityMovielistBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_movielist);
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        textInput = intent.getStringExtra("textInput");
        Log.d("Text Input:", textInput);

        final Button nextButton = binding.next;
        nextButton.setOnClickListener(view -> next());
        final Button prevButton = binding.prev;
        prevButton.setOnClickListener(view -> previous());

        updateListState();
//        displayMovies();

//        String url = "/api/search-results?type=general&title="+textInput+"&star=&year=&director=&sortBy=title&titleOrder=asc&ratingOrder=desc&page="+page+"&count=10";
//        Log.d("URL:", url);
//
//        // TODO: this should be retrieved from the backend server
//        final ArrayList<Movie> movies = new ArrayList<>();
//
//        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
//        // request type is POST
//        final StringRequest searchRequest = new StringRequest(
//                Request.Method.GET,
//                baseURL + url,
////                baseURL + "/api/search-results?type=general&title=test&star=&year=&director=&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=10",
////                baseURL + "/api/search-results?type=genre&genre=Comedy&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=10",
//                response -> {
//                    try {
//                        JSONArray jsonArray = new JSONArray(response);
//                        Log.d("FirstValue", jsonArray.get(0).toString());
////                        insertJsonArrayToMovieArray(movies, jsonArray);
//                        for(int index = 0; index < jsonArray.length(); index++){
//                            try {
//                                JSONObject movieJSONObject = jsonArray.getJSONObject(index);
//                                String id = movieJSONObject.getString("movie_id");
//                                String title = movieJSONObject.get("movie_title").toString();
//                                short year = (short) movieJSONObject.getInt("movie_year");
//                                String director = movieJSONObject.getString("movie_director");
//                                String[] genres = {"", "", ""};
//                                int numGenres = 0;
//                                int maxGenres = 3;
//                                String[] stars = {"", "", ""};
//                                int numStars = 0;
//                                int maxStars = 3;
//                                genres[0] = movieJSONObject.getString("genre_name");
//                                numGenres++;
//                                while(index < jsonArray.length() && title.equals(movieJSONObject.get("movie_title").toString())){
//                                    movieJSONObject = jsonArray.getJSONObject(index);
//                                    Log.d("Entry " + index, "Title: " + movieJSONObject.get("movie_title").toString());
//                                    String curGenre = movieJSONObject.getString("genre_name");
//                                    if(numStars < maxStars && curGenre.equals(genres[0])){
//                                        stars[numStars] = movieJSONObject.getString("star_name");
//                                        numStars++;
//                                    }else{
//                                        if(numGenres < maxGenres && !curGenre.equals(genres[numGenres-1])){
//                                            genres[numGenres] = curGenre;
//                                            numGenres++;
//                                        }
//                                    }
//                                    index++;
//                                }
//                                if(!title.equals(movieJSONObject.get("movie_title").toString())){
//                                    index--;
//                                }
//                                movies.add(new Movie(id, title, year, director, genres, stars));
//                            } catch (JSONException e) {
//                                Log.d("RetrieveMovieError", e.toString());
//                            }
//                        }
//                    } catch (JSONException e) {
//                        Log.d("login.success", response);
//                    }
//
//                    MovieListViewAdapter adapter = new MovieListViewAdapter(this, movies);
//                    ListView listView = findViewById(R.id.list);
//                    listView.setAdapter(adapter);
//                    listView.setOnItemClickListener((parent, view, position, id) -> {
//                        Movie movie = movies.get(position);
//                        @SuppressLint("DefaultLocale") String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                        Log.d("OnClick", "Clicked on id:" + movie.getID() + " title:" + movie.getName());
//                        finish();
//                        Intent SingleMoviePage = new Intent(MovieListActivity.this, SingleMovieActivity.class);
//                        SingleMoviePage.putExtra("movieId", movie.getID());
//                        startActivity(SingleMoviePage);
//                    });
//                },
//                error -> {
//                    // error
//                    Log.d("login.error", error.toString());
//                });
//        // important: queue.add is where the login request is actually sent
//        queue.add(searchRequest);
    }

    private void next(){
        Log.d("Next", "next was pressed");
//        page++;
//        displayMovies();

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest totalMovieCountRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/total-movie-count",
                response -> {
//                    Log.d("Next", "Page =" + page);
                    page++;
//                    Log.d("Next", "Page =" + page);
                    int totalMoviesRequested = page * pageCount;
                    try {
//                        Log.d("Next", "Getting response jsonObject");
                        JSONObject jsonObject = new JSONObject(response);
//                        Log.d("Next", "jsonObject: " + jsonObject.toString());
                        String totalMoviesString = jsonObject.getString("total_movies");
//                        Log.d("Next", "totalMoviesString: " + totalMoviesString);
                        int totalMovies = Integer.parseInt(totalMoviesString);
//                        Log.d("Next", "totalMovies: " + totalMovies);
                        if(totalMovies > totalMoviesRequested){
//                            Log.d("Next", "displaying movies");
                            updateListState();
//                            displayMovies();
                        }else{
                            page--;
                            Log.d("Next", "reached last page");
                        }
                    } catch (JSONException e) {
                        Log.d("RetrieveMovieCountError", e.toString());
                    }
                },
                error -> {
                    Log.d("moviesDisplayError", error.toString());
                }
        );
        queue.add(totalMovieCountRequest);
    }
    private void previous(){
        Log.d("Prev", "previous was pressed");
//        page++;
//        displayMovies();
        if(page > 1){
            page--;
            updateListState();
//            displayMovies();
        }
    }

    private void updateListState(){
        Log.d("ListState:", "Updating list state");
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest updateListState = new StringRequest(
                Request.Method.POST,
                baseURL + "/list-state",
                response -> {
                    displayMovies();
                },
                error -> {
                    // error
                    Log.d("moviesDisplayError", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("type", type);
                params.put("title", textInput);
                params.put("star", star);
                params.put("year", year);
                params.put("director", director);
                params.put("sortBy", sortBy);
                params.put("titleOrder", titleOrder);
                params.put("ratingOrder", ratingOrder);
                params.put("page", String.valueOf(page));
                params.put("count", String.valueOf(pageCount));
                return params;
            }
        };
        // important: queue.add is where the login request is actually sent
        queue.add(updateListState);
    }

    private void displayMovies() {
        String encodedText = textInput;
        try {
            encodedText = URLEncoder.encode(textInput, "UTF-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Log.d("encoding error", "encoding error");
        }
        String url = "/api/search-results?type=general&title="+encodedText+"&star=&year=&director=&sortBy=title&titleOrder=asc&ratingOrder=desc&page="+page+"&count="+pageCount;
        Log.d("URL:", url);

        final ArrayList<Movie> movies = new ArrayList<>();

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + url,
//                baseURL + "/api/search-results?type=general&title=test&star=&year=&director=&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=10",
//                baseURL + "/api/search-results?type=genre&genre=Comedy&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=10",
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Log.d("FirstValue", jsonArray.get(0).toString());
//                        insertJsonArrayToMovieArray(movies, jsonArray);
                        for(int index = 0; index < jsonArray.length(); index++){
                            try {
                                JSONObject movieJSONObject = jsonArray.getJSONObject(index);
                                String id = movieJSONObject.getString("movie_id");
                                String title = movieJSONObject.get("movie_title").toString();
                                short year = (short) movieJSONObject.getInt("movie_year");
                                String director = movieJSONObject.getString("movie_director");
                                String[] genres = {"", "", ""};
                                int numGenres = 0;
                                int maxGenres = 3;
                                String[] stars = {"", "", ""};
                                int numStars = 0;
                                int maxStars = 3;
                                genres[0] = movieJSONObject.getString("genre_name");
                                numGenres++;
                                while(index < jsonArray.length() && title.equals(movieJSONObject.get("movie_title").toString())){
                                    movieJSONObject = jsonArray.getJSONObject(index);
                                    Log.d("Entry " + index, "Title: " + movieJSONObject.get("movie_title").toString());
                                    String curGenre = movieJSONObject.getString("genre_name");
                                    if(numStars < maxStars && curGenre.equals(genres[0])){
                                        stars[numStars] = movieJSONObject.getString("star_name");
                                        numStars++;
                                    }else{
                                        if(numGenres < maxGenres && !curGenre.equals(genres[numGenres-1])){
                                            genres[numGenres] = curGenre;
                                            numGenres++;
                                        }
                                    }
                                    index++;
                                }
                                if(!title.equals(movieJSONObject.get("movie_title").toString())){
                                    index--;
                                }
                                movies.add(new Movie(id, title, year, director, genres, stars));
                            } catch (JSONException e) {
                                Log.d("RetrieveMovieError", e.toString());
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("login.success", response);
                    }

                    MovieListViewAdapter adapter = new MovieListViewAdapter(this, movies);
                    ListView listView = findViewById(R.id.list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        Movie movie = movies.get(position);
                        @SuppressLint("DefaultLocale") String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Log.d("OnClick", "Clicked on id:" + movie.getID() + " title:" + movie.getName());
                        finish();
                        Intent SingleMoviePage = new Intent(MovieListActivity.this, SingleMovieActivity.class);
                        SingleMoviePage.putExtra("movieId", movie.getID());
                        startActivity(SingleMoviePage);
                    });
                },
                error -> {
                    // error
                    Log.d("moviesDisplayError", error.toString());
                });
        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);
    }
}