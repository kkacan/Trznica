package hr.kacan.trznica.models

import java.io.Serializable

data class TipProizvoda(var id: Int, var naziv: String, var slika: String) : Serializable{

    override fun toString(): String {
        return naziv
    }

}