package ru.rele.relayconfig.network;

import android.os.AsyncTask;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by artem on 4/2/17.
 */

public class TaskPingServer extends AsyncTask<Void, Void, Boolean> {
    private NetworkTaskInfo task;
    public TaskPingServer(NetworkTaskInfo networkTaskInfo) {
        task = networkTaskInfo;
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Socket socket = new Socket();
            InetSocketAddress address = new InetSocketAddress(task.host, task.port);
            socket.connect(address, task.timeout);
            socket.close();
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            task.setStatus("Server is ready");
        } else {
            task.setStatus("Wifi is connected, but server is not responding");
        }
        task.setPingResult(result);
    }
}

