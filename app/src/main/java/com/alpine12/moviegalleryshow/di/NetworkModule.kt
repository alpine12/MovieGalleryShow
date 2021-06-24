package com.alpine12.moviegalleryshow.di

import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = when (BuildConfig.DEBUG) {
                true -> HttpLoggingInterceptor.Level.BODY
                false -> HttpLoggingInterceptor.Level.NONE
            }
        }
    }


    @Provides
    @Singleton
    fun providesApiKey(): Interceptor = Interceptor { chain ->
        var request: Request = chain.request()
        val url: HttpUrl = request.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.apiKey)
            .build()
        request = request.newBuilder().url(url).build()

        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKey: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKey)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


    @Provides
    @Singleton
    fun providesApiEndPoint(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

//    @Provides
//    @Singleton
//    fun providesLoggingInterceptor(): HttpLoggingInterceptor =
//        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//
//    @Provides
//    @Singleton
//    fun providesOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient =
//        OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .build()
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(client: OkHttpClient): Retrofit =
//        Retrofit.Builder()
//            .baseUrl(BuildConfig.baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//
//    @Provides
//    @Singleton
//    fun provideUnsplashApi(retrofit: Retrofit): ApiService =
//        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesString(): String = "Ini String"


}