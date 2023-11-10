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
        request.getSession().setAttribute("employee", userEnteredUsername);
        System.out.println("EmployeeLoginServlet hit, setting employee in session to: " + request.getSession().getAttribute("employee"));

        System.out.println("username: " + userEnteredUsername);
        System.out.println("password: " + userEnteredPassword);

        PrintWriter out = response.getWriter();
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("status", "success");
        out.write(responseJsonObject.toString());
        response.setStatus(200);


    }
}