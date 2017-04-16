package com.stosh.discountstorage;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
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

    private FireBaseSingleton() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void init() {
        mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        userIdDB = mUser.getEmail().toLowerCase().replace(".", "");
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

    public void createUserInDB(String email, String password) {
        init();
        myRef = database.getReference(Const.DB_USERS);
        User user = new User(userId, email, password);
        myRef.child(userIdDB).setValue(user);
    }

    public void createRoomList(String name) {
        init();
        myRef = database.getReference(Const.DB_USERS);
        RoomList roomList = new RoomList(name + "_" + userIdDB, name);
        myRef.child(userIdDB).child(Const.DB_ROOMS_LIST).child(name + "_" + userIdDB).setValue(roomList);
    }

    public void createRoom(String name, String password) {
        init();
        myRef = database.getReference(Const.DB_ROOMS);
        Room room = new Room(name, password, userIdDB);
        myRef.child(name + "_" + userIdDB).setValue(room);
    }

    public void createCardList(String ID, String cardName, String category) {
        init();
        myRef = database.getReference(Const.DB_ROOMS);
        CardList cardList = new CardList(cardName, cardName + "_" + ID, category);
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
        Card card = new Card(ID, cardName, category, code, format);
        myRef.child(cardName + "_" + ID).setValue(card);
    }

    public void getRooms(ValueEventListener listener) {
        init();
        myRef = database.getReference(Const.DB_USERS);
        myRef.child(userIdDB).child(Const.DB_ROOMS_LIST).addListenerForSingleValueEvent(listener);
    }

    public void checkUserInDB(String creator, ValueEventListener listener) {
        init();
        myRef = database.getReference(Const.DB_USERS);
        myRef.child(creator).addListenerForSingleValueEvent(listener);
    }

    public void checkRoomList(String creator, String nameRoom, ValueEventListener listener) {
        init();
        myRef = database.getReference(Const.DB_USERS);
        myRef.child(creator)
                .child(Const.DB_ROOMS_LIST)
                .child(nameRoom + "_" + creator)
                .addListenerForSingleValueEvent(listener)
        ;

    }

    public void checkPassword(String creator, String nameRoom, ValueEventListener listener) {
        init();
        myRef = database.getReference(Const.DB_ROOMS);
        myRef.child(nameRoom + "_" + creator).addListenerForSingleValueEvent(listener);
    }

    public void addToRoomList(String creator, String nameRoom) {
        init();
        myRef = database.getReference(Const.DB_USERS);
        RoomList roomList = new RoomList(nameRoom + "_" + creator, nameRoom);
        myRef.child(userIdDB)
                .child(Const.DB_ROOMS_LIST)
                .child(nameRoom + "_" + creator)
                .setValue(roomList)
        ;
    }

    public void getCardList(String roomName, ValueEventListener listener) {
        init();
        myRef = database.getReference(Const.DB_ROOMS);
        myRef.child(roomName).child(Const.DB_CARD_LIST).addListenerForSingleValueEvent(listener);
    }

    public void getCard(String id, ValueEventListener listener) {
        init();
        myRef = database.getReference(Const.DB_CARDS);
        Log.d("1", id);
        myRef.child(id).addListenerForSingleValueEvent(listener);

    }

}