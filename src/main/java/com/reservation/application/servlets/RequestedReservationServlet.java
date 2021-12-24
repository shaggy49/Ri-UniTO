package com.reservation.application.servlets;

import com.google.gson.Gson;
import com.reservation.application.dao.DAO;
import com.reservation.application.entities.ReservationAvailable;
import com.reservation.application.entities.ReservationRequested;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "RequestedReservationServlet", value = "/requested-reservations")
public class RequestedReservationServlet extends HttpServlet {

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
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String role = (String) session.getAttribute("role");

        if(role != null && role.equals("admin")){
            List<ReservationRequested> requestedReservations = DAO.getRequestedReservations();
            Gson gson = new Gson();
            String toJson = gson.toJson(requestedReservations);
            out.println(toJson);
        }
        else{
            out.println("Non puoi compiere questa azione");
        }

        out.flush();
        out.close();
    }

    /**
     * Status has to be: "booked", "deleted", "completed"
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String role = (String) session.getAttribute("role");

        if(role != null && role.equals("admin") || role.equals("user")){
            try {
                int requestedReservation = Integer.parseInt(request.getParameter("idRequestedReservation"));
                String status = request.getParameter("status");
                DAO.setReservationState(requestedReservation, status);
                out.println("Prenotazione modificata");
            } catch (NumberFormatException e) {
                out.println("Inserire un numero valido");
            } catch (SQLException e) {
                e.printStackTrace();
                out.println(e.getMessage());
            }
        }
        else{
            out.println("Non puoi compiere questa azione");
        }

        out.flush();
        out.close();
    }
}
