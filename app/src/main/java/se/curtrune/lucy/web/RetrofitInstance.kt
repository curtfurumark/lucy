package se.curtrune.lucy.web

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            println(message)
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client : OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(loggingInterceptor)
    }.build()

    private const val BASE_URL = "https://zenquotes.io/api/"
    private fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun getInstance(baseUrl: String): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val quoteApi: QuoteApi = getInstance().create(QuoteApi::class.java)
    val messageApi: MessageApi = getInstance("https://curtfurumark.se").create(MessageApi::class.java)
}