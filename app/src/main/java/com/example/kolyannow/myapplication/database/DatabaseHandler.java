package com.example.kolyannow.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kolyannow.myapplication.Pair;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "accoutYoBit";
    private static final String INFO_ACCOUNT = "info_account";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String COUNT = "count";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + INFO_ACCOUNT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + COUNT + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INFO_ACCOUNT);

        onCreate(db);
    }

    @Override
    public void addPair(Pair pair) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pair.getName());
        values.put(COUNT, pair.getDate());

        db.insert(INFO_ACCOUNT, null, values);
        db.close();
    }

    @Override
    public Pair getPair(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(INFO_ACCOUNT, new String[] { KEY_ID,
                        KEY_NAME, COUNT}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Pair pair = new Pair(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return pair;
    }

    @Override
    public List<Pair> getAllPairs() {
        List<Pair> contactList = new ArrayList<Pair>();
        String selectQuery = "SELECT  * FROM " + INFO_ACCOUNT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Pair pair = new Pair();
                pair.setId(Integer.parseInt(cursor.getString(0)));
                pair.setName(cursor.getString(1));
                pair.setDate(cursor.getString(2));
                contactList.add(pair);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    @Override
    public int updatePair(Pair pair) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pair.getName());
        values.put(COUNT, pair.getPrice());

        return db.update(INFO_ACCOUNT, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(pair.getId()) });
    }

    @Override
    public void deletePair(Pair pair) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INFO_ACCOUNT, KEY_ID + " = ?", new String[] { String.valueOf(pair.getId()) });
        db.close();
    }

    public  void deletePairToName(Pair pair){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INFO_ACCOUNT, KEY_NAME + " = ?", new String [] {String.valueOf(pair.getName())});
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INFO_ACCOUNT, null, null);
        db.close();
    }

    @Override
    public int getPairCount() {
//        String countQuery = "SELECT * FROM " + INFO_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(INFO_ACCOUNT, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count;

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(INFO_ACCOUNT, null, null, null, null, null, null);
        return cursor;
    }
}
