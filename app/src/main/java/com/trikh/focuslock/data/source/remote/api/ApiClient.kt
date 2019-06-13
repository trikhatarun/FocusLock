package com.trikh.focuslock.data.source.remote.api

import com.trikh.focuslock.data.source.remote.api.ApiConstants.Companion.API_KEY
import com.trikh.focuslock.data.source.remote.api.ApiConstants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

    val client: ApiInterface by lazy {
        val clientBuilder = OkHttpClient().newBuilder()

        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

        clientBuilder.addInterceptor { interceptor ->
            val request = interceptor.request()
            val url = request.url().newBuilder().addQueryParameter(API_KEY, "AIzaSyDQQ_R9oVN8f6o_0nutbCB_HIYMSt_SVB0").build()
            interceptor.proceed(request.newBuilder().url(url).build())
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(clientBuilder.build())
            .build()

        retrofit.create(ApiInterface::class.java)
    }
}