package se.curtrune.lucy.util.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import se.curtrune.lucy.classes.ItemDuration;

public class ItemDurationSerialize implements JsonDeserializer<ItemDuration.Type>, JsonSerializer<ItemDuration.Type> {

    @Override
    public ItemDuration.Type deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ItemDuration.Type.valueOf(json.getAsString().toUpperCase());
    }

    @Override
    public JsonElement serialize(ItemDuration.Type src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name());
    }
}