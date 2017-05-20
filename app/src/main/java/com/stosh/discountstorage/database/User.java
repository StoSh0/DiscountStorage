package com.stosh.discountstorage.database;

import com.google.firebase.database.IgnoreExtraProperties;


/**
 * Created by StoSh on 19-Mar-17.
 **/
@IgnoreExtraProperties
public class User {
	
	public String id;
	public String email;
	public String password;
	
	public User() {
	}
	
	public User(String id, String email, String password) {
		this.id = id;
		this.email = email;
		this.password = password;
	}
	
}
