package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 19-Mar-17.
 */


@IgnoreExtraProperties
public class Cards {

    public String name;
    public String category;
    public String code;

    public Cards(){

    }

    public Cards(String name, String category, String code){
        this.name = name;
        this.category = category;
        this.code = code;
    }
}
