package hr.kacan.trznica.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import hr.kacan.trznica.models.Ponuda
import hr.kacan.trznica.models.ResponsePonuda
import hr.kacan.trznica.repository.PonudaRepository
import hr.kacan.trznica.retrofit.APIClient.printRetrofitError
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PonudaViewModel @ViewModelInject constructor(
        private val ponudaRepository: PonudaRepository
) : ViewModel() {

    private val _ponude = MutableLiveData<MutableList<Ponuda>>()
    val ponude: LiveData<MutableList<Ponuda>> = _ponude
    private val _responsePonuda = MutableLiveData<ResponsePonuda>()
    var responsePonuda: LiveData<ResponsePonuda> = _responsePonuda

    fun getPonude(id: Long, search: String) = viewModelScope.launch {
        try {
            _ponude.postValue(ponudaRepository.getPonuda(id, search))
        } catch (e: Exception) {
            e.printRetrofitError()
        }
    }
    fun addPonuda(image: MultipartBody.Part?, ponuda: Ponuda){
        responsePonuda = liveData {
            try {
                emit(ponudaRepository.addPonuda(image, ponuda))
            } catch (e: Exception) {
                e.printRetrofitError()
            }
        }
    }
    fun delPonuda(ponuda: Ponuda){
        responsePonuda = liveData {
            try {
                emit(ponudaRepository.delPonuda(ponuda))
            } catch (e: Exception) {
                e.printRetrofitError()
            }
        }
    }
}