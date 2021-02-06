package hr.kacan.trznica.repository

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import hr.kacan.trznica.models.Korisnik
import hr.kacan.trznica.models.ResponseKorisnik
import hr.kacan.trznica.retrofit.APIInterface
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
class KorisnikRepository @Inject constructor(
        private val  apiInterface: APIInterface
){
    suspend fun registerKorisnik(korisnik: Korisnik): ResponseKorisnik = apiInterface.addKorisnik(korisnik)

    suspend fun loginKorisnik(korisnik: Korisnik): ResponseKorisnik = apiInterface.loginKorisnik(korisnik)

    suspend fun updateKorisnik(korisnik: Korisnik): ResponseKorisnik = apiInterface.updateKorisnik(korisnik)
}