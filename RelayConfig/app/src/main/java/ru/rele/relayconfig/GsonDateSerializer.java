package ru.rele.relayconfig;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by artem on 4/2/17.
 */

public class GsonDateSerializer implements JsonSerializer<Date> {

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        String primitive = src.getYear() + "-" + src.getMonth() + "-" + src.getDay();
        return new JsonPrimitive(primitive);
    }
}
