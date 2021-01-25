package hr.kacan.trznica.models

import java.io.Serializable

data class Korisnik (var id: Int,var ime: String,var prezime: String,var grad: String,
                     var adresa: String, var tel: String,var email: String, var lozinka: String): Serializable {

    constructor(email: String, lozinka: String) : this(0,"","","","","",email, lozinka)

}