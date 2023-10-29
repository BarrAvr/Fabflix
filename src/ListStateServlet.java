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
            jsonObject.addProperty("message", "no previous ");
            out.write(jsonObject.toString());
            out.close();
        }

        PrintWriter out = response.getWriter();
        out.write(listState.toString());
        out.close();
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

            String type = request.getParameter("type"); // genre, prefix, general
            String genre = request.getParameter("genre");
            String prefix = request.getParameter("prefix");
            String title = request.getParameter("title");
            String star = request.getParameter("star");
            String year = request.getParameter("year");
            String director = request.getParameter("director");
            String sortBy = request.getParameter("sortBy");
            String titleOrder = request.getParameter("titleOrder");
            String ratingOrder = request.getParameter("ratingOrder");
            String page = request.getParameter("page");
            String count = request.getParameter("count");

            synchronized (listState) {

                JsonObject newState = new JsonObject();
                if (type != null) {
                    newState.addProperty("type", type);
                }
                if (genre != null) {
                    newState.addProperty("genre", genre);
                }
                if (prefix != null) {
                    newState.addProperty("prefix", prefix);
                }
                if (title != null) {
                    newState.addProperty("title", title);
                }
                if (star != null) {
                    newState.addProperty("star", star);
                }
                if (year != null) {
                    newState.addProperty("year", year);
                }
                if (director != null) {
                    newState.addProperty("director", director);
                }
                if (sortBy != null) {
                    newState.addProperty("sortBy", sortBy);
                }
                if (titleOrder != null) {
                    System.out.println("Title Order: " + titleOrder);
                    newState.addProperty("titleOrder", titleOrder);
                }
                if (ratingOrder != null) {
                    newState.addProperty("ratingOrder", ratingOrder);
                }
                if (page != null) {
                    newState.addProperty("page", page);
                }
                if (count != null) {
                    newState.addProperty("count", count);
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