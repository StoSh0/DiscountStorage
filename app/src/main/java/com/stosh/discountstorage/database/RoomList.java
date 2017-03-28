package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 25-Mar-17.
 */

@IgnoreExtraProperties
public class RoomList {

    public String name;
    public String ID;

    public RoomList(){

    }

    public RoomList(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }
}
