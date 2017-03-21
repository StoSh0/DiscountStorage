package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 19-Mar-17.
 */
@IgnoreExtraProperties
public class RoomList {

    public String name;
    public String password;
    public String creator;

    public RoomList(){

    }

    public RoomList(String name, String password,String creator){
        this.name = name;
        this.password = password;
        this.creator = creator;
    }

}
