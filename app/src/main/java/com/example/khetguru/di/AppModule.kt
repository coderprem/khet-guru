package com.example.khetguru.di

import android.content.Context
import com.example.khetguru.data.api.CropApiService
import com.example.khetguru.data.api.DiseaseApiService
import com.example.khetguru.data.api.MarketPriceApi
import com.example.khetguru.data.api.OpenWeatherApi
import com.example.khetguru.data.api.WeatherApi
import com.example.khetguru.data.repository.CropRecommendationRepositoryImpl
import com.example.khetguru.data.repository.CropRepositoryImpl
import com.example.khetguru.data.repository.DiseaseRepositoryImpl
import com.example.khetguru.data.repository.MarketPriceRepositoryImpl
import com.example.khetguru.data.repository.WeatherRepositoryImpl
import com.example.khetguru.data.service.LocationService
import com.example.khetguru.domain.repository.CropRecommendationRepository
import com.example.khetguru.domain.repository.CropRepository
import com.example.khetguru.domain.repository.DiseaseRepository
import com.example.khetguru.domain.repository.MarketPriceRepository
import com.example.khetguru.domain.repository.WeatherRepository
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- Firebase & Auth Providers ---
    @Provides
    @Singleton
    fun provideFirebaseApp(context: Context): FirebaseApp {
        return FirebaseApp.initializeApp(context)
            ?: throw IllegalStateException("Firebase initialization failed")
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideOneTapClient(@ApplicationContext context: Context): SignInClient =
        Identity.getSignInClient(context)

    // --- Retrofit Base URLs ---
    @Provides @Named("weatherApiBaseUrl")
    fun provideWeatherApiBaseUrl(): String = "https://api.weatherapi.com/v1/"

    @Provides @Named("openWeatherBaseUrl")
    fun provideOpenWeatherBaseUrl(): String = "https://api.openweathermap.org/"

    @Provides @Named("priceApiBaseUrl")
    fun providePriceApiBaseUrl(): String = "https://api.data.gov.in/resource/"

    @Provides @Named("cropApiBaseUrl")
    fun provideCropApiBaseUrl(): String =
        "https://crop-recommendation-481186284624.asia-south1.run.app/"

    @Provides @Named("cropIdentifyBaseUrl")
    fun provideCropIdentificationBaseUrl(): String = "https://crop.kindwise.com/"

    // --- Retrofit Helpers ---
    private fun createRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private inline fun <reified T> createApi(retrofit: Retrofit): T = retrofit.create(T::class.java)

    // --- Retrofit Instances ---
    @Provides
    @Singleton
    @Named("weatherApiRetrofit")
    fun provideWeatherApiRetrofit(@Named("weatherApiBaseUrl") baseUrl: String): Retrofit =
        createRetrofit(baseUrl)

    @Provides
    @Singleton
    @Named("openWeatherRetrofit")
    fun provideOpenWeatherRetrofit(@Named("openWeatherBaseUrl") baseUrl: String): Retrofit =
        createRetrofit(baseUrl)

    @Provides
    @Singleton
    @Named("priceApiRetrofit")
    fun providePriceApiRetrofit(@Named("priceApiBaseUrl") baseUrl: String): Retrofit =
        createRetrofit(baseUrl)

    @Provides
    @Singleton
    @Named("cropApiRetrofit")
    fun provideCropApiRetrofit(@Named("cropApiBaseUrl") baseUrl: String): Retrofit =
        createRetrofit(baseUrl)

    @Provides
    @Singleton
    @Named("cropDiseaseRetrofit")
    fun provideCropDiseaseRetrofit(@Named("cropIdentifyBaseUrl") baseUrl: String): Retrofit =
        createRetrofit(baseUrl)


    // --- API Interfaces ---
    @Provides
    fun provideWeatherApi(@Named("weatherApiRetrofit") retrofit: Retrofit): WeatherApi =
        createApi(retrofit)

    @Provides
    fun provideOpenWeatherApi(@Named("openWeatherRetrofit") retrofit: Retrofit): OpenWeatherApi =
        createApi(retrofit)

    @Provides
    fun provideMarketPriceApi(@Named("priceApiRetrofit") retrofit: Retrofit): MarketPriceApi =
        createApi(retrofit)

    @Provides
    fun provideCropApi(@Named("cropApiRetrofit") retrofit: Retrofit): CropApiService =
        createApi(retrofit)

    @Provides
    fun provideDiseaseApiService(@Named("cropDiseaseRetrofit") retrofit: Retrofit): DiseaseApiService =
        createApi(retrofit)

    // --- Repositories ---
    @Provides
    fun provideWeatherRepository(
        weatherApi: WeatherApi,
        openWeatherApi: OpenWeatherApi
    ): WeatherRepository = WeatherRepositoryImpl(weatherApi, openWeatherApi)

    @Provides
    fun provideMarketPriceRepository(marketPriceApi: MarketPriceApi): MarketPriceRepository =
        MarketPriceRepositoryImpl(marketPriceApi)

    @Provides
    fun provideCropRepository(firestore: FirebaseFirestore): CropRepository =
        CropRepositoryImpl(firestore)

    @Provides
    fun provideCropRecommendationRepository(cropApiService: CropApiService): CropRecommendationRepository =
        CropRecommendationRepositoryImpl(cropApiService)

    @Provides
    fun provideDiseaseRepository(diseaseApiService: DiseaseApiService): DiseaseRepository {
        return DiseaseRepositoryImpl(diseaseApiService)
    }

    // --- Location & Disease Services ---
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    @Provides
    @Singleton
    fun provideLocationService(
        fusedLocationClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationService = LocationService(fusedLocationClient, context)
}
