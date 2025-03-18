package se.curtrune.lucy

import com.google.gson.Gson
import org.junit.Test
import se.curtrune.lucy.screens.message_board.Message

class MessageTest {

    @Test
    fun testMessageToJson(){
        println("testMessageToJson()")
        val message =  Message(
            subject = "my subject",
            content = "my content",
            category = "todo",
            state = Message.State.TODO
        )
        val json = Gson().toJson(message)
        println("json: $json")
        assert(json == "{\"subject\":\"my subject\",\"content\":\"my content\",\"category\":\"todo\",\"user\":\"\",\"id\":0,\"created\":0,\"state\":\"TODO\"}")
    }

    @Test
    fun testFromJson(){
        println("testFromJson")
        val json = "{\"subject\":\"my subject\",\"content\":\"my content\",\"category\":\"todo\",\"user\":\"\",\"id\":0,\"created\":0,\"state\":\"TODO\"}"
        val message = Gson().fromJson(json, Message::class.java)
        println(message.toString())
        assert(message != null)
    }

}