package ru.rele.relayconfig.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.rele.relayconfig.relaydata.RelayCalendarData;


/**
 * Created by artem on 4/1/17.
 */

public interface RelayServerAPI {
    @POST("/api/calendar")
    Call<Void> submitRelayCalendarData(@Body RelayCalendarData classObject);

    @GET("/api/calendar")
    Call<RelayCalendarData> getRelayCalendarData();
}
