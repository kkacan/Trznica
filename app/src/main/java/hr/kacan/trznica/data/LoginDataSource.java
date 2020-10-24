package hr.kacan.trznica.data;

import androidx.lifecycle.MutableLiveData;

import hr.kacan.trznica.conf.Constants;
import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.models.Ponuda;
import hr.kacan.trznica.models.ResponseKorisnik;
import hr.kacan.trznica.retrofit.APIClient;
import hr.kacan.trznica.retrofit.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.io.IOException;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public MutableLiveData<Result<Korisnik>> login(String username, String password) {
        final MutableLiveData<Result<Korisnik>> result = new MutableLiveData<>();

        try {

            Constants.KORISNIK = null;
            Korisnik loginKorisnik = new Korisnik(username, password);

            Retrofit client = APIClient.getClient();
            APIInterface apiInterface = client.create(APIInterface.class);
            apiInterface.loginKorisnik(loginKorisnik).enqueue(new Callback<ResponseKorisnik>() {

                @Override
                public void onResponse(Call<ResponseKorisnik> call, Response<ResponseKorisnik> response) {

                    if (response.isSuccessful()){

                        if (response.body() != null) {
                            if (response.body().getKorisnik() != null) {

                                Constants.KORISNIK = response.body().getKorisnik();

                                result.setValue(new Result.Success<>(response.body().getKorisnik()));
                            } else {
                                result.setValue(new Result.Error(new IOException("Error logging in")));
                            }
                        } else {
                            result.setValue(new Result.Error(new IOException("Error logging in")));
                        }
                    } else {
                        result.setValue(new Result.Error(new IOException("Error logging in")));
                    }
                }

                @Override
                public void onFailure(Call<ResponseKorisnik> call, Throwable t) {
                    result.setValue(new Result.Error(new IOException("Error logging in")));
                }
            });

        } catch (Exception e) {
            System.out.println("GOTCHA FAIL E "+e);
            result.setValue(new Result.Error(new IOException("Error logging in", e)));
        }

        return result;
    }

    public void logout() {
        // TODO: revoke authentication
    }
}