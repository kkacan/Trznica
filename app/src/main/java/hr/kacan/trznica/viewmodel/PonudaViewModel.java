package hr.kacan.trznica.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hr.kacan.trznica.models.ResponsePonuda;
import hr.kacan.trznica.models.TipProizvoda;
import hr.kacan.trznica.repository.PonudaRepository;
import hr.kacan.trznica.models.Ponuda;
import hr.kacan.trznica.repository.TipRepository;
import okhttp3.MultipartBody;


public class PonudaViewModel extends AndroidViewModel {


    private MutableLiveData<List<Ponuda>> mutableLiveDataPonude;
    private MutableLiveData<List<TipProizvoda>> mutableLiveDataTipProizvoda;
    private MutableLiveData<ResponsePonuda> mutableLiveDataResponsPonude;
    private PonudaRepository ponudaRepository;
    private TipRepository tipRepository;


    private Ponuda ponuda;
    private TipProizvoda tipProizvoda;

    public Ponuda getPonuda() {
        return ponuda;
    }

    public void setPonuda(Ponuda ponuda) {
        this.ponuda = ponuda;
    }

    public TipProizvoda getTipProizvoda() {
        return tipProizvoda;
    }

    public void setTipProizvoda(TipProizvoda tipProizvoda) {
        this.tipProizvoda = tipProizvoda;
    }

    public PonudaViewModel(Application application) {
        super(application);

        ponudaRepository = PonudaRepository.getInstance();
        tipRepository = TipRepository.getInstance();

    }

    public LiveData<List<Ponuda>> getPonude(long id) {

        mutableLiveDataPonude = ponudaRepository.getPonuda(id);
        return mutableLiveDataPonude;
    }

    /*public LiveData<List<TipProizvoda>> getTipProizvodaData() {

        mutableLiveDataTipProizvoda = tipRepository.getTipProizvoda();
        return mutableLiveDataTipProizvoda;
    }*/

    public LiveData<ResponsePonuda> addPonuda(MultipartBody.Part image, Ponuda ponuda) {
        mutableLiveDataResponsPonude = ponudaRepository.addPonuda(image, ponuda);
        return mutableLiveDataResponsPonude;
    }

    public LiveData<ResponsePonuda> delPonuda(Ponuda ponuda) {
        mutableLiveDataResponsPonude = ponudaRepository.delPonuda(ponuda);
        return mutableLiveDataResponsPonude;
    }

    public void editPonuda() {

        //ploviloDAO.promjeniPlovilo(plovilo);
    }

    public void deletePonuda() {

        //ploviloDAO.obrisiPlovilo(plovilo);
    }

    /*public void uploadPhoto(MultipartBody.Part image, Ponuda ponuda){
        ponudaRepository.addPonuda(image, ponuda);
    }*/

}
