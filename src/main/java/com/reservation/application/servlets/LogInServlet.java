package com.reservation.application.servlets;

import com.google.gson.Gson;
import com.reservation.application.dao.DAO;
import com.reservation.application.entities.ReservationRequested;
import com.reservation.application.entities.User;

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
import java.util.regex.Pattern;

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

        Pattern patternEmail = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        boolean isEmailCorrectForm = patternEmail.matcher(email).matches();

        HttpSession session = request.getSession();

        if(email != null && password != null && isEmailCorrectForm){
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
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
        }else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Inserire un email e una password validi.");
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String role = (String) session.getAttribute("role");

        if(role != null && role.equals("admin")){
            List<User> userList = DAO.getUsers();
            Gson gson = new Gson();
            String toJson = gson.toJson(userList);
            out.println(toJson);
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("Non puoi compiere questa azione");
        }

        out.flush();
        out.close();
    }

    //logout function -> deletes httponly cookies storing session.
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        session.invalidate();
        response.setStatus(HttpServletResponse.SC_OK);

        out.flush();
        out.close();
    }
}
