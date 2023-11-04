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

        PrintWriter out = response.getWriter();

        String userEnteredUsername = request.getParameter("username");
        String userEnteredPassword = request.getParameter("password");
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        System.out.println("================= Received:");
        System.out.println("username: " + userEnteredUsername);
        System.out.println("password: " + userEnteredPassword);
        System.out.println("gRecaptchaResponse: " + gRecaptchaResponse);

        // Verify reCAPTCHA
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
            JsonObject responseJsonObject = new JsonObject();

            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
            out.write(responseJsonObject.toString());
        } catch (Exception e) {
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("errorMessage", e.getMessage());
            String jsonOutput = responseJsonObject.toString();
            System.out.println("In catch block - Writing " + jsonOutput + " to out");
            response.getWriter().write(responseJsonObject.toString());
            out.close();
            return;
        }


//        } catch (Exception e) {
//            // Write error message JSON object to output
//            JsonObject responseJsonObject = new JsonObject();
//            responseJsonObject.addProperty("errorMessage", e.getMessage());
//            String jsonOutput = responseJsonObject.toString();
//            System.out.println("In catch block - Writing " + jsonOutput + " to out");
//            response.getWriter().write(responseJsonObject.toString());
//
//            // Set response status to 500 (Internal Server Error)
//            response.setStatus(500);
//        } finally {
//            out.close();
//        }
        }
    }

