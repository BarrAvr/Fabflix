import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    private final ArrayList<String> allowedURIs = new ArrayList<>();

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("LoginFilter: " + httpRequest.getRequestURI());

//        if (httpRequest.getRequestURI().endsWith("/_dashboard/index.html")) {
//            System.out.println("dashboard index match found!");
//            System.out.println(httpRequest.getSession().getAttribute("employee"));
//            if (httpRequest.getSession().getAttribute("employee") == null) {
//                System.out.println("Employee not logged in!");
//                httpResponse.sendRedirect("login.html");
//            }
//        }
        // Check if this URL is allowed to access without logging in
        if (this.isUrlAllowed(httpRequest, httpRequest.getRequestURI())) {

            System.out.println(" -> allowed");
            chain.doFilter(request, response);
            return;
        } else {
            httpResponse.sendRedirect("login.html");
        }
    }

    private boolean isUrlAllowed(HttpServletRequest httpRequest, String requestURI) {

        boolean isUser = httpRequest.getSession().getAttribute("user") != null;
        boolean isEmployee = httpRequest.getSession().getAttribute("employee") != null;
        // if not logged in at all, block both dashboards

        if (isEmployee) {
            System.out.println("Employee logged in, can access all pages");
            return true;
        }
        else if (isUser) {
            boolean userCanAccess = !requestURI.endsWith("/_dashboard/index.html");
            System.out.println("User logged in, but cannot access dashboard/index");
            return userCanAccess;
        }

        boolean anonUserCanAccess = allowedURIs.stream().anyMatch(requestURI.toLowerCase()::endsWith);
        System.out.println("Anon user found, returning " + anonUserCanAccess);
        return anonUserCanAccess;
    }

    public void init(FilterConfig fConfig) {
        allowedURIs.add("login.html");
        allowedURIs.add("login.js");
        allowedURIs.add("api/login");
        allowedURIs.add("/items");
        allowedURIs.add("/list-state");
        allowedURIs.add("/_dashboard/login.html");
        allowedURIs.add("/_dashboard/login.js");
        allowedURIs.add("/_dashboard/api/employee-login");
        allowedURIs.add("/api/metadata");
        allowedURIs.add("/_dashboard/api/add-star");
        allowedURIs.add("/_dashboard/api/add-movie");
//        allowedURIs.add("/_dashboard/index.html");
    }

    public void destroy() {
        // ignored.
    }

}
