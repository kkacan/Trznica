package hr.kacan.trznica.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.kacan.trznica.models.TipProizvoda
import hr.kacan.trznica.repository.TipRepository
import hr.kacan.trznica.retrofit.APIClient.printRetrofitError
import kotlinx.coroutines.launch

class TipViewModel @ViewModelInject constructor(
        private val tipRepository: TipRepository
) : ViewModel() {

    private val _tipProizvoda = MutableLiveData<MutableList<TipProizvoda>>()
    val tipProizvoda: LiveData<MutableList<TipProizvoda>> = _tipProizvoda

    fun getTipProizvodaData() = viewModelScope.launch {
        try {
            _tipProizvoda.postValue( tipRepository.getTipProizvoda())
        } catch (e: Exception) {
            e.printRetrofitError()
        }
    }
}