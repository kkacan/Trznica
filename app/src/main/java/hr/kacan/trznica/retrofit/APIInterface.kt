package hr.kacan.trznica.retrofit

import hr.kacan.trznica.models.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Sučelje za poziv API-a pomoću retrofita
 *
 * @author Kristijan Kačan
 * @since prosinac, 2019.
 */
interface APIInterface {
    @GET("ponuda")
    fun getPonuda(@Query("id") id: Long, @Query("apikey") apikey: String, @Query("search") search: String): Call<MutableList<Ponuda>>

    @Multipart
    @POST("ponuda")
    fun addPonuda(@Part file: MultipartBody.Part?, @Part("ponuda") ponuda: Ponuda): Call<ResponsePonuda>

    @HTTP(method = "DELETE", path = "ponuda", hasBody = true)
    fun delPonuda(@Body ponuda: Ponuda): Call<ResponsePonuda>

    @GET("tip_proizvoda")
    fun getTipProizvoda(@Query("apikey") apikey: String): Call<MutableList<TipProizvoda>>

    @POST("korisnik")
    fun addKorisnik(@Body korisnik: Korisnik): Call<ResponseKorisnik>

    @PUT("korisnik")
    fun updateKorisnik(@Body korisnik: Korisnik): Call<ResponseKorisnik>

    @POST("login")
    fun loginKorisnik(@Body korisnik: Korisnik): Call<ResponseKorisnik>

}