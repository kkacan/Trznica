package hr.kacan.trznica.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.models.ResponseKorisnik;
import hr.kacan.trznica.retrofit.APIClient;
import hr.kacan.trznica.retrofit.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterRepository {

    private static RegisterRepository registerRepository;

    public static RegisterRepository getInstance(){
        if (registerRepository == null){
            registerRepository = new RegisterRepository();
        }
        return registerRepository;
    }

    private APIInterface apiInterface;

    public RegisterRepository(){
        Retrofit client = APIClient.getClient();
        apiInterface = client.create(APIInterface.class);
    }

    public MutableLiveData<ResponseKorisnik> registerKorisnik(Korisnik korisnik){
        final MutableLiveData<ResponseKorisnik> korisnikData = new MutableLiveData<>();
        apiInterface.addKorisnik(korisnik).enqueue(new Callback<ResponseKorisnik>() {
            @Override
            public void onResponse(Call<ResponseKorisnik> call, Response<ResponseKorisnik> response) {

                if (response.body() != null){
                    korisnikData.setValue(response.body());
                }
                /*ResponseKorisnik responseKorisnik = new ResponseKorisnik();
                responseKorisnik.setResponse(response.body().getResponse());
                responseKorisnik.setKorisnik(response.body().getKorisnik());
                Gson gson = new Gson();
                String s = gson.toJson(response.body());
                System.out.println("GOTCHA "+ s);
                System.out.println("GOTCHA "+ responseKorisnik.getResponse() + responseKorisnik.getKorisnik());*/
            }

            @Override
            public void onFailure(Call<ResponseKorisnik> call, Throwable t) {
                System.out.println("GOTCHA FAIL "+ t);
            }
        });
        return korisnikData;
    }

    public MutableLiveData<ResponseKorisnik> updateKorisnik(Korisnik korisnik){
        final MutableLiveData<ResponseKorisnik> korisnikData = new MutableLiveData<>();
        apiInterface.updateKorisnik(korisnik).enqueue(new Callback<ResponseKorisnik>() {
            @Override
            public void onResponse(Call<ResponseKorisnik> call, Response<ResponseKorisnik> response) {

                if (response.body() != null){
                    korisnikData.setValue(response.body());
                    if (Objects.requireNonNull(korisnikData.getValue()).getResponse().equals(Constants.RESPONSE_SUCCESS)){
                        Constants.KORISNIK = response.body().getKorisnik();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseKorisnik> call, Throwable t) {
                System.out.println("GOTCHA FAIL "+ t);
            }
        });
        return korisnikData;
    }
}
