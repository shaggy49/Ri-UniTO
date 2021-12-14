package com.example.demo.DAO;

public class Insegnamento {
    private int docente;
    private String corso;

    public Insegnamento(int docente, String corso) {
        this.docente = docente;
        this.corso = corso;
    }

    public String getCorso() {
        return corso;
    }

    public int getDocente() {
        return docente;
    }

    @Override
    public String toString() {
        return "Insegnamento{" +
                "docente=" + docente +
                ", corso='" + corso + '\'' +
                '}';
    }
}
