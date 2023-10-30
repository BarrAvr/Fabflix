import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedbexample,
 * generates output as a html <table>
 */

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "SearchCountServlet", urlPatterns = "/total-movie-count")
public class SearchCountServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Getting session to find total count of movies...");
        HttpSession session = request.getSession();
        System.out.println("Getting list-state...");
        JsonObject listState = (JsonObject) session.getAttribute("listState");

        System.out.println("Writing the query...");
        String query = "";
        String type = listState.get("type").getAsString();
        if(type.equals("genre")){
            System.out.println("genre query...");
            String genre = listState.get("genre").getAsString();
            query = getGenreQueryCount(genre);
            System.out.println("genre query...");
        }else if(type.equals("prefix")){
            System.out.println("prefix query...");
            String prefix = listState.get("prefix").getAsString();
            query = getPrefixQueryCount(prefix);
            System.out.println("prefix query...");
        }else{
            System.out.println("general query...");
            String title = listState.get("title").getAsString();
            String star = listState.get("star").getAsString();
            String year = listState.get("year").getAsString();
            String director = listState.get("director").getAsString();
            query = getGeneralSearchQueryCount(star, year, title, director);
            System.out.println("general query...");
        }


        System.out.println(query);

        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            request.getServletContext().log("query：" + query);
            ResultSet rs = statement.executeQuery(query);

            JsonObject jsonObject = new JsonObject();

            while (rs.next()) {
                String total_movies = rs.getString("total_movies");
                jsonObject.addProperty("total_movies", total_movies);
            }


            // Close all structures
            rs.close();
            statement.close();
            conn.close();
            System.out.println("results:");
            System.out.println(jsonObject.toString());
            out.write(jsonObject.toString());
            response.setStatus(200);


        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
            return;
        }finally {
            out.close();
        }

    }

    private static String getGenreQueryCount(String genre) {
        String query = String.format("select count(*) as total_movies " +
                "from movies as m, genres_in_movies as gim, genres as g " +
                "where g.name = \"%s\" and m.id = gim.movieId and gim.genreId = g.id", genre);
        return query;
    }

    private static String getPrefixQueryCount(String prefix) {
        String query = String.format("select count(*) as total_movies " +
                "from movies as m " +
                "where m.title like \"%s%%\"", prefix);
        return query;
    }

    private static String getGeneralSearchQueryCount(String star, String year, String title, String director) {
        String query ="select count(*) as total_movies from movies as m";
        if((star == null || star.isEmpty()) && (year == null || year.isEmpty())){
            query += " ";
            query += String.format("where m.title like \"%%%s%%\" " +
                    "and m.director like \"%%%s%%\"", title, director);
        }else if(star == null || star.isEmpty()){
            query += " ";
            query += String.format("where m.title like \"%%%s%%\" " +
                    "and m.director like \"%%%s%%\" and m.year = %s", title, director, year);
        }else if(year == null || year.isEmpty()){
            query += ", stars_in_movies as sim, stars as s ";
            query += String.format("where m.title like \"%%%s%%\" " +
                    "and m.director like \"%%%s%%\" " +
                    "and s.name like \"%%%s%%\" " +
                    "and m.id = sim.movieId and sim.starId = s.id", title, director, star);
        }else{
            query += ", stars_in_movies as sim, stars as s ";
            query += String.format("where m.title like \"%%%s%%\" " +
                    "and m.director like \"%%%s%%\" " +
                    "and m.year = %s\" " +
                    "and s.name like \"%%%s%%\" " +
                    "and m.id = sim.movieId and sim.starId = s.id", title, director, year, star);
        }
        return query;
    }
}
