package se.curtrune.lucy.web

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.HEADERS
}

val client : OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(interceptor)
}.build()


object RetrofitMessageInstance {
    private const val BASE_URL = "http://curtfurumark.se"
    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val messageApi: MessageApi = getInstance().create(MessageApi::class.java)
}