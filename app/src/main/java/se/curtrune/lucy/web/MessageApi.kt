package se.curtrune.lucy.web

import retrofit2.http.GET
import se.curtrune.lucy.classes.Message

interface MessageApi {
    @GET("/lucinda/message.json")
    suspend fun getMessage(): Message
    @GET("/lucinda/messages.php")
    suspend fun getMessages(): List<Message>
}