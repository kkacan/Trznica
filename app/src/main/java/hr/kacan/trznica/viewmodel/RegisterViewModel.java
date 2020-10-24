package hr.kacan.trznica.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.models.ResponseKorisnik;
import hr.kacan.trznica.repository.RegisterRepository;


public class RegisterViewModel extends AndroidViewModel {


    private MutableLiveData<ResponseKorisnik> mutableLiveData;
    private RegisterRepository registerRepository;


    private Korisnik korisnik;

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public RegisterViewModel(Application application) {
        super(application);
        registerRepository = RegisterRepository.getInstance();
    }

    public LiveData<ResponseKorisnik> registerKorisnik(Korisnik korisnik) {
        mutableLiveData = registerRepository.registerKorisnik(korisnik);
        return mutableLiveData;
    }

    public void getKorisnici() {

        //ploviloDAO.dodajNovoPlovilo(plovilo);
    }

    public LiveData<ResponseKorisnik> editKorisnik(Korisnik korisnik) {
        mutableLiveData = registerRepository.updateKorisnik(korisnik);
        return mutableLiveData;
    }

    public void deleteKorisnik() {

        //ploviloDAO.obrisiPlovilo(plovilo);
    }

}
