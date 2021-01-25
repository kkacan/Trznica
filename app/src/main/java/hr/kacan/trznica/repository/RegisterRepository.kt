package hr.kacan.trznica.repository

import androidx.lifecycle.MutableLiveData
import hr.kacan.trznica.App
import hr.kacan.trznica.conf.Constants
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.models.ResponseKorisnik
import hr.kacan.trznica.retrofit.APIClient
import hr.kacan.trznica.retrofit.APIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegisterRepository {

    private val apiInterface: APIInterface

    fun registerKorisnik(korisnik: Korisnik): MutableLiveData<ResponseKorisnik> {
        val korisnikData = MutableLiveData<ResponseKorisnik>()
        apiInterface.addKorisnik(korisnik).enqueue(object : Callback<ResponseKorisnik> {
            override fun onResponse(call: Call<ResponseKorisnik>, response: Response<ResponseKorisnik>) {
                if (response.body() != null) {
                    korisnikData.value = response.body()
                }
            }
            override fun onFailure(call: Call<ResponseKorisnik>, t: Throwable?) {
            }
        })
        return korisnikData
    }

    fun updateKorisnik(korisnik: Korisnik): MutableLiveData<ResponseKorisnik> {
        val korisnikData = MutableLiveData<ResponseKorisnik>()
        apiInterface.updateKorisnik(korisnik).enqueue(object : Callback<ResponseKorisnik> {
            override fun onResponse(call: Call<ResponseKorisnik>, response: Response<ResponseKorisnik>) {
                if (response.body() != null) {
                    korisnikData.value = response.body()
                    if (Objects.requireNonNull(korisnikData.value)?.response == Constants.RESPONSE_SUCCESS) {
                        App.KORISNIK = response.body()!!.korisnik
                    }
                }
            }
            override fun onFailure(call: Call<ResponseKorisnik?>?, t: Throwable?) {
            }
        })
        return korisnikData
    }

    init {
        val client = APIClient.getClient()
        apiInterface = client?.create(APIInterface::class.java)!!
    }
}