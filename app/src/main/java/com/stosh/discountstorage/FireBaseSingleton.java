package com.stosh.discountstorage;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.database.Card;
import com.stosh.discountstorage.database.CardList;
import com.stosh.discountstorage.database.Room;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.database.User;
import com.stosh.discountstorage.interfaces.Const;

/**
 * Created by StoSh on 21-Mar-17.
 **/

public class FireBaseSingleton {
	
	private static final FireBaseSingleton ourInstance = new FireBaseSingleton();
	
	public static FireBaseSingleton getInstance() {
		return ourInstance;
	}
	
	private FirebaseAuth.AuthStateListener mAuthListener;
	private FirebaseAuth mAuth;
	private FirebaseUser mUser;
	private FirebaseDatabase database;
	private DatabaseReference myRef;
	private String userId;
	private String userIdDB;
	private String userEmail;
	
	private FireBaseSingleton() {
		mAuth = FirebaseAuth.getInstance();
		database = FirebaseDatabase.getInstance();
	}
	
	private void init() {
		mUser = mAuth.getCurrentUser();
		assert mUser != null;
		userId = mUser.getUid();
		userEmail = mUser.getEmail();
		assert userEmail != null;
		userIdDB = userEmail.toLowerCase().replace(".", "");
	}
	
	public void check(FirebaseAuth.AuthStateListener mAuthListener) {
		this.mAuthListener = mAuthListener;
	}
	
	public void onStart() {
		mAuth.addAuthStateListener(mAuthListener);
	}
	
	public void onStop() {
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}
	
	public void login(Activity activity,
					  String email,
					  String password,
					  OnCompleteListener<AuthResult> listener) {
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(activity, listener);
	}
	
	public void singUp(String email, String password, OnCompleteListener<AuthResult> listener) {
		mAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(listener);
	}
	
	public void resetPassword(String email, OnCompleteListener<Void> listener) {
		mAuth.sendPasswordResetEmail(email)
				.addOnCompleteListener(listener);
	}
	
	public void changeEmail(String email, OnCompleteListener listener) {
		init();
		mUser.updateEmail(email).addOnCompleteListener(listener);
	}
	
	public void changePassword(String password, OnCompleteListener listener) {
		init();
		mUser.updatePassword(password)
				.addOnCompleteListener(listener);
	}
	
	public void deleteUser(OnCompleteListener listener) {
		init();
		mUser.delete().addOnCompleteListener(listener);
	}
	
	public void singOut() {
		mAuth.signOut();
	}
	
	public String getUserEmail() {
		init();
		return userEmail;
	}
	
	public void createUserInDB(String email, String password) {
		init();
		myRef = database.getReference(Const.DB_USERS);
		User user = new User(userId, email, password);
		myRef.child(userIdDB).setValue(user);
	}
	
	public void createRoomList(String name) {
		init();
		myRef = database.getReference(Const.DB_USERS);
		RoomList roomList = new RoomList(name + "_" + userIdDB);
		myRef.child(userIdDB).child(Const.DB_ROOMS_LIST).child(name + "_" + userIdDB).setValue(roomList);
	}
	
	public void createRoom(String name, String password) {
		init();
		myRef = database.getReference(Const.DB_ROOMS);
		Room room = new Room(name, password, userEmail);
		myRef.child(name + "_" + userIdDB).setValue(room);
	}
	
	public void addRoomToList(String id) {
		init();
		myRef = database.getReference(Const.DB_USERS);
		RoomList roomList = new RoomList(id);
		myRef.child(userIdDB).child(Const.DB_ROOMS_LIST).child(id).setValue(roomList);
	}
	
	public void getRoomList(ValueEventListener listener) {
		init();
		myRef = database.getReference(Const.DB_USERS);
		myRef.child(userIdDB).child(Const.DB_ROOMS_LIST).addListenerForSingleValueEvent(listener);
	}
	
	public void getRoom(String id, ValueEventListener listener) {
		init();
		myRef = database.getReference(Const.DB_ROOMS);
		myRef.child(id).addListenerForSingleValueEvent(listener);
	}
	
	public void deleteRoom(String id) {
		init();
		myRef = database.getReference(Const.DB_ROOMS);
		myRef.child(id).removeValue();
	}
	
	
	public void checkRoom(String id, ValueEventListener listener) {
		init();
		myRef = database.getReference(Const.DB_ROOMS);
		myRef.child(id).addListenerForSingleValueEvent(listener);
	}
	
	public void deleteFromRoomList(String id) {
		init();
		myRef = database.getReference(Const.DB_USERS);
		myRef.child(userIdDB).child(Const.DB_ROOMS_LIST).child(id).removeValue();
	}
	
	
	public void createCardList(String ID, String cardName) {
		init();
		myRef = database.getReference(Const.DB_ROOMS);
		CardList cardList = new CardList(cardName + "_" + ID);
		myRef.child(ID).child(Const.DB_CARD_LIST)
				.child(cardName + "_" + ID)
				.setValue(cardList);
	}
	
	public void createCard(String ID,
						   String cardName,
						   String category,
						   String code,
						   String format) {
		init();
		myRef = database.getReference(Const.DB_CARDS);
		Card card = new Card(cardName, category, code, format, userEmail);
		myRef.child(cardName + "_" + ID).setValue(card);
	}
	
	public void getCardList(String roomId, ValueEventListener listener) {
		init();
		myRef = database.getReference(Const.DB_ROOMS);
		myRef.child(roomId).child(Const.DB_CARD_LIST).addListenerForSingleValueEvent(listener);
	}
	
	public void reNameCard(String id, String name) {
		init();
		myRef = database.getReference(Const.DB_CARDS);
		myRef.child(id).child("name").setValue(name);
	}
	
	public void deleteFromCardList(String idRoom, String idCard) {
		init();
		myRef = database.getReference(Const.DB_ROOMS);
		myRef.child(idRoom).child(Const.DB_CARD_LIST).child(idCard).removeValue();
	}
	
	public void getCard(String id, ValueEventListener listener) {
		init();
		myRef = database.getReference(Const.DB_CARDS);
		myRef.child(id).addListenerForSingleValueEvent(listener);
	}
	
	public void deleteCard(String id) {
		init();
		myRef = database.getReference(Const.DB_CARDS);
		myRef.child(id).removeValue();
		myRef = database.getReference(Const.DB_CARDS);
		myRef.child(id).removeValue();
	}
}