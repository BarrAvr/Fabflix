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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "AddStarServlet", urlPatterns = "/_dashboard/api/add-star")
public class AddStarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
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
        System.out.println("AddStarServlet hit");
        response.setContentType("application/json"); // Response mime type

        String name = request.getParameter("name");
        int birthYear = Integer.parseInt(request.getParameter("birthYear"));

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try {

            Connection conn = dataSource.getConnection();
//
            CallableStatement call = conn.prepareCall("call add_star(?, ?)");
            call.setString(1, name);
            call.setInt(2,birthYear);
            System.out.println("Final call is: " + call);
//
            // Perform the query
            ResultSet rs = call.executeQuery();

//            while (rs.next()) {
//                System.out.println(rs.getString("@id := SUBSTRING(max(id), 3, 8)"));
//            }

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
            }

//            while (rs.next()) {
//                System.out.println(rs.getString("@integerid := CAST(@id AS SIGNED) + 1"));
//            }

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
            }

            while (rs.next()) {
                String starId = rs.getString("starid");
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "Success! Star ID: " + starId);
                out.write(responseJsonObject.toString());
            }

            response.setStatus(200);
            conn.close();

        } catch (Exception e) {

            // Write error message JSON object to output
            e.printStackTrace();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "fail");
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

    }
}