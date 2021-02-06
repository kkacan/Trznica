package hr.kacan.trznica.retrofit

import hr.kacan.trznica.models.*
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Sučelje za poziv API-a pomoću retrofita
 *
 * @author Kristijan Kačan
 * @since prosinac, 2020.
 */

interface APIInterface {
    @GET("ponuda")
    suspend fun getPonuda(@Query("id") id: Long, @Query("apikey") apikey: String, @Query("search") search: String): MutableList<Ponuda>

    @Multipart
    @POST("ponuda")
    suspend fun addPonuda(@Part file: MultipartBody.Part?, @Part("ponuda") ponuda: Ponuda): ResponsePonuda

    @HTTP(method = "DELETE", path = "ponuda", hasBody = true)
    suspend fun delPonuda(@Body ponuda: Ponuda): ResponsePonuda

    @GET("tip_proizvoda")
    suspend fun getTipProizvoda(@Query("apikey") apikey: String): MutableList<TipProizvoda>

    @POST("korisnik")
    suspend fun addKorisnik(@Body korisnik: Korisnik): ResponseKorisnik

    @PUT("korisnik")
    suspend fun updateKorisnik(@Body korisnik: Korisnik): ResponseKorisnik

    @POST("login")
    suspend fun loginKorisnik(@Body korisnik: Korisnik): ResponseKorisnik

}