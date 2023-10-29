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
import java.util.Objects;

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedbexample,
 * generates output as a html <table>
 */

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "SearchServlet", urlPatterns = "/api/search-results")
public class SearchServlet extends HttpServlet {

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


        String query = "";
        String type = request.getParameter("type");
        String sortBy = request.getParameter("sortBy");
        String titleOrder = request.getParameter("titleOrder");
        String ratingOrder = request.getParameter("ratingOrder");
        String page = request.getParameter("page");
        String count = request.getParameter("count");
        if(type.equals("genre")){
//            displayGenreSearchResult(request, response);
            String genre = request.getParameter("genre");
            String title_order, rating_order, title_first;
            query = getGenreQuery(genre, sortBy, titleOrder, ratingOrder, page, count);
            System.out.println("genre searching...");
        }else if(type.equals("prefix")){
            String prefix = request.getParameter("prefix");
            query = getPrefixQuery(prefix, sortBy, titleOrder, ratingOrder, page, count);
            System.out.println("prefix searching...");
        }else{
//            displayGeneralSearchResult(request, response);
            String title = request.getParameter("title");
            String star = request.getParameter("star");
            String year = request.getParameter("year");
            String director = request.getParameter("director");
            query = getGeneralSearchQuery(star, year, title, director, sortBy, titleOrder, ratingOrder, page, count);
            System.out.println("general searching...");
        }


        System.out.println(query);

        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        System.out.println("searching...");

        try {
            // Create a new connection to database
            Connection conn = dataSource.getConnection();
            System.out.println("connecting...");


            // Declare a new statement
            Statement statement = conn.createStatement();
            System.out.println("statementing...");

//            // Generate a SQL query
//            String query = getGenreQuery(genre);

            // Log to localhost log
            request.getServletContext().log("query：" + query);
            System.out.println("logging...");


            // Perform the query
            System.out.println("querying...");
            System.out.println(query);
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

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


            // Close all structures
            rs.close();
            statement.close();
            conn.close();

            out.write(jsonArray.toString());
            response.setStatus(200);


        } catch (Exception e) {
            /*
             * After you deploy the WAR file through tomcat manager webpage,
             *   there's no console to see the print messages.
             * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
             *
             * To view the last n lines (for example, 100 lines) of messages you can use:
             *   tail -100 catalina.out
             * This can help you debug your program after deploying it on AWS.
             */
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

    private static String getGenreQuery(String genre, String sortBy, String titleOrder, String ratingOrder, String page, String count) {
        String query;
        query = "with top_movies(id, title, year, director, rating) as (" +
                "with selected_movies(id, title, year, director) as ";
        query += String.format("(select m.id, m.title, m.year, m.director " +
                "from movies as m, genres_in_movies as gim, genres as g " +
                "where g.name = \"%s\" and m.id = gim.movieId and gim.genreId = g.id) ", genre);
        query +="select sm.id, sm.title, sm.year, sm.director, r.rating " +
                "from selected_movies as sm, ratings as r where sm.id = r.movieId ";
        query += getOrderAppendage("sm", "r", sortBy, titleOrder, ratingOrder);
        query += " ";
        query += getPaginationAppendage(page, count);
        query +=") ";
        query +="select * from top_movies as m, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
                "where m.id = sim.movieId and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id ";
        query += getOrderAppendage("m", "m", sortBy, titleOrder, ratingOrder);
        query += ", ";
        query +="g.name, s.name";
        return query;
    }

    private static String getOrderAppendage(String titleTableName, String ratingTableName, String sortBy, String titleOrder, String ratingOrder) {
        String query ="order by ";
        if(sortBy.equals("title")){
            query +=titleTableName + ".title ";
            if(titleOrder.equals("desc")){
                query +="desc, ";
            }else{
                query +="asc, ";
            }
            query +=ratingTableName + ".rating ";
            if(ratingOrder.equals("desc")){
                query +="desc";
            }else{
                query +="asc";
            }
        }else{
            query +=ratingTableName + ".rating ";
            if(ratingOrder.equals("desc")){
                query +="desc, ";
            }else{
                query +="asc, ";
            }
            query +=titleTableName + ".title ";
            if(titleOrder.equals("desc")){
                query +="desc";
            }else{
                query +="asc";
            }
        }
        return query;
    }

    private static String getPrefixQuery(String prefix, String sortBy, String titleOrder, String ratingOrder, String page, String count) {
        String query = "with top_movies(id, title, year, director, rating) as ";
        query +=String.format("(select mo.id, mo.title, mo.year, mo.director, r.rating " +
                "from movies as mo, ratings as r " +
                "where mo.id = r.movieId and mo.title like \"%s%%\" ", prefix);
        query +=getOrderAppendage("mo", "r", sortBy, titleOrder, ratingOrder);
        query +=" ";
        query +=getPaginationAppendage(page, count);
        query +=") ";
        query +="select * from top_movies as m, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
                "where m.id = sim.movieId and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id ";
        query +=getOrderAppendage("m", "m", sortBy, titleOrder, ratingOrder);
        query +=", ";
        query +="g.name, s.name";

        return query;
    }

    private static String getGeneralSearchQuery(String star, String year, String title, String director, String sortBy, String titleOrder, String ratingOrder, String page, String count) {
        String query = "with top_movies(id, title, year, director, rating) as " +
                "(with selected_movies(id, title, year, director) as ";
        if((star == null || star.isEmpty()) && (year == null || year.isEmpty())){
            query += String.format("(select m.id, m.title, m.year, m.director " +
                    "from movies as m where m.title like \"%%%s%%\" and m.director like \"%%%s%%\") ", title, director);
        }else if(star == null || star.isEmpty()){
            query += String.format("(select m.id, m.title, m.year, m.director from movies as m " +
                    "where m.title like \"%%%s%%\" and m.director like \"%%%s%%\" and m.year = %s) ", title, director, year);
        }else if(year == null || year.isEmpty()){
            query += String.format("(select m.id, m.title, m.year, m.director from movies as m, stars_in_movies as sim, stars as s " +
                    "where m.title like \"%%%s%%\" and m.director like \"%%%s%%\" and s.name like \"%%%s%%\" " +
                    "and m.id = sim.movieId and sim.starId = s.id) ", title, director, star);
        }else{
            query += String.format("(select m.id, m.title, m.year, m.director " +
                    "from movies as m, stars_in_movies as sim, stars as s " +
                    "where m.title like \"%%%s%%\" and m.director like \"%%%s%%\" " +
                    "and m.year = %s and s.name like \"%%%s%%\" " +
                    "and m.id = sim.movieId and sim.starId = s.id) ", title, director, year, star);
        }
        query +="select sm.id, sm.title, sm.year, sm.director, r.rating " +
                "from selected_movies as sm, ratings as r where sm.id = r.movieId ";
//        query +="order by r.rating desc) ";
        query +=getOrderAppendage("sm", "r", sortBy, titleOrder, ratingOrder);
        query += " ";
        query += getPaginationAppendage(page, count);
        query += ") ";
        query +="select * from top_movies as m, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
                "where m.id = sim.movieId and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id ";
        query +=getOrderAppendage("m", "m", sortBy, titleOrder, ratingOrder);
        query +=", ";
        query +="g.name, s.name";
//        if(sortBy.equals("title")){
//            query +="order by m.title ";
//            if(titleOrder.equals("desc")){
//                query +="desc, ";
//            }else{
//                query +="asc, ";
//            }
//            query +="m.rating ";
//            if(ratingOrder.equals("desc")){
//                query +="desc, ";
//            }else{
//                query +="asc, ";
//            }
//        }else{
//            query +="order by m.rating ";
//            if(ratingOrder.equals("desc")){
//                query +="desc, ";
//            }else{
//                query +="asc, ";
//            }
//            query +="m.title ";
//            if(titleOrder.equals("desc")){
//                query +="desc, ";
//            }else{
//                query +="asc, ";
//            }
//        }
//        query +="g.name, s.name";

        return query;
    }

    private static String getPaginationAppendage(String page, String count){
        int pageNumber = Integer.parseInt(page);
        int movieCount = Integer.parseInt(count);
        int offset = (pageNumber - 1) * movieCount;

        String appendage = "";
        appendage += "limit " + movieCount + " ";
        appendage += "offset " + offset;
        return appendage;
    }
}
