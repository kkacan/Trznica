package hr.kacan.trznica.retrofit

import android.util.Log
import android.widget.Toast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import hr.kacan.trznica.App
import hr.kacan.trznica.conf.Constants
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Klasa retrofit klijenta
 *
 * @author Kristijan Kačan
 * @since Listopad, 2020.
 */
@Module
@InstallIn(ApplicationComponent::class)
object APIClient {

    @Provides
    @Singleton
    fun getClient(): Retrofit =
            Retrofit.Builder()
                    .baseUrl(Constants.API_BASE_URL)
                    .client(
                            OkHttpClient.Builder()
                                    .connectTimeout(10L, TimeUnit.SECONDS)
                                    .readTimeout(10L, TimeUnit.SECONDS)
                                    .writeTimeout(10L, TimeUnit.SECONDS)
                                    .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

    @Singleton
    fun Throwable.printRetrofitError() {
        Toast.makeText(App.appContext, "Greška u spajanju na poslužitelj! ${this.message}", Toast.LENGTH_LONG).show()
        this.printStackTrace()
        when (this) {

            is IOException -> Log.e(
                    this::class.java.simpleName,
                    "Network Error happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
            is HttpException -> Log.e(
                    this::class.java.simpleName,
                    "HTTP Exception happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
            else -> Log.e(
                    this::class.java.simpleName,
                    "Unknown Error happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
        }
    }

}