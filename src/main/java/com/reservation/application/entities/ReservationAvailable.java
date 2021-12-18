package com.reservation.application.entities;

public class ReservationAvailable {
    private int idTeacher;
    private int idCourse;
    private String date;
    private String time;

    public ReservationAvailable(int idTeacher, int idCourse, String date, String time) {
        this.idTeacher = idTeacher;
        this.idCourse = idCourse;
        this.date = date;
        this.time = time;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "ReservationAvailable{" +
                " idTeacher=" + idTeacher +
                ", idCourse=" + idCourse +
                ", date=" + date +
                ", time='" + time + '\'' +
                '}';
    }

}

