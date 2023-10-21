import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
            // Declare our statement
            Statement statement = conn.createStatement();

            // TODO #1: replace this query with the correct one retrieving email, password
            // from row in customers where email = %s
            String query = "with top_movies(id, title, year, director, rating) as (select mo.id, mo.title, mo.year, mo.director, r.rating " +
                    "from movies as mo, ratings as r where mo.id = r.movieId order by r.rating desc limit 20) " +
                    "select * from top_movies as m, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
                    "where m.id = sim.movieId and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by m.rating desc, m.title, g.name";
//            String query = "select * from movies as m, ratings as r, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
//                    "where m.id = r.movieId and r.movieId = sim.movieId and sim.starId = s.id and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by r.rating desc";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            // TODO #2: remove this line altogether
//            JsonArray jsonArray = new JsonArray();

            // TODO #3: assemble json object with status and message based on what the query returns;
            // login will fail if rs.size() == 0 or rs.size() > 0 but password doesn't match

            JsonObject responseJsonObject = new JsonObject();
            if (username.equals("anteater") && password.equals("123456")) {
                // Login success:

                // TODO #4: set User attribute in session after adding a cart attr
                // set this user into the session
                request.getSession().setAttribute("user", new User(username));

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                // Log to localhost log
                request.getServletContext().log("Login failed");
                // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
                if (!username.equals("anteater")) {
                    responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
                } else {
                    responseJsonObject.addProperty("message", "incorrect password");
                }
            }
            out.write(responseJsonObject.toString());

            // Iterate through each row of rs
//            while (rs.next()) {
//                String movie_id = rs.getString("m.id");
//                String movie_title = rs.getString("m.title");
//                String movie_year = rs.getString("m.year");
//                String movie_director = rs.getString("m.director");
//                String movie_rating = rs.getString("m.rating");
//                String star_id = rs.getString("s.id");
//                String star_name = rs.getString("s.name");
//                String genre_name = rs.getString("g.name");
//
//                // Create a JsonObject based on the data we retrieve from rs
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("movie_id", movie_id);
//                jsonObject.addProperty("movie_title", movie_title);
//                jsonObject.addProperty("movie_year", movie_year);
//                jsonObject.addProperty("movie_director", movie_director);
//                jsonObject.addProperty("movie_rating", movie_rating);
//                jsonObject.addProperty("star_id", star_id);
//                jsonObject.addProperty("star_name", star_name);
//                jsonObject.addProperty("genre_name", genre_name);
//
                // TODO: delete lines 117 and 124; jsonArray no longer exists
//                jsonArray.add(jsonObject);
//            }
//            rs.close();
//            statement.close();
//            conn.close();

            // Log to localhost log
//            request.getServletContext().log("getting " + jsonArray.size() + " results");

            // Write JSON string to output

            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {

            // Write error message JSON object to output
            JsonObject responseJsonObject = new JsonObject();
            if (username.equals("anteater") && password.equals("123456")) {
                // Login success:

                // set this user into the session
                request.getSession().setAttribute("user", new User(username));

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");
            out.write(responseJsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
