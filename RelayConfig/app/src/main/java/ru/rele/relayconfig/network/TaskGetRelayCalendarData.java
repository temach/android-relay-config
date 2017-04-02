package ru.rele.relayconfig.network;

import android.os.AsyncTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.rele.relayconfig.relaydata.RelayCalendarData;

/**
 * Created by artem on 4/2/17.
 */

public class TaskGetRelayCalendarData extends AsyncTask<Void, Void, RelayCalendarData> {
    private NetworkTaskInfo task;
    public TaskGetRelayCalendarData(NetworkTaskInfo networkTaskInfo) {
        task = networkTaskInfo;
    }

    @Override
    protected RelayCalendarData doInBackground(Void... voids) {
        // set up the service
        Retrofit restAdapter = new Retrofit
                .Builder()
                .baseUrl(String.format("http://%s:%s", task.host, task.port + ""))
                .addConverterFactory(GsonFactoryBuilder.buildGsonConverter())
                .build();
        RelayServerAPI server = restAdapter.create(RelayServerAPI.class);
        Call<RelayCalendarData> request = server.getRelayCalendarData();
        try {
            Response<RelayCalendarData> response = request.execute();
            // isSuccessful is true if response code => 200 and <= 300
            if (response.isSuccessful()) {
                return response.body();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        task.setStatus("Getting calendar data from server");
    }

    @Override
    protected void onPostExecute(RelayCalendarData data) {
        super.onPostExecute(data);
        if (data != null) {
            task.setRelayCalendarData(data);
            task.setStatus("Retrieved calendar: " + data.calendarName);
        } else {
            task.setStatus("Failed to read data");
        }
    }
}

