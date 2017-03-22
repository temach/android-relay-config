package ru.rele.relayconfig;

import android.app.Application;

/**
 * Created by artem on 3/22/17.
 * Use this class to pass info between activities
 */

public class MainApplication extends Application {
    private CycleControl currentCycle;

    public void setCurrentCycle(CycleControl cc) {
        currentCycle = cc;
    }
    public CycleControl getCurrentCycle() {
        return currentCycle;
    }
}
