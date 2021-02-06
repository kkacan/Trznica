package hr.kacan.trznica

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.models.Ponuda
import hr.kacan.trznica.models.TipProizvoda
import java.util.*

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var TIP_PROIZVODA_LIST: MutableList<TipProizvoda> = ArrayList()
        var TIP_PROIZVODA_ID: Long = 0
        var TIP_PROIZVODA_NAZIV: String = ""
        lateinit var KORISNIK: Korisnik
        lateinit var PONUDA: Ponuda
        lateinit var appContext: Context
    }
}