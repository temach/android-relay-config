package ru.rele.relayconfig.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by artem on 4/2/17.
 */

public class GsonFactoryBuilder {

    public static GsonConverterFactory buildGsonConverter() {
        GsonBuilder builder = new GsonBuilder();

        // you can also register custom serialiser / deserialiser if you want
        // see www.stackoverflow.com/questions/35502079/custom-converter-for-retrofit-2
        builder.registerTypeAdapter(Date.class, new GsonDateSerializer());

        Gson myGson = builder
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .enableComplexMapKeySerialization()
                .create();
        return GsonConverterFactory.create(myGson);
    }
}
