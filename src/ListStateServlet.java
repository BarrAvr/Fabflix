import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ListStateServlet", urlPatterns = "/list-state")

public class ListStateServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        JsonObject listState = (JsonObject) session.getAttribute("listState");

        if (listState == null) {
            PrintWriter out = response.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("listState", "null");
            out.write(jsonObject.toString());
        }

        PrintWriter out = response.getWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("listState", listState.toString());
        out.write(jsonObject.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        try {
            response.setContentType("application/json");
            JsonObject result = new JsonObject();

            HttpSession session = request.getSession();

            JsonObject listState = (JsonObject) session.getAttribute("listState");
            System.out.println(listState);
            if (listState == null) {
                listState = new JsonObject();
            }

            String genre = request.getParameter("genre");
            String prefix = request.getParameter("prefix");
            String titleSearch = request.getParameter("titleSearch");
            String starSearch = request.getParameter("starSearch");
            String yearSearch = request.getParameter("yearSearch");
            String directorSearch = request.getParameter("directorSearch");
            String sortOrder = request.getParameter("sortOrder");
            String titleAscending = request.getParameter("titleAscending");
            String ratingAscending = request.getParameter("ratingAscending");
            String pageSize = request.getParameter("pageSize");
            String currentPage = request.getParameter("currentPage");

            synchronized (listState) {

                JsonObject newState = new JsonObject();
                if (genre != null) {
                    newState.addProperty("genre", genre);
                }
                if (prefix != null) {
                    newState.addProperty("prefix", prefix);
                }
                if (titleSearch != null) {
                    newState.addProperty("titleSearch", titleSearch);
                }
                if (starSearch != null) {
                    newState.addProperty("starSearch", starSearch);
                }
                if (yearSearch != null) {
                    newState.addProperty("yearSearch", yearSearch);
                }
                if (directorSearch != null) {
                    newState.addProperty("directorSearch", directorSearch);
                }
                if (sortOrder != null) {
                    newState.addProperty("sortOrder", sortOrder);
                }
                if (titleAscending != null) {
                    newState.addProperty("titleAscending", titleAscending);
                }
                if (ratingAscending != null) {
                    newState.addProperty("ratingAscending", ratingAscending);
                }
                if (pageSize != null) {
                    newState.addProperty("pageSize", pageSize);
                }
                if (currentPage != null) {
                    newState.addProperty("currentPage", currentPage);
                }

                session.setAttribute("listState", newState);

                result.addProperty("message", "success");
                result.addProperty("new listState", newState.toString());

                out.write(result.toString());
                response.setStatus(200);
            }
        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}