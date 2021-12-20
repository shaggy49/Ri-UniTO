package com.reservation.application.entities;


public class ReservationRequested {
    private int id;
    private User user;
    private Teacher teacher;
    private Course course;
    private String rDate;
    private String rTime;
    private String status;

    public ReservationRequested(int id, User user, Teacher teacher, Course course, String rDate, String rTime, String status) {
        this.id = id;
        this.user = user;
        this.teacher = teacher;
        this.course = course;
        this.rDate = rDate;
        this.rTime = rTime;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Course getCourse() {
        return course;
    }

    public String getrDate() {
        return rDate;
    }

    public String getrTime() {
        return rTime;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ReservationRequested{" +
                "user=" + user +
                ", teacher=" + teacher +
                ", course=" + course +
                ", rDate='" + rDate + '\'' +
                ", rTime='" + rTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
