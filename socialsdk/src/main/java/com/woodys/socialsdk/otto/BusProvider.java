package com.woodys.socialsdk.otto;

/**
 * Otto Bus Provider
 * Created by woodys on 2016/11/20.
 */
public class BusProvider {

    private static MainThreadBus bus;

    public static MainThreadBus getInstance() {
        if (bus == null)
            bus = new MainThreadBus();

        return bus;
    }

}
