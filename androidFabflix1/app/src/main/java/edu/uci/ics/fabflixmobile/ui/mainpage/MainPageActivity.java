package edu.uci.ics.fabflixmobile.ui.mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import edu.uci.ics.fabflixmobile.databinding.ActivityMainpageBinding;
import edu.uci.ics.fabflixmobile.ui.movielist.MovieListActivity;

public class MainPageActivity extends AppCompatActivity {
    private EditText textInput;
//    private TextView message;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainpageBinding binding = ActivityMainpageBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        setContentView(binding.getRoot());

        textInput = binding.textInput;
        final Button searchButton = binding.search;

        //assign a listener to call a function to handle the user request when clicking a button
        searchButton.setOnClickListener(view -> search());
    }

    @SuppressLint("SetTextI18n")
    public void search() {
//        message.setText("Trying to Search");
        finish();
        // use the same network queue across our application
        Intent MovieListPage = new Intent(MainPageActivity.this, MovieListActivity.class);
        MovieListPage.putExtra("textInput", textInput.getText().toString());
        // activate the list page.
        startActivity(MovieListPage);
    }
}