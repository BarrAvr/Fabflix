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
            JsonArray tableArray1 = new JsonArray();
            while(rs.next()) {
                //Iterate through first resultset
                String columnName = rs.getString("Field");
                String columnType = rs.getString("Type");
                JsonObject rowObject = new JsonObject();
                rowObject.addProperty("columnName", columnName);
                rowObject.addProperty("columnType", columnType);
                System.out.println("Added " + columnName + ", " + columnType + " to the 1st tableArray");
                tableArray1.add(rowObject);
            }
            System.out.println("Done with RS / Table 1");
            System.out.println("Adding" + tableArray1 + " to responseJsonArray");
            responseJsonArray.add(tableArray1);
            System.out.println(responseJsonArray);

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray2 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 2nd tableArray");
                    tableArray2.add(rowObject);
                }
                System.out.println("Done with RS / Table 2");
                System.out.println("Adding" + tableArray2 + " to responseJsonArray");
                responseJsonArray.add(tableArray2);
                System.out.println(responseJsonArray);
            }


            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray3 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 3rd tableArray");
                    tableArray3.add(rowObject);
                }
                System.out.println("Done with RS / Table 3");
                System.out.println("Adding" + tableArray3 + " to responseJsonArray");
                responseJsonArray.add(tableArray3);
            }


            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray4 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 4th tableArray");
                    tableArray4.add(rowObject);
                }
                System.out.println("Adding" + tableArray4 + " to responseJsonArray");
                responseJsonArray.add(tableArray4);
                System.out.println(responseJsonArray);
            }


            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray5 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 5th tableArray");
                    tableArray5.add(rowObject);
                }
                System.out.println("Adding" + tableArray5 + " to responseJsonArray");
                responseJsonArray.add(tableArray5);
                System.out.println(responseJsonArray);
            }


            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray6 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 6th tableArray");
                    tableArray6.add(rowObject);
                }
                System.out.println("Adding" + tableArray6 + " to responseJsonArray");
                responseJsonArray.add(tableArray6);
                System.out.println(responseJsonArray);
            }


            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray7 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 7th tableArray");
                    tableArray7.add(rowObject);
                }
                System.out.println("Adding" + tableArray7 + " to responseJsonArray");
                responseJsonArray.add(tableArray7);
                System.out.println(responseJsonArray);
            }

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray8 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 8th tableArray");
                    tableArray8.add(rowObject);
                }
                System.out.println("Adding" + tableArray8 + " to responseJsonArray");
                responseJsonArray.add(tableArray8);
                System.out.println(responseJsonArray);
            }


            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray9 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 9th tableArray");
                    tableArray9.add(rowObject);
                }
                System.out.println("Adding" + tableArray9 + " to responseJsonArray");
                responseJsonArray.add(tableArray9);
                System.out.println(responseJsonArray);
            }

            num++;

            rs.close();
            if (call.getMoreResults()) {
                rs = call.getResultSet();
                System.out.println("Processing ResultSet " + num + "...");
                JsonArray tableArray10 = new JsonArray();
                while(rs.next()) {
                    //Iterate through second resultset
                    String columnName = rs.getString("Field");
                    String columnType = rs.getString("Type");
                    JsonObject rowObject = new JsonObject();
                    rowObject.addProperty("columnName", columnName);
                    rowObject.addProperty("columnType", columnType);
                    System.out.println("Added " + columnName + ", " + columnType + " to the 9th tableArray");
                    tableArray10.add(rowObject);
                }
                System.out.println("Adding" + tableArray10 + " to responseJsonArray");
                responseJsonArray.add(tableArray10);
                System.out.println(responseJsonArray);
            }


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
