package com.reservation.application;

import java.io.*;
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.annotation.*;

@WebServlet(name = "tryInsert", value = "/insert")
public class InsertServlet extends HttpServlet {
    private String message;

    public void init(ServletConfig config) {
        ServletContext ctx = config.getServletContext();
        String url = ctx.getInitParameter("url");
        DAO.insertSomething(url);
        message = "You inserted a valid tuple motherfucker!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}