package se.curtrune.lucy.web

import retrofit2.http.GET
import se.curtrune.lucy.screens.affirmations.Quote

interface QuoteApi {

    @GET("random")
    suspend fun getRandomQuotes(): List<Quote>
}