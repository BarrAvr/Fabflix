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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "AddMovieServlet", urlPatterns = "/_dashboard/api/add-movie")
public class AddMovieServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("AddMovieServlet hit");
        response.setContentType("application/json"); // Response mime type

        String title = request.getParameter("title");
        int year = Integer.parseInt(request.getParameter("year"));
        String director = request.getParameter("director");
        String star = request.getParameter("star");
        String genre = request.getParameter("genre");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try {

            Connection conn = dataSource.getConnection();
            CallableStatement call = conn.prepareCall("call add_movie(?, ?, ?, ?, ?)");
            call.setString(1, title);
            call.setInt(2, year);
            call.setString(3, director);
            call.setString(4, star);
            call.setString(5, genre);
            System.out.println("Final call is: " + call);

            call.executeQuery();
            int count = 0;

            while (call.getMoreResults()) {
                count++;
                ResultSet rs = call.getResultSet();
                System.out.println("On ResultSet " + count);
                if (rs.getMetaData().getColumnCount() == 3) {
                    System.out.println("ColumnCount is 3!");
                    while (rs.next()) {
                        String movieId = rs.getString("newmovieid");
                        String starId = rs.getString("newstarid");
                        String genreId = rs.getString("newgenreid");
                        System.out.println(movieId + " " + starId + " " + genreId);
                        JsonObject responseJsonObject = new JsonObject();
                        String message = "Success!";
                        if (movieId != null) {
                            message += "Movie ID: " + movieId + " ";
                        }
                        if (starId != null) {
                            message += "Star ID: " + starId + " ";
                        }
                        if (genreId != null) {
                            message += "Genre ID: " + genreId + " ";
                        }
                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", message);
                        out.write(responseJsonObject.toString());
                    }

                }
            }

            if (count == 0) {
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "duplicate");
                responseJsonObject.addProperty("message", "Duplicated movie was not added");
                out.write(responseJsonObject.toString());
            }

//

            // Perform the query
//            int count = 1;
//            ResultSet rs = call.executeQuery();
//            boolean hasResults = true;
//            String movieId = "", starId = "", genreId = "";
//            while (hasResults || call.getUpdateCount() != -1) {
//                if (hasResults) {
//                    count++;
//                    rs = call.getResultSet();
//                    if (rs.getMetaData().getColumnCount() == 3) {
//                        movieId = rs.getString("newmovieid");
//                        starId = rs.getString("newstarid");
//                        genreId = rs.getString("newgenreid");
//                        System.out.println("On ResultSet " + count + " we have " + movieId + " " + starId + " " + genreId);
//                    }
//
//                    // Move to the next result set
//                    hasResults = call.getMoreResults();
//                } else if (call.getUpdateCount() != -1) {
                    // This handles any additional results (updates or count)
                    // Do something with update count if needed

                    // Move to the next result set
//                    hasResults = call.getMoreResults();
//                }
//            }
//            while (call.getMoreResults()) {
//                rs = call.getResultSet();
//
//                String movieId = "", starId = "", genreId = "";
//                while (rs.next()) {
//                    movieId = rs.getString("newmovieid");
//                    starId = rs.getString("newstarid");
//                    genreId = rs.getString("newgenreid");
//                    System.out.println("On the last ResultSet " + count " we have " + movieId + " " + starId + " " + genreId);
//                    }
//                    JsonObject responseJsonObject = new JsonObject();
//                    responseJsonObject.addProperty("movieid", movieId);
//                    responseJsonObject.addProperty("starid", starId);
//                    responseJsonObject.addProperty("genreid", genreId);
//                    out.write(responseJsonObject.toString());
//                }
//            }




//            while (rs.next()) {
//                String starId = rs.getString("starid");
//                JsonObject responseJsonObject = new JsonObject();
//                responseJsonObject.addProperty("status", "success");
//                responseJsonObject.addProperty("message", "Success! Star ID: " + starId);
//                out.write(responseJsonObject.toString());
//            }

//            while (rs.next()) {
//                String starId = rs.getString("starid");
//                JsonObject responseJsonObject = new JsonObject();
//                responseJsonObject.addProperty("status", "success");
//                responseJsonObject.addProperty("message", "Success! Star ID: " + starId);
//                out.write(responseJsonObject.toString());
//            }
//
            response.setStatus(200);

        } catch (Exception e) {

            // Write error message JSON object to output
            e.printStackTrace();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "fail");
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

    }
}