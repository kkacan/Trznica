package hr.kacan.trznica.repository

import androidx.lifecycle.MutableLiveData
import hr.kacan.trznica.App
import hr.kacan.trznica.models.TipProizvoda
import hr.kacan.trznica.retrofit.APIClient
import hr.kacan.trznica.retrofit.APIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TipRepository {

    private val apiInterface: APIInterface

    fun getTipProizvoda(): MutableLiveData<MutableList<TipProizvoda>> {
        val tipProizvodaData = MutableLiveData<MutableList<TipProizvoda>>()
        apiInterface.getTipProizvoda(App.KORISNIK.email).enqueue(object : Callback<MutableList<TipProizvoda>> {
            override fun onResponse(call: Call<MutableList<TipProizvoda>>, response: Response<MutableList<TipProizvoda>>) {
                if (response.isSuccessful) {
                    tipProizvodaData.value = response.body()
                }
            }
            override fun onFailure(call: Call<MutableList<TipProizvoda>>, t: Throwable) {
                tipProizvodaData.value = null
            }
        })
        return tipProizvodaData
    }

    init {
        val client = APIClient.getClient()
        apiInterface = client!!.create(APIInterface::class.java)
    }
}