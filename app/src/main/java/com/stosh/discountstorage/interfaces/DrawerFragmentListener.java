package com.stosh.discountstorage.interfaces;

/**
 * Created by StoSh on 31-Mar-17.
 */

public interface DrawerFragmentListener {
    void send(String code);
    void sendList(String roomName);
    void sendCard(String ID);
}
