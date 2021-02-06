package hr.kacan.trznica.repository

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import hr.kacan.trznica.App
import hr.kacan.trznica.models.Ponuda
import hr.kacan.trznica.models.ResponsePonuda
import hr.kacan.trznica.retrofit.APIInterface
import okhttp3.MultipartBody
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
class PonudaRepository @Inject constructor(
        private val  apiInterface: APIInterface
){
    suspend fun getPonuda(id: Long, search: String): MutableList<Ponuda> = apiInterface.getPonuda(id, App.KORISNIK.email, search)

    suspend fun addPonuda(image: MultipartBody.Part?, ponuda: Ponuda): ResponsePonuda = apiInterface.addPonuda(image, ponuda)

    suspend fun delPonuda(ponuda: Ponuda): ResponsePonuda = apiInterface.delPonuda(ponuda)
}