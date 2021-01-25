package hr.kacan.trznica.retrofit

import hr.kacan.trznica.conf.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Klasa za postavljanje retrofit klijenta
 *
 * @author Kristijan Kačan
 * @since Listopad, 2020.
 */
object APIClient {
    private var retrofit: Retrofit? = null
    fun getClient(): Retrofit? {
        val client = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit
    }
}