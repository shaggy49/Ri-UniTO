package com.example.demo;

import com.example.demo.DAO.DAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AuthServlet", value = "/auth-servlet")
public class AuthServlet extends HttpServlet {
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
        response.setContentType("text/html");

        String account = request.getParameter("account");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        if(account != null && password != null){
            PrintWriter out = response.getWriter();
            String role = DAO.getUserRole(account, password);
            if(role.equals("")){
                out.println(String.format("<p>Non sembra esser presente alcun account registrato con l'indirizzo %s.</p>",account));
                session.setAttribute("role", "guest");
            }
            else{
                out.println(String.format("<p>Il tuo ruolo risulta essere: %s.</p>",role));
                if(role.equals("admin"))
                    out.println(String.format("<p>Adesso potrai eseguire l'inserimento di un docente!</p>"));
                session.setAttribute("role", role);
            }
            out.flush();
            out.close();
            session.setAttribute("account", account);
        }

    }
}
