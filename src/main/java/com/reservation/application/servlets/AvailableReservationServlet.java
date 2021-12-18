package com.reservation.application.servlets;

import com.reservation.application.dao.DAO;
import com.reservation.application.entities.ReservationAvailable;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AvailableReservationServlet", value = "/available-reservations")
public class AvailableReservationServlet extends HttpServlet {

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
        /* todo: restituire una stringa json invece che stampe html
        * non va bene stampare la lista tramite html: più che altro non ha senso
        * quello che servirà al frontend saranno proprio gli oggetti per poterli manipolare e "trasformare"
        * graficamente, occorre quindi per forza di cose trasformare questa lista di oggetti prelevata dal db in un
        * oggetto JSON, che verrà poi "spacchettato" a livello di frontend (web o android che sia).
        * */
        response.setContentType("text/html");
        List<ReservationAvailable> availableReservations = DAO.getAvailableReservations();
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>test</title></head><body>");
        out.println("<p> Lista prelevata dal db!<p>");
        for (ReservationAvailable reservation : availableReservations) {
            out.println(String.format("<p>%s</p>", reservation));
        }
        out.println("</body></html>");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        /* todo: cambiare la risposta in semplice testo
        * non va bene rispondere impostando il tipo di contenuto come text di tipo html
        * perché questi endpoint diventano difficili da gestire da android se il testo restituito è un html
        * */
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>test</title></head><body>");
        try{
            int idReservationAvailable = Integer.parseInt(request.getParameter("idReservationAvailable"));
            int idUser = Integer.parseInt(request.getParameter("idUser"));
            DAO.bookRequestedReservation(idReservationAvailable, idUser);
            out.println("<p> Trasformazione eseguita</p>");
        }
        catch (SQLException e){
            String errorMessage = e.getMessage();
            //todo: tradurre errori di vincolo unique di resReq (tokenizzando la stringa 'errorMessage'):
            //vedere errori su Notion!
            //caso di utente: "L'utente ha già una prenotazione attiva per quell'ora!"
            //caso prof: "Il professore ha già una prenotazione attiva per quell'ora"
            //Duplicate entry '4-lun-18' for key 'id_teacher'
            //Duplicate entry '2-mar-15-booked' for key 'id_user'
            out.println(String.format("<p> Trasformazione non eseguita, errore: %s</p>", e.getMessage()));
        }
        catch (NumberFormatException e) {
            out.println("<p> You pass a not valid number! </p>");
        }
        out.println("</body></html>");
        out.flush();
        out.close();
    }
}
