package hr.kacan.trznica.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hr.kacan.trznica.models.Ponuda;
import hr.kacan.trznica.models.ResponsePonuda;
import hr.kacan.trznica.models.TipProizvoda;
import hr.kacan.trznica.repository.PonudaRepository;
import hr.kacan.trznica.repository.TipRepository;
import okhttp3.MultipartBody;


public class TipViewModel extends AndroidViewModel {



    private MutableLiveData<List<TipProizvoda>> mutableLiveDataTipProizvoda;

    private TipRepository tipRepository;

    private TipProizvoda tipProizvoda;


    public TipProizvoda getTipProizvoda() {
        return tipProizvoda;
    }

    public void setTipProizvoda(TipProizvoda tipProizvoda) {
        this.tipProizvoda = tipProizvoda;
    }

    public TipViewModel(Application application) {
        super(application);

        tipRepository = TipRepository.getInstance();

    }

    public LiveData<List<TipProizvoda>> getTipProizvodaData() {

        mutableLiveDataTipProizvoda = tipRepository.getTipProizvoda();
        return mutableLiveDataTipProizvoda;
    }





}
