package se.curtrune.lucy

import com.google.gson.GsonBuilder
import org.junit.Assert.assertEquals
import org.junit.Test
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.util.gson.ItemDurationSerialize

class ItemDurationTest {

    @Test
    fun testSerialize(){
        println("testSerialize()")
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(ItemDuration.Type::class.java,
            ItemDurationSerialize()
        )
        val gson = gsonBuilder.create()
        val itemDuration = ItemDuration()
        itemDuration.type = ItemDuration.Type.SECONDS
        itemDuration.seconds =  42
        val json = gson.toJson(itemDuration)
        println(json)
        assertEquals("{\"type\":\"SECONDS\",\"seconds\":42}", json)
    }
    @Test
    fun testDeserialize(){
        println("testDeserialize")
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(ItemDuration.Type::class.java,
            ItemDurationSerialize()
        )
        val gson = gsonBuilder.create()
        val itemDuration = gson.fromJson("{\"type\":\"SECONDS\",\"seconds\":42}", ItemDuration::class.java)
        assertEquals(42, itemDuration.seconds)
        assertEquals(ItemDuration.Type.SECONDS, itemDuration.type)

    }
}