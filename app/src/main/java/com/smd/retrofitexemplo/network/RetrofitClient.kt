package com.smd.retrofitexemplo.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

object RetrofitClient {
    // Url da api
    private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

    // 2. Configuração do Leitor de JSON. Aqui configuramos como o app deve ler os dados que vêm da internet.
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // 3. O Serviço da API (ApiService). "by lazy" para economizar bateria e memória.
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}