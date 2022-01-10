package com.reservation.application.servlets;

import com.reservation.application.dao.DAO;

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

@WebServlet(name = "LogInServlet", value = "/log-in")
public class LogInServlet extends HttpServlet {

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
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        if(email != null && password != null){
            String role = null;
            int uID=-1;
            try {
                role = DAO.getUserRole(email, password);
                uID = DAO.getUserID(email, password);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
                out.println(e.getMessage());
            }
            if(role.equals("")){

                out.println("Nessun account trovato");
                session.setAttribute("role", "guest");
            }
            else{
                out.println(String.format("{\"role\": \"%s\"}",role));
                session.setAttribute("role", role);
                session.setAttribute("uID", uID);
            }
            session.setAttribute("email", email);
            out.flush();
            out.close();
        }

    }


}
