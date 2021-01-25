package hr.kacan.trznica.models

import java.io.Serializable

data class Ponuda(var cijena: Double, var email: String, var id: Long, var ime: String, var naziv: String,
                  var opis: String, var prezime: String, var slika: String, var tipProizvoda: Int,
                  var grad: String, var tel: String, var korisnikId: Int) : Serializable {

                  constructor(cijena: Double, naziv: String, opis: String, slika: String, tipProizvoda: Int, korisnikId: Int) :
                          this(cijena, "", System.currentTimeMillis(), "", naziv, opis, "", slika, tipProizvoda, "", "", korisnikId)
}