package edu.uci.ics.fabflixmobile.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.ui.mainpage.MainPageActivity;
import edu.uci.ics.fabflixmobile.ui.movielist.MovieListActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView message;

    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    private final String host = "34.221.232.164";

    private final String port = "8443";
//    private final String domain = "cs122b_project2_login_cart_example_war";
    private final String domain = "team-sravan-project1";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    // Preparing project for demo!
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        setContentView(binding.getRoot());

        username = binding.username;
        password = binding.password;
        message = binding.message;
        final Button loginButton = binding.login;

        //assign a listener to call a function to handle the user request when clicking a button
        loginButton.setOnClickListener(view -> login());
    }

    @SuppressLint("SetTextI18n")
    public void login() {
        message.setText("Trying to login");
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest loginRequest = new StringRequest(
                Request.Method.POST,
//                baseURL + "/api/login",
                baseURL + "/api/android-login",
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
//                    response.toString();
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.get("status").toString().equals("success")){
                            Log.d("login.success", "Login was successful");
                            //Complete and destroy login activity once successful
                            finish();
                            // initialize the activity(page)/destination
                            Intent MovieListPage = new Intent(LoginActivity.this, MainPageActivity.class);
                            // activate the list page.
                            startActivity(MovieListPage);
                        }else{
                            message.setText("Login Failed!");
                            Log.d("login.success", "Login wasn't successful");
//                            finish();
                        }
                    } catch (JSONException e) {
                        Log.d("login.success", response);
                    }
//                    Log.d("login.success", response);
//                    System.out.println("login.success: " + response.toString());
//                    //Complete and destroy login activity once successful
//                    finish();
//                    // initialize the activity(page)/destination
//                    Intent MovieListPage = new Intent(LoginActivity.this, MovieListActivity.class);
//                    // activate the list page.
//                    startActivity(MovieListPage);
                },
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };
        // important: queue.add is where the login request is actually sent
        queue.add(loginRequest);
    }
}
