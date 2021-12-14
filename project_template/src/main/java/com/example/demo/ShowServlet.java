package com.example.demo;

import com.example.demo.DAO.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "ShowServlet", value = "/show-servlet")
public class ShowServlet extends HttpServlet {
    private String message;

/*    public void init(ServletConfig config) {
        message = "Docenti disponibili";
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
    }*/

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        Corso corso = new Corso(request.getParameter("docente"));
        ServletContext context = getServletContext();
        RequestDispatcher rd = context.getNamedDispatcher("DAOServlet");
        request.setAttribute("corso", corso);
        rd.include(request,response);
        Gson gson = new Gson(); // traduttore da e verso formato JSON
        String listaCorsiJSON = (String) request.getAttribute("listaCorsiDocente");
        System.out.println("Lista ricevuta dal DAOServlet: "+listaCorsiJSON);
        Type listDocentiType = new TypeToken<ArrayList<Docente>>(){}.getType();
        ArrayList<Docente> docenti = gson.fromJson(listaCorsiJSON,listDocentiType);
        System.out.println("La lista dei docenti arrivata è così: "+docenti.toString() );
        //ArrayList<Docente> docenti = DAO.showDocentiForCourse(corso);

        // Hello
        PrintWriter out = response.getWriter();
        /*out.println("<html><body>");*/
        if(!docenti.isEmpty()){
            out.println("<p> <b> I docenti disponibili per il corso di "+corso.getTitolo()+" sono: </b> </p>");
            for (Docente d: docenti) {
                out.println("<p>"+d+"</p>");
            }
        }
        else {
            out.println("<p>Non ci sono docenti per il corso \""+corso.getTitolo()+"\"</p>");
        }
        out.flush();
        //out.close(); non va fatto!
        /*out.println("</body></html>");*/
    }

    public void destroy() {
    }
}