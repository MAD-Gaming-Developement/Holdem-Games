package tg.holdemcasino.tgames.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tg.holdemcasino.tgames.BaseApp
import tg.holdemcasino.tgames.network.datasource.GameApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GlobalModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseApp.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun providesHttpClient() : OkHttpClient{
        val httpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(interceptor)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) : GameApi{
        return retrofit.create(GameApi::class.java)
    }
}