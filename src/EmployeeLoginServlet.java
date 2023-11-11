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
import java.sql.ResultSet;
import java.sql.Statement;


@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/_dashboard/api/employee-login")
public class EmployeeLoginServlet extends HttpServlet {

    private DataSource dataSource;

    public void init(ServletConfig config) {
        System.out.println("EmployeeLoginServlet was hit, in init()");
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

        try {

//            Connection conn = dataSource.getConnection();
//            // Declare our statement
//            Statement statement = conn.createStatement();
//
//            String query = String.format("select email, password, id from customers where email = \"%s\"", userEnteredUsername);
////            String query = "select * from movies as m, ratings as r, stars_in_movies as sim, stars as s, genres_in_movies as gim, genres as g " +
////                    "where m.id = r.movieId and r.movieId = sim.movieId and sim.starId = s.id and sim.starId = s.id and gim.movieId = m.id and gim.genreId = g.id order by r.rating desc";
//
//            // Perform the query
//            ResultSet rs = statement.executeQuery(query);
//            // The log message can be found in localhost log
//            request.getServletContext().log("getting username: " + userEnteredUsername);
//
////            cases:
////            1 - success; 2 - username matched but password didn't; 3 - user not found / nothing matched
//            int loginCase = 0;
//
//            int size = 0;
//            String email = "", pass = "", id = "";
//            while (rs.next()) {
//                size++;
//                email = rs.getString("email");
//                pass = rs.getString("password");
//                id = rs.getString("id");
//            }
//
//            if (size > 0) {
//                System.out.println("Found " + email + " " + pass);
//                boolean usernameMatch = email.equals(userEnteredUsername);
//                boolean passwordMatch = pass.equals(userEnteredPassword);
//                passwordMatch = VerifyPassword.verifyCredentials(userEnteredUsername, userEnteredPassword);
//                System.out.println("Comparison: " + usernameMatch + " " + passwordMatch);
//                if (usernameMatch && passwordMatch) {
//                    loginCase = 1;
//                } else if (usernameMatch && !passwordMatch) {
//                    loginCase = 2;
//                }
//            } else {
//                System.out.println("Did not find username in DB");
//                loginCase = 3;
//            }

            boolean employeeUsernameMatch = userEnteredUsername.equals("classta@email.edu");
            boolean employeePasswordMatch = userEnteredPassword.equals("classta");


            if (employeeUsernameMatch && employeePasswordMatch) {

                request.getSession().setAttribute("employee", userEnteredUsername);
                System.out.println("EmployeeLoginServlet hit, setting employee in session to: " + request.getSession().getAttribute("employee"));

                System.out.println("username: " + userEnteredUsername);
                System.out.println("password: " + userEnteredPassword);

                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "success");
                response.getWriter().write(responseJsonObject.toString());
            } else {
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "There was an error logging in with the provided credetials. Please try again.");
                response.getWriter().write(responseJsonObject.toString());
            }
            response.setStatus(200);

        } catch (Exception e) {

        }


    }
}