package ru.rele.relayconfig.network;

import ru.rele.relayconfig.relaydata.RelayCalendarData;

/**
 * Created by artem on 4/2/17.
 */

public class NetworkTaskInfo {

    public interface NetworkTaskListener {
        void OnNetworkTaskUpdate(NetworkTaskInfo task);
    }

    // input values
    // in milliseconds
    public final int timeout = 1000;
    public final int port = 9000;
    public final String host = "192.168.1.40";

    // return values
    private String status;
    private boolean pingResult;
    private RelayCalendarData relayCalendarData;

    private NetworkTaskListener listener;
    public void setListener(NetworkTaskListener l) {
        listener = l;
    }

    public void setStatus(String s) {
        status = s;
        listener.OnNetworkTaskUpdate(this);
    }
    public String getStatus() {
        return status;
    }

    public void setPingResult(boolean isPingSuccessful) {
        pingResult = isPingSuccessful;
        listener.OnNetworkTaskUpdate(this);
    }
    public boolean getPingResult() {
        return pingResult;
    }

    public void setRelayCalendarData(RelayCalendarData data) {
        relayCalendarData = data;
        listener.OnNetworkTaskUpdate(this);
    }
    public RelayCalendarData getRelayCalendarData() {
        return relayCalendarData;
    }
}
