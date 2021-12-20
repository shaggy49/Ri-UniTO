package com.reservation.application.entities;

public class ReservationAvailable {
    private int id;
    private Teacher teacher;
    private Course course;
    private String date;
    private String time;

    public ReservationAvailable(int id, Teacher teacher, Course course, String date, String time) {
        this.id = id;
        this.teacher = teacher;
        this.course = course;
        this.date = date;
        this.time = time;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Course getCourse() {
        return course;
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
                "teacher=" + teacher +
                ", course=" + course +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

