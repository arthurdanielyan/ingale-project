package com.nightx.ingale.feature_yt.yt_core.di

import com.nightx.ingale.feature_yt.yt_core.data.api.YoutubeV3Api
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val youtubeApiModule = module {
    single {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()

            val request: Request = original.newBuilder()
                .addHeader("key", "AIzaSyAY6hxar8pnsad3crSvPTLDkhKECUM1PN0")
                .method(original.method, original.body)
                .build()

            chain.proceed(request)
        }

        val client = httpClient.build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(YoutubeV3Api::class.java)
    }
}