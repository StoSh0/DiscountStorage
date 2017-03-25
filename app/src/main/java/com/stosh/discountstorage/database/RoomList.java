package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 25-Mar-17.
 */

@IgnoreExtraProperties
public class RoomList {

    public String name;

    public RoomList(String name) {
        this.name = name;
    }
}
