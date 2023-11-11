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

@WebServlet(name = "MetadataServlet", urlPatterns = "/_dashboard/api/metadata")
public class MetadataServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try {

            Connection conn = dataSource.getConnection();

            CallableStatement call = conn.prepareCall("call get_metadata");

            // Perform the query
            ResultSet rs = call.executeQuery();

            JsonArray responseJsonArray = new JsonArray();

            int num = 1;
            System.out.println("Processing ResultSet " + num + "...");
            JsonArray tableArray = new JsonArray();
            while(rs.next()) {
                //Iterate through first resultset
                String columnName = rs.getString("Field");
                String columnType = rs.getString("Type");
                JsonObject rowObject = new JsonObject();
                rowObject.addProperty("columnName", columnName);
                rowObject.addProperty("columnType", columnType);
                tableArray.add(rowObject);
            }
            responseJsonArray.add(tableArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    tableArray.add(rowObject);
                }
            }
            responseJsonArray.add(tableArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    tableArray.add(rowObject);
                }
            }
            responseJsonArray.add(tableArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    tableArray.add(rowObject);
                }
            }
            responseJsonArray.add(tableArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    tableArray.add(rowObject);
                }
            }
            responseJsonArray.add(tableArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    tableArray.add(rowObject);
                }
            }
            responseJsonArray.add(tableArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    tableArray.add(rowObject);
                }
            }
            responseJsonArray.add(tableArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    tableArray.add(rowObject);
                }
            }
            responseJsonArray.add(tableArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    tableArray.add(rowObject);
                }
            }
            responseJsonArray.add(tableArray);

            rs.close();
            call.close();
            conn.close();

            // Write JSON string to output
            out.write(responseJsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);
            conn.close();

        } catch (Exception e) {

            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }
}
