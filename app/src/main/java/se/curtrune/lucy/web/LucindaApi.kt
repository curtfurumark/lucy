package se.curtrune.lucy.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
//import io.ktor.client.features.json.JsonFeature
//import io.ktor.client.features.json.serializer.KotlinxSerializer
//import io.ktor.client.features.logging.LogLevel
//import io.ktor.client.features.logging.Logging
import se.curtrune.lucy.classes.calender.Holiday
import se.curtrune.lucy.screens.affirmations.Affirmation
import se.curtrune.lucy.screens.affirmations.Quote
import se.curtrune.lucy.screens.message_board.Message

interface LucindaApi {
    suspend fun getAffirmation(): Affirmation
    suspend fun getHolidays(): List<Holiday>
    //suspend fun getResponse(): HttpResponse
    suspend fun getMessages(): List<Message>
    suspend fun getMessage(): Message
    suspend fun getUpdateAvailable(): VersionInfo
    suspend fun insertMessage(message: Message): String
    suspend fun getQuotes(): List<Quote>
    suspend fun updateMessage(message: Message): String

    companion object{
        fun create(): LucindaApi{
            return LucindaApiImpl(
                client = HttpClient(Android){
                    install(Logging){
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation){
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        })
                    }
                }
            )
        }
    }
}