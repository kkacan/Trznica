package hr.kacan.trznica

import android.app.Application
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.models.TipProizvoda
import java.util.*

class App: Application() {

    companion object {
        var TIP_PROIZVODA_LIST: MutableList<TipProizvoda> = ArrayList()
        var TIP_PROIZVODA_ID: Long = 0
        var TIP_PROIZVODA_NAZIV: String = ""
        lateinit var KORISNIK: Korisnik
    }
}