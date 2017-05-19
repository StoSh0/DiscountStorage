package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by StoSh on 19-Mar-17.
 */


@IgnoreExtraProperties
public class Card {
	public String name;
	public String category;
	public String code;
	public String format;
	public String creator;
	
	public Card() {
		
	}
	
	public Card(String name, String category, String code, String format, String creator) {
		this.name = name;
		this.category = category;
		this.code = code;
		this.format = format;
		this.creator = creator;
	}
}
