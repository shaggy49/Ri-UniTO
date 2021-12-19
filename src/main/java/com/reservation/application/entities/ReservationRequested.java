package com.reservation.application.entities;


public class ReservationRequested {
    private int idUser;
    private int idTeacher;
    private int idCourse;
    private String rDate;
    private String rTime;
    private String status;

    public ReservationRequested(int idTeacher, int idUser, int idCourse, String rDate, String rTime, String status) {
        this.idUser = idTeacher;
        this.idCourse = idCourse;
        this.rDate = rDate;
        this.rTime = rTime;
        this.status = status;
    }


    public int getIdUser() {
        return idUser;
    }

    public int getIdTeacher() {
        return idUser;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public String getDate() {
        return rDate;
    }

    public String getTime() {
        return rTime;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ReservationRequested{" +
                " idUser=" + idUser +
                ", idTeacher=" + idTeacher +
                ", idCourse=" + idCourse +
                ", rDate=" + rDate +
                ", rTime='" + rTime + '\'' +
                ", status=" + status +
                '}';
    }

}
