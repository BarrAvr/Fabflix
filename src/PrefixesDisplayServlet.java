import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


@WebServlet(name = "PrefixesDisplayServlet", urlPatterns = "/api/prefixes")
public class PrefixesDisplayServlet extends HttpServlet {

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
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
//            Statement statement = conn.createStatement();

            String query = "select substring(movies.title, 1, 1) as prefix from movies group by prefix order by prefix";

            PreparedStatement preparedStatement = conn.prepareStatement(query);


//            ResultSet rs = statement.executeQuery(query);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(query);
            JsonArray jsonArray = new JsonArray();
            while (rs.next()) {
                String prefix = rs.getString("prefix");
                System.out.println(prefix);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("prefix", prefix);
                jsonArray.add(jsonObject);
            }


            // Close all structures
            rs.close();
//            statement.close();
            preparedStatement.close();
            conn.close();

            out.write(jsonArray.toString());
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
