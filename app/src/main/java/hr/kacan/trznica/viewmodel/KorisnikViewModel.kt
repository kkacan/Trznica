package hr.kacan.trznica.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.models.ResponseKorisnik
import hr.kacan.trznica.repository.KorisnikRepository
import hr.kacan.trznica.retrofit.APIClient.printRetrofitError

class KorisnikViewModel @ViewModelInject constructor(
        private val korisnikRepository: KorisnikRepository
) : ViewModel() {

    private val _responseKorisnik = MutableLiveData<ResponseKorisnik>()
    var responseKorisnik: LiveData<ResponseKorisnik> = _responseKorisnik

    fun registerKorisnik(korisnik: Korisnik){
        responseKorisnik = liveData {
            try {
                emit(korisnikRepository.registerKorisnik(korisnik))
            } catch (e: Exception) {
                e.printRetrofitError()
            }
        }
    }
    fun editKorisnik(korisnik: Korisnik){
        responseKorisnik = liveData {
            try {
                emit(korisnikRepository.updateKorisnik(korisnik))
            } catch (e: Exception) {
                e.printRetrofitError()
            }
        }
    }

    fun loginKorisnik(korisnik: Korisnik){
        responseKorisnik = liveData {
            try {
                emit(korisnikRepository.loginKorisnik(korisnik))
            } catch (e: Exception) {
                e.printRetrofitError()
            }
        }
    }

}