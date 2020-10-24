package hr.kacan.trznica.models;

import java.io.Serializable;

public class ResponseKorisnik implements Serializable {
    private String response;
    private Korisnik korisnik;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
