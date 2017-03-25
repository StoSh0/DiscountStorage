package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 19-Mar-17.
 */


@IgnoreExtraProperties
public class Card {

    public String nameRoom;
    public String name;
    public String category;
    public String code;
    public String format;
    public Card(){

    }

    public Card(String nameRoom, String name, String category, String code, String format){
        this.nameRoom = nameRoom;
        this.name = name;
        this.category = category;
        this.code = code;
        this.format = format;
    }
}
