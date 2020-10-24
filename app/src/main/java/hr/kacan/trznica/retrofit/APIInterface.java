package hr.kacan.trznica.retrofit;

import java.util.List;

import hr.kacan.trznica.models.Korisnik;
import hr.kacan.trznica.models.Ponuda;

import hr.kacan.trznica.models.ResponseKorisnik;
import hr.kacan.trznica.models.ResponsePonuda;
import hr.kacan.trznica.models.TipProizvoda;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Sučelje za poziv API-a pomoću retrofita
 *
 * @author Kristijan Kačan
 * @since prosinac, 2019.
 */
public interface APIInterface {

    @GET("ponuda")
    Call<List<Ponuda>> getPonuda(@Query("id") long id);

    @GET("tip_proizvoda")
    Call<List<TipProizvoda>> getTipProizvoda();

    //@FormUrlEncoded
    @POST("korisnik")
    Call<ResponseKorisnik> addKorisnik(@Body Korisnik korisnik);

    @POST("korisnik_update")
    Call<ResponseKorisnik> updateKorisnik(@Body Korisnik korisnik);

   /* @GET("korisnik")
    Call<List<Korisnik>> getKorisnik(@Query("email") String email, @Query("lozinka") String lozinka);*/


    @POST("login")
    Call<ResponseKorisnik> loginKorisnik(@Body Korisnik korisnik);

    @Multipart
    @POST("ponuda")
    Call<ResponsePonuda> addPonuda(@Part MultipartBody.Part file, @Part("ponuda") Ponuda ponuda);

    @POST("delete")
    Call<ResponsePonuda> delPonuda(@Body Ponuda ponuda);

}
