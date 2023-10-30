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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // The log message can be found in localhost log
        request.getServletContext().log("getting id: " + id);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();



        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            String query = String.format("select m.id, m.title, m.year, m.director, r.rating " +
                    "from movies as m, ratings as r " +
                    "where m.id = r.movieId and m.id = \"%s\"", id);

            String starQuery = String.format("select s.name, s.id " +
                    "from stars as s, stars_in_movies as sim " +
                    "where s.id = sim.starId and sim.movieId = \"%s\" ORDER BY s.name", id);

            String genreQuery = String.format("select g.name " +
                    "from genres as g, genres_in_movies as gim " +
                    "where g.id = gim.genreId and gim.movieId = \"%s\" ORDER BY g.name", id);


            // Declaring statement
            Statement statement = conn.createStatement();

            // Perform the query
            System.out.println(query);
            ResultSet rs = statement.executeQuery(query);

            //JsonArray jsonArray = new JsonArray();

            // Getting column values from rs
            while (rs.next()) {
                String movieTitle = rs.getString("m.title");
                String movieYear = rs.getString("m.year");
                String movieDirector = rs.getString("m.director");
                String movieRating = rs.getString("r.rating");
                String movieId = rs.getString("m.id");
                // Actually creating response[0] with the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_director", movieDirector);
                jsonObject.addProperty("movie_rating", movieRating);
                jsonObject.addProperty("movie_id", movieId);

                // Adding JSON with single movie info to response at index 0
                jsonArray.add(jsonObject);
            }

            rs.close();

//          TEST CODE: Generating JsonArray of test stars to insert into response[1]
//            JsonObject testObj = new JsonObject();
//            JsonArray sampleStars = new JsonArray();
//            sampleStars.add("Star 1");
//            sampleStars.add("Star 2");
//            sampleStars.add("Star 3");

//            testObj.addProperty("stars", sampleStars.toString());
//            request.getServletContext().log(sampleStars.getAsString());
//            jsonArray.add(testObj);
            System.out.println(starQuery);
            rs = statement.executeQuery(starQuery);
            // Initializing starArray, the array of star objs that will
            // eventually be stringified and put in response[1]
            JsonArray starArray = new JsonArray();
            // Looping through stars in result set, converting each to
            // a JsonObject indivStarObj, and then adding each to the
            // starArray described above
            while (rs.next()) {
                String starName = rs.getString("s.name");
                String starId = rs.getString("s.id");

                JsonObject indivStarObject = new JsonObject();
                indivStarObject.addProperty("star_name", starName);
                indivStarObject.addProperty("star_id", starId);
//
                starArray.add(indivStarObject);
            }

            rs.close();

            // Turn the final starArray into a string, + use addProperty
            // to assign it to "stars" attribute in thr final object
            JsonObject starsObj = new JsonObject();
//            starsObj.addProperty("stars", starArray);
//            request.getServletContext().log(sampleStars.getAsString());
            jsonArray.add(starArray);
            System.out.println(genreQuery);
            rs = statement.executeQuery(genreQuery);

            JsonArray genreArray = new JsonArray();
            // Looping through genres in result set and then adding each to the
            // genreArray described above
            while (rs.next()) {
                String genreName = rs.getString("g.name");

                JsonObject indivGenreObject = new JsonObject();
                indivGenreObject.addProperty("genre_name", genreName);
//
                genreArray.add(indivGenreObject);
            }
            // Turn the final starArray into a string, + use addProperty
            // to assign it to "stars" attribute in thr final object
//            starsObj.addProperty("stars", starArray);
//            request.getServletContext().log(sampleStars.getAsString());
            jsonArray.add(genreArray);

            rs.close();
            statement.close();
            conn.close();

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
//        finally {
//            out.close();
//        }

        out.write(jsonArray.toString());
        response.setStatus(200);
        out.close();


        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }

}