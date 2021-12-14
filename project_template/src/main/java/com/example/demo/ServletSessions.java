package com.example.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

//TODO: modificare questa servlet per renderla simile alla AuthServlet
@WebServlet(name = "ServletSessions", urlPatterns = {"/ServletSessions"})
public class ServletSessions extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String userName = request.getParameter("utente");
        String sessionID = request.getParameter("sessione");
        HttpSession s = request.getSession();
        String jsessionID = s.getId(); // estraggo il session ID
        System.out.println("JSessionID:" + jsessionID);
        System.out.println("sessionID ricevuto:" + sessionID);
        System.out.println("userName ricevuto:" + userName);

        if (userName != null) {
            s.setAttribute("userName", userName); // salvo dei dati in sessione...
        }
        if (sessionID!=null && jsessionID.equals(sessionID)) {
            //System.out.println("sessione riconosciuta!");
            out.print("sessione riconosciuta!");
        } else {
            //System.out.println(jsessionID);
            out.print(jsessionID);
        }
    }
}
