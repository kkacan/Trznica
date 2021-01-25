package hr.kacan.trznica.repository

import androidx.lifecycle.MutableLiveData
import hr.kacan.trznica.App
import hr.kacan.trznica.models.Ponuda
import hr.kacan.trznica.models.ResponsePonuda
import hr.kacan.trznica.retrofit.APIClient
import hr.kacan.trznica.retrofit.APIInterface
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PonudaRepository {

    private val apiInterface: APIInterface?
    fun getPonuda(id: Long, search: String): MutableLiveData<MutableList<Ponuda>> {
        val ponudaData = MutableLiveData<MutableList<Ponuda>>()
        apiInterface?.getPonuda(id, App.KORISNIK.email, search)?.enqueue(object : Callback<MutableList<Ponuda>> {
            override fun onResponse(call: Call<MutableList<Ponuda>>, response: Response<MutableList<Ponuda>>) {
                if (response.isSuccessful) {
                    ponudaData.value = response.body()
                }
            }
            override fun onFailure(call: Call<MutableList<Ponuda>>, t: Throwable) {
                ponudaData.value = null
            }
        })
        return ponudaData
    }

    fun addPonuda(image: MultipartBody.Part?, ponuda: Ponuda): MutableLiveData<ResponsePonuda> {
        val mutableLiveDataResponsPonude = MutableLiveData<ResponsePonuda>()
        apiInterface?.addPonuda(image, ponuda)?.enqueue(object : Callback<ResponsePonuda> {
            override fun onResponse(call: Call<ResponsePonuda>, response: Response<ResponsePonuda>) {
                if (response.body() != null) {
                    mutableLiveDataResponsPonude.value = response.body()
                }
            }
            override fun onFailure(call: Call<ResponsePonuda>, t: Throwable) {
            }
        })
        return mutableLiveDataResponsPonude
    }

    fun delPonuda(ponuda: Ponuda): MutableLiveData<ResponsePonuda> {
        val mutableLiveDataResponsPonude = MutableLiveData<ResponsePonuda>()
        apiInterface?.delPonuda(ponuda)?.enqueue(object : Callback<ResponsePonuda> {
            override fun onResponse(call: Call<ResponsePonuda>, response: Response<ResponsePonuda>) {
                if (response.body() != null) {
                    mutableLiveDataResponsPonude.value = response.body()
                }
            }
            override fun onFailure(call: Call<ResponsePonuda>, t: Throwable) {
            }
        })
        return mutableLiveDataResponsPonude
    }

    init {
        val client = APIClient.getClient()
        apiInterface = client?.create(APIInterface::class.java)
    }
}