package ru.rele.relayconfig;

/**
 * Created by artem on 3/23/17.
 */

public class RelayTimeStripData {
    int startHour;
    int startMinute;

    int endHour;
    int endMinute;

    RelayTimeStripData() {
        // empty constructor
    }

    RelayTimeStripData(int startH, int startMin, int endH, int endMin) {
        startHour = startH;
        startMinute = startMin;
        endHour = endH;
        endMinute = endMin;
    }
}
