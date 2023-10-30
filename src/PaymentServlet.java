import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Date;

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
import java.sql.ResultSet;
import java.sql.Statement;


@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {

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
        response.setContentType("application/json");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String id = request.getParameter("id");
        String expiration = request.getParameter("expiration");

        System.out.println(String.format("PaymentServlet received (%s, %s, %s, %s)", firstName, lastName, id, expiration));

        request.getServletContext().log("getting id: " + id);

        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();

        try {

            Connection conn = dataSource.getConnection();
            // Declare our statement
            Statement statement = conn.createStatement();

            String query = String.format("select id, firstName, lastName, expiration from creditcards where id = \"%s\"", id);
//            String query = "select * from movies as m, ratings as r, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
//                    "where m.id = r.movieId and r.movieId = sim.movieId and sim.starId = s.id and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by r.rating desc";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            // The log message can be found in localhost log
            request.getServletContext().log("getting id: " + id);

            int size = 0;
            String dbFirstName = "", dbLastName = "";
            Date dbExpiration = null;
            while (rs.next()) {
                size++;
                dbFirstName = rs.getString("firstName");
                dbLastName = rs.getString("lastName");
                dbExpiration = rs.getDate("expiration");
            }

            if (size > 0) {
                System.out.println("Found " + dbFirstName + " " + dbLastName + " " + dbExpiration.toString());
                boolean firstNameMatch = firstName.equals(dbFirstName);
                boolean lastNameMatch = lastName.equals(dbLastName);
                boolean expirationMatch = expiration.equals(dbExpiration.toString());
                System.out.println("Matches: " + firstNameMatch + " " + lastNameMatch + " " + expirationMatch);
                if (firstNameMatch && lastNameMatch && expirationMatch) {
                    JsonObject responseJsonObject = new JsonObject();
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                    out.write(responseJsonObject.toString());
                } else {
                    JsonObject responseJsonObject = new JsonObject();
                    responseJsonObject.addProperty("status", "failure");
                    responseJsonObject.addProperty("message", "one of the fields did not match");
                    out.write(responseJsonObject.toString());
                }
            } else {
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "failure");
                responseJsonObject.addProperty("message", "id not found");
                out.write(responseJsonObject.toString());
            }

            response.setStatus(200);

            conn.close();

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("errorMessage", e.getMessage());
            String jsonOutput = responseJsonObject.toString();
            System.out.println("In catch block - Writing " + jsonOutput + " to out");
            response.getWriter().write(responseJsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
//        } finally {
//            out.close();
//        }

        }
    }
}

