package com.example.randommovie.di

import com.example.randommovie.BuildConfig
import com.example.randommovie.api.service.MovieService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://moviesdatabase.p.rapidapi.com/"
private const val API_KEY = BuildConfig.API_KEY
private const val HOST = BuildConfig.HOST

val networkModule = module {

    single {
        okHttpClientBuilder(API_KEY, HOST)
    }

    single {
        retrofitBuilder(BASE_URL, get(), get(), get())
    }

    single<GsonConverterFactory> {
        GsonConverterFactory.create()
    }

    single<RxJava3CallAdapterFactory> {
        RxJava3CallAdapterFactory.create()
    }

    single<MovieService> {
        createService(get())
    }
}

private fun okHttpClientBuilder(keyValue: String, hostValue: String, ): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("X-RapidAPI-Key", keyValue)
                .header("X-RapidAPI-Host", hostValue)
                .method(originalRequest.method, originalRequest.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

private fun retrofitBuilder(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
    rxJava3CallAdapterFactory: RxJava3CallAdapterFactory
): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(gsonConverterFactory)
    .addCallAdapterFactory(rxJava3CallAdapterFactory)
    .build()

private inline fun <reified T> createService(retrofit: Retrofit): T =
    retrofit.create(T::class.java)