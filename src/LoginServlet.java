import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

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
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        System.out.println("username: " + userEnteredUsername);
        System.out.println("password: " + userEnteredPassword);
        System.out.println("gRecaptchaResponse:" + gRecaptchaResponse);

        PrintWriter out = response.getWriter();

//        try {

//        }
//        catch (Exception e) {
//            JsonObject responseJsonObject = new JsonObject();
//            System.out.println("Entered catch block due to Recaptcha errror: " + e.getMessage());
//            responseJsonObject.addProperty("status", "fail");
//            responseJsonObject.addProperty("message", "Login failed - Recaptcha verification error: " + e.getMessage());
//            out.write(responseJsonObject.toString());
//
//            out.close();
//            return;
//        }


        request.getServletContext().log("getting username: " + userEnteredUsername);

//        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();

        try {

            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
            JsonObject responseJsonObject = new JsonObject();
//            responseJsonObject.addProperty("status", "success");
//            responseJsonObject.addProperty("message", "success");
//            out.write(responseJsonObject.toString());
//            System.out.println("success - Writing " + responseJsonObject.toString() + " to out");
//            out.close();

            Connection conn = dataSource.getConnection();
            // Declare our statement
//            Statement statement = conn.createStatement();

            String query = String.format("select email, password, id from customers where email = \"%s\"", userEnteredUsername);
//            String query = "select * from movies as m, ratings as r, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
//                    "where m.id = r.movieId and r.movieId = sim.movieId and sim.starId = s.id and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by r.rating desc";

            PreparedStatement preparedStatement = conn.prepareStatement(query);

            // Perform the query
//            ResultSet rs = statement.executeQuery(query);
            ResultSet rs = preparedStatement.executeQuery();
            // The log message can be found in localhost log
            request.getServletContext().log("getting username: " + userEnteredUsername);

//            cases:
//            1 - success; 2 - username matched but password didn't; 3 - user not found / nothing matched
            int loginCase = 0;

            int size = 0;
            String email = "", pass = "", id = "";
            while (rs.next()) {
                size++;
                email = rs.getString("email");
                pass = rs.getString("password");
                id = rs.getString("id");
            }

            if (size > 0) {
                System.out.println("Found " + email + " " + pass);
                boolean usernameMatch = email.equals(userEnteredUsername);
                boolean passwordMatch = VerifyPassword.verifyCredentials(userEnteredUsername, userEnteredPassword);
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
//            JsonObject responseJsonObject = new JsonObject();

            if (loginCase == 1) {
                // Login success:


                // set this user into the session
                request.getSession().setAttribute("user", new User(userEnteredUsername));
                request.getSession().setAttribute("id", id);
                request.getSession().setAttribute("email", email);

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");
                response.getWriter().write(responseJsonObject.toString());
                String jsonOutput = responseJsonObject.toString();
                System.out.println("In try block - Writing " + jsonOutput + " to out");

                response.setStatus(200);
//                responseJsonObject.addProperty("email, id", email + " " + id);

            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                // Log to localhost log
                request.getServletContext().log("Login failed");
                // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
                if (loginCase == 3) {
//                    responseJsonObject.addProperty("message", "user " + userEnteredUsername + " does not exist");
                    responseJsonObject.addProperty("message", "Login failed: either your username or password was incorrect");
                    response.getWriter().write(responseJsonObject.toString());
                    String jsonOutput = responseJsonObject.toString();
                    System.out.println("In try block - Writing " + jsonOutput + " to out");

                    response.setStatus(200);
                } else {
//                    responseJsonObject.addProperty("message", "user " + userEnteredUsername + " found, but password incorrect");
                    responseJsonObject.addProperty("message", "Login failed: either your username or password was incorrect");
                    response.getWriter().write(responseJsonObject.toString());
                    String jsonOutput = responseJsonObject.toString();
                    System.out.println("In try block - Writing " + jsonOutput + " to out");

                    response.setStatus(200);
                }
            }


            rs.close();
            preparedStatement.close();
            conn.close();

        } catch (Exception e) {
            // Write error message JSON object to output
            e.printStackTrace();
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", e.getMessage());
            String jsonOutput = responseJsonObject.toString();
            System.out.println("In catch block - Writing " + jsonOutput + " to out");
            response.getWriter().write(responseJsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(200);
//        } finally {
//            out.close();
//        }

        }
    }
}