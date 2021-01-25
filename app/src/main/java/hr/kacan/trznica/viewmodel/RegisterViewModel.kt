package hr.kacan.trznica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.models.ResponseKorisnik
import hr.kacan.trznica.repository.RegisterRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var mutableLiveData: MutableLiveData<ResponseKorisnik>
    private val registerRepository: RegisterRepository = RegisterRepository()

    fun registerKorisnik(korisnik: Korisnik): LiveData<ResponseKorisnik> {
        mutableLiveData = registerRepository.registerKorisnik(korisnik)
        return mutableLiveData
    }

    fun editKorisnik(korisnik: Korisnik): LiveData<ResponseKorisnik> {
        mutableLiveData = registerRepository.updateKorisnik(korisnik)
        return mutableLiveData
    }

}