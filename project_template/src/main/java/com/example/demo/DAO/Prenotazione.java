package com.example.demo.DAO;

import java.util.Objects;

public class Prenotazione {
    private int docente;
    private int utente;
    private String corso;

    public Prenotazione(int docente, int utente, String corso) {
        this.docente = docente;
        this.utente = utente;
        this.corso = corso;
    }

    public int getDocente() {
        return docente;
    }

    public int getUtente() {
        return utente;
    }

    public String getCorso() {
        return corso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prenotazione that = (Prenotazione) o;
        return docente == that.docente && utente == that.utente && Objects.equals(corso, that.corso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docente, utente, corso);
    }
}
