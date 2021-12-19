package com.reservation.application.servlets;

import com.google.gson.Gson;
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
        /* fixme: remove this comment
        * non va bene stampare la lista tramite html: più che altro non ha senso
        * quello che servirà al frontend saranno proprio gli oggetti per poterli manipolare e "trasformare"
        * graficamente, occorre quindi per forza di cose trasformare questa lista di oggetti prelevata dal db in un
        * oggetto JSON, che verrà poi "spacchettato" a livello di frontend (web o android che sia).
        * */
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        List<ReservationAvailable> availableReservations = DAO.getAvailableReservations();
        Gson gson = new Gson();
        String availableReservationsJSON = gson.toJson(availableReservations);
        out.println(availableReservationsJSON);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        /* fixme: remove this comment
        * non va bene rispondere impostando il tipo di contenuto come text di tipo html
        * perché questi endpoint diventano difficili da gestire da android se il testo restituito è un html
        * */
        PrintWriter out = response.getWriter();
        try{
            //passaggio di parametri come query params, se si vuol usare il body delle post: https://stackoverflow.com/questions/14525982/getting-request-payload-from-post-request-in-java-servlet
            int idReservationAvailable = Integer.parseInt(request.getParameter("idReservationAvailable"));
            int idUser = Integer.parseInt(request.getParameter("idUser"));
            DAO.bookRequestedReservation(idReservationAvailable, idUser);
            out.println("Trasformazione eseguita");
        }
        catch (SQLException e){
            String errorMessage = e.getMessage().substring(e.getMessage().indexOf("$") + 1, e.getMessage().lastIndexOf("$"));;
            String messageToPrint = "";
            if(errorMessage.equals("teacherunique"))
                messageToPrint = "Il professore selezionato ha già una prenotazione attiva per quell'ora";
            else if (errorMessage.equals("userunique"))
                messageToPrint = "L'utente selezionato ha già una prenotazione attiva per quell'ora";
            else
                messageToPrint = e.getMessage();
            out.println(messageToPrint);
        }
        catch (NumberFormatException e) {
            out.println("Devi inserire un numero valido");
        }
        out.flush();
        out.close();
    }
}
