package hr.kacan.trznica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.kacan.trznica.models.TipProizvoda
import hr.kacan.trznica.repository.TipRepository

class TipViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var mutableLiveDataTipProizvoda: MutableLiveData<MutableList<TipProizvoda>>
    private var tipRepository: TipRepository = TipRepository()

    fun getTipProizvodaData(): LiveData<MutableList<TipProizvoda>> {
        mutableLiveDataTipProizvoda = tipRepository.getTipProizvoda()
        return mutableLiveDataTipProizvoda
    }

}