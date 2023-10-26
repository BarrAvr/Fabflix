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


        response.setContentType("application/json");
        //response.setContentType("text/html");    // Response mime type

        // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
        String title = request.getParameter("title");
        String star = request.getParameter("star");
        String year = request.getParameter("year");
        String director = request.getParameter("director");

        request.getServletContext().log("getting title: " + title);
        System.out.println("getting title: " + title);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
//        // Building page head with title
//        out.println("<html><head><title>MovieDB: Found Records</title></head>");
//        // Building page body
//        out.println("<body><h1>MovieDB: Found Records</h1>");
        System.out.println("searching...");

        try {
            // Create a new connection to database
            Connection conn = dataSource.getConnection();
            System.out.println("connecting...");


            // Declare a new statement
            Statement statement = conn.createStatement();
            System.out.println("statementing...");

            // Generate a SQL query
            String query = "";
            if(star.isEmpty() && year.isEmpty()){
                query = String.format("SELECT * from movies as m where " +
                        "m.title like \"%%%s%%\" " +
                        "and m.director like \"%%%s%%\"", title, director);
            }else if(star.isEmpty()){
                query = String.format("SELECT * from movies as m where " +
                        "m.title like \"%%%s%%\" " +
                        "and m.director like \"%%%s%%\" " +
                        "and m.year like \"%s\"", title, director, year);
            }else if(year.isEmpty()){
                query = String.format("SELECT * from movies as m, stars as s, stars_in_movies as sim where " +
                        "m.title like \"%%%s%%\" " +
                        "and m.director like \"%%%s%%\" " +
                        "and s.name like \"%s\" " +
                        "and m.id = sim.movieId and sim.starId = s.id", title, director, star);
            }else{
                query = String.format("SELECT * from movies as m, stars as s, stars_in_movies as sim where " +
                        "m.title like \"%%%s%%\" " +
                        "and m.director like \"%%%s%%\" " +
                        "and m.year like \"%%%s%%\" " +
                        "and s.name like \"%s\" " +
                        "and m.id = sim.movieId and sim.starId = s.id", title, director, year, star);
            }
//            String query = "SELECT * from movies";
//            query += " where";
//            query += String.format(" title like \"%%%s%%\" and director like \"%%%s%%\"", title, director);
//            String query = String.format("SELECT * from movies as m, stars as s, stars_in_movies as sim where " +
//                    "m.title like \"%%%s%%\" " +
//                    "and m.director like \"%%%s%%\" " +
//                    "and m.year like \"%%%s%%\" " +
//                    "and s.name like \"%%%s%%\" " +
//                    "and m.id = sim.movieId and sim.starId = s.id", title, director, year, star);


            // Log to localhost log
            request.getServletContext().log("query：" + query);
            System.out.println("logging...");


            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            System.out.println("querying...");
            System.out.println(query);
            // Create a html <table>
//            out.println("<table border>");
//
//            // Iterate through each row of rs and create a table row <tr>
//            out.println("<tr><td>ID</td><td>Name</td></tr>");
//            while (rs.next()) {
////                String m_ID = rs.getString("ID");
////                String m_Name = rs.getString("name");
//                String m_ID = rs.getString("id");
//                String m_Title = rs.getString("title");
//                out.println(String.format("<tr><td>%s</td><td>%s</td></tr>", m_ID, m_Title));
//            }
//            out.println("</table>");

            JsonArray jsonArray = new JsonArray();

            while (rs.next()) {
                String movieID = rs.getString("m.id");
                String movieTitle = rs.getString("m.title");
                String movieYear = rs.getString("m.year");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movieID);
                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
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
//            request.getServletContext().log("Error: ", e);
//
//            // Output Error Massage to html
//            out.println(String.format("<html><head><title>MovieDB: Error</title></head>\n<body><p>SQL error in doGet: %s</p></body></html>", e.getMessage()));
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
}
