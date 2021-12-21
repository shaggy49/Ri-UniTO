package com.reservation.application.servlets;

import com.google.gson.Gson;
import com.reservation.application.dao.DAO;
import com.reservation.application.entities.Teacher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TeacherServlet", value = "/teacher")
public class TeacherServlet extends HttpServlet {

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
        List<Teacher> teachers = DAO.getTeachers();
        Gson gson = new Gson();
        String toJson = gson.toJson(teachers);
        out.println(toJson);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            DAO.insertTeacher(name, surname);
            out.println("Inserimento effettuato");
        } catch (NumberFormatException e) {
            out.println("Inserire un numero valido");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println(e.getMessage());
        }
        out.flush();
        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            int idTeacher = Integer.parseInt(request.getParameter("idTeacher"));
            DAO.removeTeacher(idTeacher);
            out.println("Docente rimosso");
        } catch (NumberFormatException e) {
            out.println("Inserire un numero valido");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println(e.getMessage());
        }
        out.flush();
        out.close();
    }
}
