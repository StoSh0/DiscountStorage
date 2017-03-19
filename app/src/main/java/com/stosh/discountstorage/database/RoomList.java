package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 19-Mar-17.
 */
@IgnoreExtraProperties
public class RoomList {

    public String name;
    public String password;

    public RoomList(){

    }

    public RoomList(String name, String password){
        this.name = name;
        this.password = password;
    }

}
