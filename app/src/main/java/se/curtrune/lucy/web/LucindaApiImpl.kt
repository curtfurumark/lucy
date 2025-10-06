package se.curtrune.lucy.web

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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
        return client.get(Urls.GET_AFFIRMATION_URL).body()
/*        return client.get {
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
        }*/
    }

    override suspend fun getHolidays(): List<Holiday> {
        return client.get(Urls.HOLIDAYS_URL).body()
/*        return client.get {
            try {
                url(Urls.HOLIDAYS_URL)
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
        }*/
    }

    override suspend fun getMessages(): List<Message> {
        return client.get(Urls.MESSAGES_URL).body()
/*        return client.get{
            try {
                url(Urls.MESSAGES_URL)
            }catch (exception: RedirectResponseException){
                //3xx
                println("RedirectResponseException ${exception.response.status.description}")
                emptyList<Message>()
            }catch (exception: ClientRequestException){
                //4xx
                println("client request exception ${exception.response.status.description}")
                println(exception.response.status.description)
                emptyList<Message>()
            }catch (exception: ServerResponseException){
                //5xx'
                println("server response exception ${exception.response.status.description}")
                println(exception.response.status.description)
                emptyList<Message>()
            }catch (e: Exception){
                println("Exception ")
                println(e.message)
                emptyList<Message>()
            }
        }*/
    }

    override suspend fun getMessage(): Message {
        return client.get(Urls.MESSAGE_URL).body()
/*        return client.get{
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
        }*/
    }

    override suspend fun getUpdateAvailable(): VersionInfo {
        return client.get(Urls.VERSION_INFO_URL).body()
/*        return client.get{
            url(Urls.VERSION_INFO_URL)
        }*/
    }

    override suspend fun insertMessage(message: Message): String {
        return try{
            client.post {
                url(Urls.INSERT_MESSAGE_URL)
                contentType(ContentType.Application.Json)
                setBody(message)
            }.body()
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
        return client.get(Urls.GET_QUOTES_URL).body()
    }

    override suspend fun updateMessage(message: Message): String {
        return try {
            client.post {
                url(Urls.UPDATE_MESSAGE_URL)
                contentType(ContentType.Application.Json)
                setBody(message)
            }.body()
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