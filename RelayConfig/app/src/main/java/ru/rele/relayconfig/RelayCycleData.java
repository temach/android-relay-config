package ru.rele.relayconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artem on 3/23/17.
 */

public class RelayCycleData {
    private List<RelayTimeStripData> timeStrips = new ArrayList<>();

    public void addTimeStrip(RelayTimeStripData tm) {
        timeStrips.add(tm);
    }

}
