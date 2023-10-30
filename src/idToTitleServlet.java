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
@WebServlet(name = "idToTitleServlet", urlPatterns = "/id-to-title")
public class idToTitleServlet extends HttpServlet {

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

        String movie_id = request.getParameter("movie_id");
        String query = String.format("select m.title " +
                "from movies as m " +
                "where m.id = \"%s\"", movie_id);


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
                String movie_title = rs.getString("m.title");
                jsonObject.addProperty("movie_title", movie_title);
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
}
