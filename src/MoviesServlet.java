import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MoviesServlet", urlPatterns = "/api/movies")
public class MoviesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try {

            Connection conn = dataSource.getConnection();
            // Declare our statement
//            Statement statement = conn.createStatement();

            String query = "with top_movies(id, title, year, director, rating) as (select mo.id, mo.title, mo.year, mo.director, r.rating " +
                    "from movies as mo, ratings as r where mo.id = r.movieId order by r.rating desc limit 20) " +
                    "select * from top_movies as m, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
                    "where m.id = sim.movieId and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by m.rating desc, m.title, g.name";
//            String query = "select * from movies as m, ratings as r, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
//                    "where m.id = r.movieId and r.movieId = sim.movieId and sim.starId = s.id and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by r.rating desc";

            PreparedStatement preparedStatement = conn.prepareStatement(query);

            // Perform the query
//            ResultSet rs = statement.executeQuery(query);
            ResultSet rs = preparedStatement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
                String movie_id = rs.getString("m.id");
                String movie_title = rs.getString("m.title");
                String movie_year = rs.getString("m.year");
                String movie_director = rs.getString("m.director");
                String movie_rating = rs.getString("m.rating");
                String star_id = rs.getString("s.id");
                String star_name = rs.getString("s.name");
                String genre_name = rs.getString("g.name");

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("star_id", star_id);
                jsonObject.addProperty("star_name", star_name);
                jsonObject.addProperty("genre_name", genre_name);

                jsonArray.add(jsonObject);
            }
            rs.close();
//            statement.close();
            preparedStatement.close();
            conn.close();

            // Log to localhost log
            request.getServletContext().log("getting " + jsonArray.size() + " results");

            // Write JSON string to output
            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);
            conn.close();

        } catch (Exception e) {

            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }
}
