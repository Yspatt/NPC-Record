package com.yan.minecraft.npc.util.world;

import com.google.gson.*;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        if(location != null && location.getWorld() != null) {
            jsonObject.add("location", new JsonPrimitive(LocationUtils.toStringExact(location)));
        }
        return jsonObject;
    }

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return LocationUtils.toLocationExact(jsonObject.get("location").getAsString());
    }

}