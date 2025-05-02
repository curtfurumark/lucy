package se.curtrune.lucy.web

import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import se.curtrune.lucy.classes.calender.Holiday
import se.curtrune.lucy.screens.affirmations.Affirmation
import se.curtrune.lucy.screens.affirmations.Quote
import se.curtrune.lucy.screens.message_board.Message


class LucindaApiImpl(private val client: HttpClient): LucindaApi {
    override suspend fun getAffirmation(): Affirmation {
        println("implementation of getAffirmation")
        return client.get {
            try {
                url(Urls.GET_AFFIRMATION_URL)
            } catch (exception: RedirectResponseException) {
                //3xx
                println(exception.response.status.description)
                Affirmation(affirmation = "you are the tops")
            } catch (exception: ClientRequestException) {
                //4xx
                println(exception.response.status.description)
                Affirmation(affirmation = "keep it up")
            } catch (exception: ServerResponseException) {
                //5xx
                println(exception.response.status.description)
                Affirmation(affirmation = "you're great")
            } catch (e: Exception) {
                println(e.message)
                Affirmation(affirmation = "don't worry")
            }
        }
    }

    override suspend fun getHolidays(): List<Holiday> {
        return emptyList()
    }

    override suspend fun getMessages(): List<Message> {
        return client.get{
            try {

                url(Urls.MESSAGES_URL)
            }catch (exception: RedirectResponseException){
                //3xx
                println(exception.response.status.description)
                emptyList<Message>()
            }catch (exception: ClientRequestException){
                //4xx
                println(exception.response.status.description)
                emptyList<Message>()
            }catch (exception: ServerResponseException){
                //5xx
                println(exception.response.status.description)
                emptyList<Message>()
            }catch (e: Exception){
                println(e.message)
                emptyList<Message>()
            }
        }
    }

    override suspend fun getMessage(): Message {
        return client.get{
            try {

                url(Urls.MESSAGE_URL)
            }catch (exception: RedirectResponseException){
                //3xx
                println(exception.response.status.description)
                emptyList<Message>()
            }catch (exception: ClientRequestException){
                //4xx
                println(exception.response.status.description)
                emptyList<Message>()
            }catch (exception: ServerResponseException){
                //5xx
                println(exception.response.status.description)
                emptyList<Message>()
            }catch (e: Exception){
                println(e.message)
                emptyList<Message>()
            }
        }
    }

    override suspend fun getUpdateAvailable(): VersionInfo {
        return client.get{
            url(Urls.VERSION_INFO_URL)
        }
    }

    override suspend fun insertMessage(message: Message): String {
        return try{
            client.post<String>{
                url(Urls.INSERT_MESSAGE_URL)
                contentType(ContentType.Application.Json)
                body = message
            }
        }catch (exception: RedirectResponseException){
            //3xx
            println(exception.response.status.description)
            exception.response.status.description
        }catch (exception: ClientRequestException){
            //4xx
            println(exception.response.status.description)
            exception.response.status.description
        }catch (exception: ServerResponseException){
            //5xx
            println(exception.response.status.description)
            exception.response.status.description
        }catch (e: Exception){
            "exception ${e.message}"
        }
    }

    override suspend fun getQuotes(): List<Quote> {
        return client.get{
            url(Urls.GET_QUOTES_URL)
        }
    }

    override suspend fun updateMessage(message: Message): String {
        return try {
            client.post<String>{
            url(Urls.UPDATE_MESSAGE_URL)
            contentType(ContentType.Application.Json)
            body = message
        }
        }catch (exception: RedirectResponseException){
            //3xx
            println(exception.response.status.description)
            exception.response.status.description
        }catch (exception: ClientRequestException){
            //4xx
            println(exception.response.status.description)
            exception.response.status.description
        }catch (exception: ServerResponseException){
            //5xx
            println(exception.response.status.description)
            exception.response.status.description
        }catch (e: Exception) {
                "exception ${e.message}"
        }
    }
}