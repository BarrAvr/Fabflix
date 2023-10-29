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

//        String url = "api/search-results.html?";
//
//        if (listState.get("type").getAsString().equals("genre")) {
//            url += "type=genre&";
//            url += "genre=" + listState.get("genre").getAsString();
//            url += "&";
//        } else if (listState.get("type").getAsString().equals("prefix")) {
//            url += "type=prefix&";
//            url += "prefix=" + listState.get("prefix").getAsString();
//            url += "&";
//        } else {
//            url += "type=general&";
//            url += "title=";
//            if (listState.get("title") != null) {
//                url += listState.get("title").getAsString();
//            }
//            url += "&";
//            url += "star=";
//            if (listState.get("star") != null) {
//                url += listState.get("star").getAsString();
//            }
//            url += "&";
//            url += "year=";
//            if (listState.get("year") != null) {
//                url += listState.get("year").getAsString();
//            }
//            url += "&";
//            url += "director=";
//            if (listState.get("director") != null) {
//                url += listState.get("director").getAsString();
//            }
//            url += "&";
//
//        }
//        url += "sortBy=" + listState.get("sortBy").getAsString();
//        url += "&";
//
//        url += "titleOrder=" + listState.get("titleOrder").getAsString();
//        url += "&";
//
//        url += "ratingOrder=" + listState.get("ratingOrder").getAsString();
//
        PrintWriter out = response.getWriter();
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("url", url);
//        System.out.println("url printing...");
//        System.out.println(url);
//        System.out.println(jsonObject.toString());
//        out.write(jsonObject.toString());
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
            String pageSize = request.getParameter("pageSize");
            String currentPage = request.getParameter("currentPage");

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
                    newState.addProperty("titleOrder", titleOrder);
                }
                if (ratingOrder != null) {
                    newState.addProperty("ratingOrder", ratingOrder);
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