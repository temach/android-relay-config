package ru.rele.relayconfig.network;

import android.os.AsyncTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import ru.rele.relayconfig.relaydata.RelayCalendarData;

/**
 * Created by artem on 4/2/17.
 */

public class TaskPostRelayCalendarData extends AsyncTask<RelayCalendarData, Void, Boolean> {
    private NetworkTaskInfo task;
    public TaskPostRelayCalendarData(NetworkTaskInfo networkTaskInfo) {
        task = networkTaskInfo;
    }

    @Override
    protected Boolean doInBackground(RelayCalendarData... relayCalendarDatas) {
        RelayCalendarData calendarData = relayCalendarDatas[0];

        // set up the service
        Retrofit restAdapter = new Retrofit
                .Builder()
                .baseUrl(String.format("http://%s:%s", task.host, task.port+""))
                .addConverterFactory(GsonFactoryBuilder.buildGsonConverter())
                .build();
        RelayServerAPI server = restAdapter.create(RelayServerAPI.class);
        Call<Void> request = server.submitRelayCalendarData(calendarData);
        try {
            // isSuccess is true if response code => 200 and <= 300
            return request.execute().isSuccessful();
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        task.setStatus("Sending HTTP POST task to device.");
    }

    @Override
    protected void onPostExecute(Boolean available) {
        super.onPostExecute(available);
        task.setStatus("Settings flushed: " + available);
    }
}

