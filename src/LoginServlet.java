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
import java.sql.ResultSet;
import java.sql.Statement;


@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

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

        String userEnteredUsername = request.getParameter("username");
        String userEnteredPassword = request.getParameter("password");

        request.getServletContext().log("getting username: " + userEnteredUsername);

        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();

        try {

            Connection conn = dataSource.getConnection();
            // Declare our statement
            Statement statement = conn.createStatement();

            String query = String.format("select email, password from customers where email = \"%s\"", userEnteredUsername);
//            String query = "select * from movies as m, ratings as r, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
//                    "where m.id = r.movieId and r.movieId = sim.movieId and sim.starId = s.id and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by r.rating desc";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            // The log message can be found in localhost log
            request.getServletContext().log("getting username: " + userEnteredUsername);

//            cases:
//            1 - success; 2 - username matched but password didn't; 3 - user not found / nothing matched
            int loginCase = 0;

            int size = 0;
            String email = "", pass = "";
            while (rs.next()) {
                size++;
                email = rs.getString("email");
                pass = rs.getString("password");
            }

            if (size > 0) {
                System.out.println("Found " + email + " " + pass);
                boolean usernameMatch = email.equals(userEnteredUsername);
                boolean passwordMatch = pass.equals(userEnteredPassword);
                System.out.println("Comparison: " + usernameMatch + " " + passwordMatch);
                if (usernameMatch && passwordMatch) {
                    loginCase = 1;
                } else if (usernameMatch && !passwordMatch) {
                    loginCase = 2;
                }
            } else {
                System.out.println("Did not find username in DB");
                loginCase = 3;
            }





            // Output stream to STDOUT
            JsonObject responseJsonObject = new JsonObject();

            if (loginCase == 1) {
                // Login success:


                // set this user into the session
                request.getSession().setAttribute("user", new User(userEnteredUsername));

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                // Log to localhost log
                request.getServletContext().log("Login failed");
                // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
                if (loginCase == 3) {
                    responseJsonObject.addProperty("message", "user " + userEnteredUsername + " does not exist");
                } else {
                    responseJsonObject.addProperty("message", "user " + userEnteredUsername + " found, but password incorrect");
                }
            }
            String jsonOutput = responseJsonObject.toString();
            System.out.println("In try block - Writing " + jsonOutput + " to out");
            response.getWriter().write(responseJsonObject.toString());

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