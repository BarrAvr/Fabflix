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

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"
//            String query = "SELECT * from stars as s, stars_in_movies as sim, movies as m " +
//                    "where m.id = sim.movieId and sim.starId = s.id and s.id = ?";

            String query = "select m.title, m.year, m.director, r.rating " +
                    "from movies as m, ratings as r " +
                    "where m.id = r.movieId and m.id = ?";


            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {

                String movieTitle = rs.getString("m.title");
                String movieYear = rs.getString("m.year");
                String movieDirector = rs.getString("m.director");
                String movieRating = rs.getString("r.rating");

//                String movieTitle = rs.getString("movieTitle");
//                String movieYear = rs.getString("movieYear");
//                String movieDirector = rs.getString("movieDirector");
//                String movieRating = rs.getString("movieRating");

                // Create a JsonObject based on the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_director", movieDirector);
                jsonObject.addProperty("movie_rating", movieRating);
//                jsonObject.addProperty("movie_id", movieRating);
//                jsonObject.addProperty("movie_title", movieTitle);
//                jsonObject.addProperty("movie_year", movieYear);
//                jsonObject.addProperty("movie_director", movieDirector);

                jsonArray.add(jsonObject);
            }
            rs.close();
            statement.close();


            // getting genres for single movie:
            String genreQuery = "select.g.name " +
                    "from genres as g, genres_in_movies as gim, movies as m " +
                    "where g.id = gim.genreId and gim.movieId = m.id and m.id = ?";
            PreparedStatement preparedGQ = conn.prepareStatement(genreQuery);
            preparedGQ.setString(1, id);
            ResultSet genreRS = preparedGQ.executeQuery();

            ArrayList<String> genres = new ArrayList<String>();
            while (genreRS.next()) {
                System.out.println("For single movie with id" + id + ", received genre" + genreRS.getString("g.name"));
                genres.add(genreRS.getString("g.name"));
            }
            genreRS.close();
            preparedGQ.close();

            JsonObject jsonObject2 = new JsonObject();
            String genreString = String.join(", ", genres);
            jsonObject2.addProperty("genres", genreString);
            jsonArray.add(jsonObject2);

            // getting stars for single movie:
            // select s.name, s.id from stars as s, stars_in_movies as sim, movies as m where s.id = sim.starId and sim.movieId = m.id and m.id = "tt0395642" limit 3;
            String starQuery = "select.s.name, sid " +
                    "from stars as s, stars_in_movies as sim, movies as m " +
                    "where s.id = sim.starId and sim.movieId = m.id and m.id = ? " +
                    "limit 3";
            PreparedStatement preparedSQ = conn.prepareStatement(starQuery);
            preparedSQ.setString(1, id);
            ResultSet starRS = preparedSQ.executeQuery();

            ArrayList<String> stars = new ArrayList<String>();
            while (starRS.next()) {
                System.out.println("For single movie with id" + id + ", received star" + starRS.getString("s.name"));
                genres.add(genreRS.getString("g.name"));
                stars.add(starRS.getString("s.name"));
            }
            starRS.close();
            preparedSQ.close();

            JsonObject jsonObject3 = new JsonObject();
            String starString = String.join(", ", stars);
            jsonObject3.addProperty("stars", starString);
            jsonArray.add(jsonObject3);

            starRS.close();
            preparedSQ.close();

            // select g.name from genres as g, genres_in_movies as gim, movies as m where g.id = gim.genreId and gim.movieId = m.id and m.id = "tt0498362";

//            String query2 = "select g.name " +
//                    "from genres_in_movies as gm, genres as g " +
//                    "where gm.id = g.id and gm.movieId = ?";
//            PreparedStatement statement2 = conn.prepareStatement(query);
//            statement2.setString(1, id);
//            ResultSet rs2 = statement.executeQuery();
//            jsonArray = new JsonArray();
//            while (rs2.next()) {
//                String movieGenre = rs2.getString("g.name");
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("movie_genre", movieGenre);
//
//                jsonArray.add(jsonObject);

//            }
//            rs2.close();
//            statement2.close();



            // Write JSON string to output
            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }

}
