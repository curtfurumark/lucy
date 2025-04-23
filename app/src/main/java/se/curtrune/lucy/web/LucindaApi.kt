package se.curtrune.lucy.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import se.curtrune.lucy.screens.affirmations.Affirmation
import se.curtrune.lucy.screens.affirmations.Quote
import se.curtrune.lucy.screens.message_board.Message

interface LucindaApi {
    suspend fun getAffirmation(): Affirmation
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
                    install(JsonFeature){
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}