package com.reservation.application.entities;

public class Teacher {
    private int id;
    private String name;
    private String surname;

    public Teacher(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public String getSurname() {
        return surname;
    }


    @Override
    public String toString() {
        return name + " " + surname + " ";
    }
}
