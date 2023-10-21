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

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        request.getServletContext().log("getting username: " + username);

        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();

        try {

            Connection conn = dataSource.getConnection();
            // Declare our statement
            Statement statement = conn.createStatement();

            String query = "select email, password from customers where email = \"a@email.com\"";
//            String query = "select * from movies as m, ratings as r, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
//                    "where m.id = r.movieId and r.movieId = sim.movieId and sim.starId = s.id and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by r.rating desc";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            // The log message can be found in localhost log
            request.getServletContext().log("getting username: " + username);

            int size = 0;
            String email = "", pass = "";
            while (rs.next()) {
                email = rs.getString("email");
                pass = rs.getString("password");
                System.out.println("Found " + email + " " + pass);
            }

            boolean usernameMatch = email.equals("a@email.com");
            boolean passwordMatch = pass.equals("a2");

            System.out.println("Comparison: " + usernameMatch + " " + passwordMatch);
            // Output stream to STDOUT
            JsonObject responseJsonObject = new JsonObject();

            if (usernameMatch && passwordMatch) {
                // Login success:


                // set this user into the session
                request.getSession().setAttribute("user", new User(username));

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                // Log to localhost log
                request.getServletContext().log("Login failed");
                // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
                if (!username.equals("anteater")) {
                    responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
                } else {
                    responseJsonObject.addProperty("message", "incorrect password");
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
