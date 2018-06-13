package com.example.kolyannow.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, account.getName());
        values.put(COUNT, account.getCount());

        db.insert(INFO_ACCOUNT, null, values);
        db.close();
    }

    @Override
    public Account getInfoAccount(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(INFO_ACCOUNT, new String[] { KEY_ID,
                        KEY_NAME, COUNT}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Account account = new Account(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));

        return account;
    }

    @Override
    public List<Account> getAllInfoAccounts() {
        List<Account> contactList = new ArrayList<Account>();
        String selectQuery = "SELECT  * FROM " + INFO_ACCOUNT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account();
                account.setId(Integer.parseInt(cursor.getString(0)));
                account.setName(cursor.getString(1));
                account.setCount(cursor.getString(2));
                contactList.add(account);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    @Override
    public int updateInfoAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, account.getName());
        values.put(COUNT, account.getCount());

        return db.update(INFO_ACCOUNT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(account.getId()) });
    }

    @Override
    public void deleteInfoAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INFO_ACCOUNT, KEY_ID + " = ?", new String[] { String.valueOf(account.getId()) });
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INFO_ACCOUNT, null, null);
        db.close();
    }

    @Override
    public int getInfoAccountCount() {
        String countQuery = "SELECT  * FROM " + INFO_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
}
