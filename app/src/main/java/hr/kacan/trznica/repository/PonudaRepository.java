package hr.kacan.trznica.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import hr.kacan.trznica.models.Ponuda;
import hr.kacan.trznica.models.ResponsePonuda;
import hr.kacan.trznica.models.TipProizvoda;
import hr.kacan.trznica.retrofit.APIClient;
import hr.kacan.trznica.retrofit.APIInterface;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PonudaRepository {

    private static PonudaRepository ponudaRepository;

    public static PonudaRepository getInstance(){
        if (ponudaRepository == null){
            ponudaRepository = new PonudaRepository();
        }
        return ponudaRepository;
    }

    private APIInterface apiInterface;

    public PonudaRepository(){
        Retrofit client = APIClient.getClient();
        apiInterface = client.create(APIInterface.class);
    }

    public MutableLiveData<List<Ponuda>> getPonuda(long id){
        final MutableLiveData<List<Ponuda>> ponudaData = new MutableLiveData<>();
        apiInterface.getPonuda(id).enqueue(new Callback<List<Ponuda>>() {

            @Override
            public void onResponse(Call<List<Ponuda>> call, Response<List<Ponuda>> response) {

                if (response.isSuccessful()){
                    ponudaData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Ponuda>> call, Throwable t) {
                ponudaData.setValue(null);
            }

        });
        return ponudaData;
    }


    public MutableLiveData<ResponsePonuda> addPonuda(MultipartBody.Part image, Ponuda ponuda){

        final MutableLiveData<ResponsePonuda> mutableLiveDataResponsPonude = new MutableLiveData<>();
        //RequestBody naziv = RequestBody.create(MediaType.parse("text/plain"), ponuda.getNaziv());

        apiInterface.addPonuda(image, ponuda).enqueue(new Callback<ResponsePonuda>() {
            @Override
            public void onResponse(Call<ResponsePonuda> call, Response<ResponsePonuda> response) {
                //System.out.println("GOTCHA "+response.message());

                if (response.body() != null) {
                    //System.out.println("GOTCHA "+ response.body().getResponseImage());
                    mutableLiveDataResponsPonude.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponsePonuda> call, Throwable t) {

                System.out.println("GOTCHA FAIL "+t);

            }
        });
        return mutableLiveDataResponsPonude;
    }

    public MutableLiveData<ResponsePonuda> delPonuda(Ponuda ponuda){

        final MutableLiveData<ResponsePonuda> mutableLiveDataResponsPonude = new MutableLiveData<>();


        apiInterface.delPonuda(ponuda).enqueue(new Callback<ResponsePonuda>() {
            @Override
            public void onResponse(Call<ResponsePonuda> call, Response<ResponsePonuda> response) {

                if (response.body() != null) {
                    mutableLiveDataResponsPonude.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponsePonuda> call, Throwable t) {

                System.out.println("GOTCHA FAIL "+t);

            }
        });
        return mutableLiveDataResponsPonude;
    }

}
