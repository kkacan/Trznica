package hr.kacan.trznica.repository

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import hr.kacan.trznica.App
import hr.kacan.trznica.models.TipProizvoda
import hr.kacan.trznica.retrofit.APIInterface
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
class TipRepository @Inject constructor(
        private val  apiInterface: APIInterface
){
    suspend fun getTipProizvoda(): MutableList<TipProizvoda> = apiInterface.getTipProizvoda(App.KORISNIK.email)
}