package com.stosh.discountstorage;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.database.Card;
import com.stosh.discountstorage.database.CardList;
import com.stosh.discountstorage.database.Room;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.database.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StoSh on 21-Mar-17.
 */

public class FireBaseSingleton {

    private final String DB_USERS = "Users";
    private final String DB_ROOMS_LIST = "RoomList";
    private final String DB_ROOMS = "Rooms";
    private final String DB_CARD_LIST = "CardList";
    private final String DB_CARDS = "Card";

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

    private FireBaseSingleton() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    private void init() {
        mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        userIdDB = mUser.getEmail().replace(".", "").toLowerCase();
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

    public void login(
            Activity activity,
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

    public void addUserToDB(String email, String password) {
        init();
        myRef = database.getReference(DB_USERS);
        User user = new User(userId, email, password);
        myRef.child(userIdDB).setValue(user);
    }

    public void createRoomList(String name) {
        init();
        myRef = database.getReference(DB_USERS);
        RoomList roomList = new RoomList(name + "_" + userIdDB);
        myRef.child(userIdDB).child(DB_ROOMS_LIST).child(name + "_" + userIdDB).setValue(roomList);
    }

    public void createRoom(String name, String password) {
        init();
        myRef = database.getReference(DB_ROOMS);
        Room room = new Room(name, password, userIdDB);
        myRef.child(name + "_" + userIdDB).setValue(room);
    }

    public void createCardList(String roomName, String cardName) {
        init();
        myRef = database.getReference(DB_ROOMS);
        CardList cardList = new CardList(cardName, cardName + "_" + roomName);
        myRef.child(roomName).child(DB_CARD_LIST).child(cardName + "_" + roomName).setValue(cardList);
    }

    public void createCard(String roomName, String cardName, String category, String code, String format) {
        init();
        myRef = database.getReference(DB_CARDS);
        Card card = new Card(roomName, cardName, category, code, format);
        myRef.child(cardName + "_" + roomName + "_" + userIdDB).setValue(card);
    }

    public void getRooms(ValueEventListener listener) {
        init();
        myRef = database.getReference(DB_USERS);
        myRef.child(userIdDB).child(DB_ROOMS_LIST).addValueEventListener(listener);
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
        init();
        mAuth.signOut();
    }

    public void checkUserInDB(String creator, ValueEventListener listener){
        init();
        myRef = database.getReference(DB_USERS);
        myRef.child(creator).addValueEventListener(listener);
    }

    public void checkRoomList(String creator, String nameRoom, ValueEventListener listener){
        init();
        myRef = database.getReference(DB_USERS);
        myRef.child(creator).child(DB_ROOMS_LIST).child(nameRoom + "_" + creator).addValueEventListener(listener);

    }

    public void checkPassword(String creator, String nameRoom, ValueEventListener listener){
        init();
        myRef = database.getReference(DB_ROOMS);
        myRef.child(nameRoom + "_" + creator).addValueEventListener(listener);
    }

    public void addToRoomList(String creator, String nameRoom){
        init();
        myRef = database.getReference(DB_USERS);
        RoomList roomList = new RoomList(nameRoom + "_" + creator);
        myRef.child(userIdDB).child(DB_ROOMS_LIST).child(nameRoom + "_" + creator).setValue(roomList);
    }
}
