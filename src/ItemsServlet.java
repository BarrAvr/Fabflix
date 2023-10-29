import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ItemServlet", urlPatterns = "/items")

public class ItemsServlet extends HttpServlet {
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");

        if (previousItems == null) {
            previousItems = new ArrayList<String>();
        }

        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();
        for (String item : previousItems) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("itemName", item);
            jsonArray.add(jsonObject);
        }

        out.write(jsonArray.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        try {
            response.setContentType("application/json");
            JsonObject result = new JsonObject();

            String type = request.getParameter("type");

            String newItem = request.getParameter("newItem");
            HttpSession session = request.getSession();

            ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
            System.out.println(previousItems);

            if (previousItems == null) {
                previousItems = new ArrayList<String>();
            }

            synchronized (previousItems) {

                if (type.equals("add")) {
                    previousItems.add(newItem);

                    session.setAttribute("previousItems", previousItems);

                    result.addProperty("message", "added item " + newItem);
                    result.addProperty("new previousItems", previousItems.toString());

                    out.write(result.toString());
                    response.setStatus(200);
                } else if (type.equals("remove")) {
                    String itemToRemove = request.getParameter("itemToRemove");
                    previousItems = (ArrayList<String>) session.getAttribute("previousItems");
                    System.out.println(previousItems);

                    if (previousItems == null) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("message", "Error: you can't remove any items if your cart is empty");
                        out.write(jsonObject.toString());
                    } else {
                        previousItems.remove(itemToRemove);
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("message", "removed item " + itemToRemove);
                        jsonObject.addProperty("previousItems", previousItems.toString());
                        out.write(jsonObject.toString());
                    }
                } else if (type.equals("delete")) {
                    String itemToDelete = request.getParameter("itemToDelete");
                    previousItems = (ArrayList<String>) session.getAttribute("previousItems");
                    previousItems.removeIf(m -> m.equals(itemToDelete));
                    session.setAttribute("previousItems", previousItems);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("message", "deleted item " + itemToDelete);
                    jsonObject.addProperty("previousItems", previousItems.toString());
                    out.write(jsonObject.toString());
                }
            }
        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            e.printStackTrace();
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}