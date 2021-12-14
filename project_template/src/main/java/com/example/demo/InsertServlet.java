package com.example.demo;

import com.example.demo.DAO.DAO;
import com.example.demo.DAO.Docente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "InsertServlet", value = "/insert-servlet")
public class InsertServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("text/html");

        if(session.isNew()){
            session.setAttribute("role", "guest");
        }

        if(session.getAttribute("role").equals("admin")){
            String name = request.getParameter("name");
            String surnname = request.getParameter("surname");

            if(name != null && surnname != null){
                Docente docente = new Docente(name,surnname);

                DAO.insertTeacher(docente);
                // Hello
                PrintWriter out = response.getWriter();
                out.println("<p> Successo!<p>");
                out.println(String.format("<p>Il docente \"%s %s\" è stato correttamente inserito</p>",name,surnname));
                out.println(String.format("<p>Inserimento effettuato da: %s.</p>",session.getAttribute("account")));
                out.flush();
                out.close();
            }

        }
        else {
            PrintWriter out = response.getWriter();
            out.println("<p> Errore!<p>");
            out.println(String.format("<p>Non hai i permessi per inserire un docente, il tuo ruolo risulta essere: %s.</p>",session.getAttribute("role")));
            out.flush();
            out.close();
        }



    }
}
