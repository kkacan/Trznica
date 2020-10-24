package hr.kacan.trznica.models;

import java.io.Serializable;

public class Korisnik implements Serializable {

    private Integer id;
    private String ime;
    private String prezime;
    private String grad;
    private String adresa;
    private String tel;
    private String email;
    private String lozinka;

    public Korisnik(String ime, String prezime, String grad, String adresa, String tel, String email, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.grad = grad;
        this.adresa = adresa;
        this.tel = tel;
        this.email = email;
        this.lozinka = lozinka;
    }

    public Korisnik(String email, String lozinka) {
        this.email = email;
        this.lozinka = lozinka;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
