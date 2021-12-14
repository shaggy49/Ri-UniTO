package com.example.demo.DAO;

import java.util.Objects;

public class Corso {
    private String titolo;

    public Corso(String titolo) {
        this.titolo = titolo;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    @Override
    public String toString() {
        return "Corso{" +
                "titolo='" + titolo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corso corso = (Corso) o;
        return Objects.equals(titolo, corso.titolo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo);
    }
}
