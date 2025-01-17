package se.curtrune.lucy.screens.affirmations

import retrofit2.http.GET

interface QuoteApi {

    @GET("random")
    suspend fun getRandomQuotes(): List<Quote>
}