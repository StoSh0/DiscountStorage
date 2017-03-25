package com.stosh.discountstorage;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stosh.discountstorage.database.Room;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.database.User;

/**
 * Created by StoSh on 21-Mar-17.
 */

public class FireBaseSingleton {

    private final String DBUser = "Users";
    private final String DBRoomList = "RoomList";
    private final String DBRooms = "Rooms";

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
        mAuth.addAuthStateListener(mAuthListener);
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
        myRef = database.getReference("Users");
        User user = new User(userId, email, password);
        myRef.child(userIdDB).setValue(user);
    }

    public void createList(String name) {
        init();
        myRef = database.getReference(DBUser);
        RoomList roomList = new RoomList(name + "_" + userIdDB);
        myRef.child(userIdDB).child(DBRoomList).child(name + "_" + userIdDB).setValue(roomList);
    }

    public void createRoom(String name, String password) {
        init();
        myRef = database.getReference(DBRooms);
        Room room = new Room(name, password, userIdDB);
        myRef.child(name + "_" + userIdDB).setValue(room);
    }

}


