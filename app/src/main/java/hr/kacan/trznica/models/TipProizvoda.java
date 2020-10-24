package hr.kacan.trznica.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class TipProizvoda implements Serializable {

    private int id;
    private String naziv;
    private String slika;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    @NonNull
    @Override
    public String toString() {
        return this.naziv;
    }
}
