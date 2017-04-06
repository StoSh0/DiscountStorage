package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 25-Mar-17.
 */

@IgnoreExtraProperties
public class RoomList {
    public String ID, name;

    public RoomList(){

    }

    public RoomList(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }
}
