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
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;


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
            String query = "select id, firstName, lastName, expiration from creditcards where id = ?");
            PreparedStatement statement = conn.prepareStatement(query);


//            String query = "select * from movies as m, ratings as r, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
//                    "where m.id = r.movieId and r.movieId = sim.movieId and sim.starId = s.id and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by r.rating desc";

            // Perform the query
            ResultSet rs = statement.executeQuery();
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
            rs.close();
            statement.close();

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

//                    String insertQuery = String
//                    INSERT INTO creditcards VALUES('490001', 'James', 'Brown', '2007/09/20');
//                    INSERT INTO creditcards VALUES('490002', 'Margaret', 'Black', '2006/05/20');
//                    INSERT INTO creditcards VALUES('490003', 'Keith', 'Black', '2006/06/25');
//                    INSERT INTO creditcards VALUES('490004', 'Kyle', 'Shaw', '2008/04/21');

                    HttpSession session = request.getSession();
                    ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
                    String customerId = (String) session.getAttribute("id");
                    for (String movieId : previousItems) {
                        String insertQuery = String.format("INSERT INTO sales (customerId, movieId, saleDate) VALUES (%s, '%s', CURDATE())", customerId, movieId);
                        PreparedStatement statement2 = conn.prepareStatement(insertQuery);
                        System.out.println(insertQuery);
                        statement2.executeUpdate();
                        statement2.close();
                        System.out.println("Query executed");
                    }

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

