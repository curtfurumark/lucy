package se.curtrune.lucy.web

object Urls {
    private const val BASE_URL = "https://curtfurumark.se"
    const val MESSAGES_URL = "$BASE_URL/lucinda/messages.php"
    const val MESSAGE_URL = "$BASE_URL/lucinda/message.json"
    const val INSERT_MESSAGE_URL = "$BASE_URL/lucinda/insert_message.php"
    const val VERSION_INFO_URL =  "$BASE_URL/lucinda/apk/lucinda.json"
    const val ECHO_BODY_URL = "$BASE_URL/rest/echo_body.php"
    const val JSON_TO_OBJECT_URL = "$BASE_URL/rest/json_to_object.php"
    const val GET_AFFIRMATION_URL: String = "https://www.affirmations.dev"
    const val GET_QUOTES_URL = "https://zenquotes.io/api/random"
}