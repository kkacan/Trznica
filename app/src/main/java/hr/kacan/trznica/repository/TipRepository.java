package hr.kacan.trznica.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hr.kacan.trznica.models.TipProizvoda;
import hr.kacan.trznica.retrofit.APIClient;
import hr.kacan.trznica.retrofit.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TipRepository {

    private static TipRepository tipRepository;

    public static TipRepository getInstance(){
        if (tipRepository == null){
            tipRepository = new TipRepository();
        }
        return tipRepository;
    }

    private APIInterface apiInterface;

    public TipRepository(){
        Retrofit client = APIClient.getClient();
        apiInterface = client.create(APIInterface.class);
    }


    public MutableLiveData<List<TipProizvoda>> getTipProizvoda(){
        final MutableLiveData<List<TipProizvoda>> tipProizvodaData = new MutableLiveData<>();
        apiInterface.getTipProizvoda().enqueue(new Callback<List<TipProizvoda>>() {

            @Override
            public void onResponse(Call<List<TipProizvoda>> call, Response<List<TipProizvoda>> response) {

                if (response.isSuccessful()){
                    tipProizvodaData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<TipProizvoda>> call, Throwable t) {
                tipProizvodaData.setValue(null);
                System.out.println("GOTCHA T "+t);
            }

        });
        return tipProizvodaData;
    }



}
