package com.stosh.discountstorage.interfaces;

/**
 * Created by StoSh on 31-Mar-17.
 **/

public interface DrawerFragmentListener {
	void send(String id, String code);
	
	void edit(String id, String idRoom);
}
