package hr.kacan.trznica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.kacan.trznica.models.Ponuda
import hr.kacan.trznica.models.ResponsePonuda
import hr.kacan.trznica.repository.PonudaRepository

import okhttp3.MultipartBody

class PonudaViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var mutableLiveDataPonude: MutableLiveData<MutableList<Ponuda>>
    private lateinit var mutableLiveDataResponsPonude: MutableLiveData<ResponsePonuda>
    private val ponudaRepository: PonudaRepository = PonudaRepository()
    private lateinit var ponuda: Ponuda
    fun getPonuda(): Ponuda {
        return ponuda
    }

    fun setPonuda(ponuda: Ponuda) {
        this.ponuda = ponuda
    }

    fun getPonude(id: Long, search: String): LiveData<MutableList<Ponuda>> {
        mutableLiveDataPonude = ponudaRepository.getPonuda(id, search)
        return mutableLiveDataPonude
    }

    fun addPonuda(image: MultipartBody.Part?, ponuda: Ponuda): LiveData<ResponsePonuda> {
        mutableLiveDataResponsPonude = ponudaRepository.addPonuda(image, ponuda)
        return mutableLiveDataResponsPonude
    }

    fun delPonuda(ponuda: Ponuda): LiveData<ResponsePonuda> {
        mutableLiveDataResponsPonude = ponudaRepository.delPonuda(ponuda)
        return mutableLiveDataResponsPonude
    }

}