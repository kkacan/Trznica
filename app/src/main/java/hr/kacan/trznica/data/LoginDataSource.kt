package hr.kacan.trznica.data

import androidx.lifecycle.MutableLiveData
import hr.kacan.trznica.App
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.models.ResponseKorisnik
import hr.kacan.trznica.retrofit.APIClient
import hr.kacan.trznica.retrofit.APIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    fun login(username: String, password: String): MutableLiveData<Result<Korisnik>> {
        val result = MutableLiveData<Result<Korisnik>>()
        try {
            val loginKorisnik = Korisnik(username, password)
            val client = APIClient.getClient()
            val apiInterface = client?.create(APIInterface::class.java)
            apiInterface?.loginKorisnik(loginKorisnik)?.enqueue(object : Callback<ResponseKorisnik> {
                override fun onResponse(call: Call<ResponseKorisnik>, response: Response<ResponseKorisnik>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            if (response.body()?.korisnik != null) {
                                App.KORISNIK = response.body()!!.korisnik
                                result.setValue(Result.Success(App.KORISNIK))
                            } else {
                                result.setValue(Result.Error(IOException(response.body()!!.response)))
                            }
                        } else {
                            result.setValue(Result.Error(IOException("Error logging in")))
                        }
                    } else {
                        result.setValue(Result.Error(IOException("Error logging in")))
                    }
                }

                override fun onFailure(call: Call<ResponseKorisnik>, t: Throwable?) {
                    result.value = Result.Error(IOException("Error logging in"))
                }
            })
        } catch (e: Exception) {
            result.value = Result.Error(IOException("Error logging in", e))
        }
        return result
    }

    fun logout() {
        // TODO: revoke authentication
    }
}