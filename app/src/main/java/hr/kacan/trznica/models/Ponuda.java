package hr.kacan.trznica.models;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Ponuda implements Serializable {

    private Double cijena;
    private String email;
    private Long id;
    private String ime;
    private String naziv;
    private String opis;
    private String prezime;
    private String slika;
    private int tipProizvoda;
    private String grad;
    private String tel;
    private int korisnikId;


    public Ponuda(double cijena, String naziv, String opis, String slika, int tipProizvoda, int korisnikId) {
        this.cijena = cijena;
        this.naziv = naziv;
        this.opis = opis;
        this.slika = slika;
        this.tipProizvoda = tipProizvoda;
        this.korisnikId = korisnikId;
        this.id = System.currentTimeMillis();

    }



    public int getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(int korisnikId) {
        this.korisnikId = korisnikId;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Double getCijena() {
        return cijena;
    }

    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public int getTipProizvoda() {
        return tipProizvoda;
    }

    public void setTipProizvoda(int tipProizvodaNaziv) {
        this.tipProizvoda = tipProizvodaNaziv;
    }

}
