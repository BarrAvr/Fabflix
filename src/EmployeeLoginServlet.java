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
            Connection conn = dataSource.getConnection();
            String employeeQuery = "select email, password from employees where email = ?";
            PreparedStatement stat = conn.prepareStatement(employeeQuery);
            stat.setString(1, userEnteredUsername);
            ResultSet rs = stat.executeQuery();

//
//
            int size = 0;
            String email = "", pass = "";
            while (rs.next()) {
                size++;
                email = rs.getString("email");
                pass = rs.getString("password");
            }

            if (size > 0) {
                System.out.println("Found " + email + " " + pass);
                boolean employeeUsernameMatch = userEnteredUsername.equals(email);
                boolean employeePasswordMatch = userEnteredPassword.equals(pass);
                System.out.println("Comparison: " + employeeUsernameMatch + " " + employeePasswordMatch);

                if (employeeUsernameMatch && employeePasswordMatch) {
                    request.getSession().setAttribute("employee", userEnteredUsername);
                    System.out.println("Employee logged in successfully, setting employee in session to: " + request.getSession().getAttribute("employee"));
                    JsonObject responseJsonObject = new JsonObject();
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                    response.getWriter().write(responseJsonObject.toString());
                } else {
                    JsonObject responseJsonObject = new JsonObject();
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "There was an error logging in, please try again.");
                    response.getWriter().write(responseJsonObject.toString());
                }
            } else {
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "There was an error logging in, please try again.");
                response.getWriter().write(responseJsonObject.toString());
            }

            response.setStatus(200);
//            

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", e.getMessage());
            String jsonOutput = responseJsonObject.toString();
            System.out.println("In catch block - Writing " + jsonOutput + " to out");
            response.getWriter().write(responseJsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(200);
        }


    }
}