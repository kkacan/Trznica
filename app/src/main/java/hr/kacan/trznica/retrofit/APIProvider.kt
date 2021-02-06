package hr.kacan.trznica.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object APIProvider {

    @Provides
    @Singleton
    fun provideAPIInterface(): APIInterface = APIClient.getClient().create(APIInterface::class.java)
}