package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 25-Mar-17.
 */
@IgnoreExtraProperties
public class CardList {

    public String name;
    public String ID;

    public CardList(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }
}
