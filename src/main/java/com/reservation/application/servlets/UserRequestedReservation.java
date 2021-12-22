package com.reservation.application.servlets;

import com.google.gson.Gson;
import com.reservation.application.dao.DAO;
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
import java.util.List;

@WebServlet(name = "UserRequestedReservation", value = "/user-reservations")
public class UserRequestedReservation extends HttpServlet {

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

    /**
     * Un utente chiama questo endpoint solamente dopo essersi autenticato usando l'endpoint
     * di log-in.
     * L'endpoint restituirá la lista di prenotazioni corrispondente alla mail presente
     * in sessione utente.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String email = (String) session.getAttribute("email");

        if(email == null || email == "guest"){
            out.println("You must log in to see your reservations");
            throw new ServletException();
        }
        else{
            List<ReservationRequested> userRequestedReservations = DAO.getRequestedReservationsByUserMail(email);
            Gson gson = new Gson();
            String toJson = gson.toJson(userRequestedReservations);
            out.println(toJson);
        }
        out.flush();
        out.close();
    }


}
