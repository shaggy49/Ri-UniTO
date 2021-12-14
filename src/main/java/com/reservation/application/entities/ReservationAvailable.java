package com.reservation.application.entities;

import java.util.Objects;

public class ReservationAvailable {
    private int id;
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

    public int getId() {
        return id;
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
                "id=" + id +
                ", idTeacher=" + idTeacher +
                ", idCourse=" + idCourse +
                ", date=" + date +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationAvailable that = (ReservationAvailable) o;
        return id == that.id;
    }
}

