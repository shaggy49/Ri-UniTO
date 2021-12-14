package com.reservation.application.servlets;

import com.reservation.application.dao.DAO;
import com.reservation.application.entities.ReservationAvailable;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Servlet", value = "/Servlet")
public class Servlet extends HttpServlet {

    public void init(ServletConfig config) {
        try {
            super.init(config);
            ServletContext ctx = config.getServletContext();
            String url = ctx.getInitParameter("url");
            String user = ctx.getInitParameter("user");
            String password = ctx.getInitParameter("password");
            DAO.registerDriver(url, user, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        List<ReservationAvailable> availableReservations = DAO.getAvailableReservations();
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>test</title></head><body>");
        for (ReservationAvailable reservation : availableReservations) {
            out.println("<p> Lista prelevata dal db!<p>");
            out.println(String.format("<p>%s</p>", reservation));
        }
        out.println("</body></html>");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
