package com.dw.andro.dictionary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dw.andro.dictionary.DataObject.WordDetailObject;
import com.dw.andro.dictionary.DataObject.WordObject;

import java.util.ArrayList;

/**
 * Created by dvayweb on 01/04/16.
 */
public class DatabaseAdapter {

    DatabaseHelper helper;
    SQLiteDatabase db;

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();

    }

    public long insertFavoriteWord(String wId, String name) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.FAVORITE_WORD, name);
        contentValues.put(helper.FAVORITE_WORD_ID, wId);
//        contentValues.put(helper.LEVEL, name);
        long id = db.insert(helper.FAVORITE_TABLE, null, contentValues);
        db.close();
        return id;

    }

    public long insertFavoriteDetail(String meaning, String description, String translation, long favoriteId) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.FAVORITE_MEANING, meaning);
        contentValues.put(helper.FAVORITE_DESCRIPTION, description);
        contentValues.put(helper.FAVORITE_TRANSLATION, translation);
        contentValues.put(helper.FOREIGN_FAVORITE_DETAIL_ID, favoriteId);

        long id = db.insert(helper.FAVORITE_DETAIL_TABLE, null, contentValues);
        db.close();
        return id;
    }

    public long insertWOD(String word, String wId, String date) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.WOD_WORD, word);
        contentValues.put(helper.WOD_Word_ID, wId);
        contentValues.put(helper.WOD_DATE, date);

        long id = db.insert(helper.WOD_TABLE, null, contentValues);
        db.close();
        return id;
    }

    public long insertWODDetail(String meaning, String description, String translation, long WODId) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.WOD_MEANING, meaning);
        contentValues.put(helper.WOD_DESCRIPTION, description);
        contentValues.put(helper.WOD_TRANSLATION, translation);
        contentValues.put(helper.FOREIGN_WOD_DETAIL_ID, WODId);

        long id = db.insert(helper.WOD_DETAIL_TABLE, null, contentValues);
        db.close();
        return id;
    }

    public WordObject getWOD() {
        open();
        String[] columns = {helper.WOD_Word_ID, helper.WOD_ID, helper.WOD_WORD, helper.WOD_DATE};
        Cursor cursor = db.query(helper.WOD_TABLE, columns, null, null, null, null, null);

        if (cursor.moveToLast()) {
            WordObject returnData = new WordObject();
            returnData.setWordId(cursor.getString(0));
            returnData.setWODId(cursor.getString(1));
            returnData.setWord(cursor.getString(2));
            returnData.setDateTime(cursor.getString(3));
            return returnData;
        }
        return null;
    }

    public ArrayList<WordDetailObject> getWODDetail(String id) {
        open();
        String[] columns = {helper.WOD_MEANING, helper.WOD_DESCRIPTION, helper.WOD_TRANSLATION};
        Cursor cursor = db.query(helper.WOD_DETAIL_TABLE, columns, helper.FOREIGN_WOD_DETAIL_ID + "=" + id, null, null, null, null);
        ArrayList<WordDetailObject> result = new ArrayList<>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                WordDetailObject returnData = new WordDetailObject();
                returnData.setMeaning(cursor.getString(0));
                returnData.setDescription(cursor.getString(1));
                returnData.setTranslation(cursor.getString(2));
                result.add(returnData);
                cursor.moveToNext();
            }
            return result;
        }
        return null;
    }

    public ArrayList<WordObject> getFavoriteList() {
        open();
        String[] columns = {helper.FAVORITE_WORD_ID, helper.FAVORITE_WORD};
        Cursor cursor = db.query(helper.FAVORITE_TABLE, columns, null, null, null, null, null);
        ArrayList<WordObject> result = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            WordObject returnData = new WordObject();
            returnData.setWordId(cursor.getString(0));
            returnData.setWord(cursor.getString(1));
            result.add(returnData);
            cursor.moveToNext();
        }
        return result;
    }

    public ArrayList<WordDetailObject> getFavoriteDetail(String id) {
        open();
        String[] columns = {helper.FAVORITE_MEANING, helper.FAVORITE_DESCRIPTION, helper.FAVORITE_TRANSLATION};
        Cursor cursor = db.query(helper.FAVORITE_DETAIL_TABLE, columns, helper.FOREIGN_FAVORITE_DETAIL_ID + "=" + id, null, null, null, null);
        ArrayList<WordDetailObject> result = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            WordDetailObject returnData = new WordDetailObject();
            returnData.setMeaning(cursor.getString(0));
            returnData.setDescription(cursor.getString(1));
            returnData.setTranslation(cursor.getString(2));
            result.add(returnData);
            cursor.moveToNext();
        }
        return result;
    }

    public void removeFavoriteDetail(String id) {
        open();
        db.delete(helper.FAVORITE_DETAIL_TABLE, helper.FOREIGN_FAVORITE_DETAIL_ID + "=?", new String[]{id});
        db.close();
    }

    public void removeFavoriteWord(String id) {
        open();
        db.delete(helper.FAVORITE_TABLE, helper.FAVORITE_WORD_ID + "=?", new String[]{id});
        db.close();
    }

    public boolean checkFavoriteWord(String id) {
        open();
        String[] columns = {helper.FAVORITE_WORD};
        Cursor cursor = db.query(helper.FAVORITE_TABLE, columns, helper.FAVORITE_WORD_ID + "=" + id, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        } else
            return false;
    }

    public boolean checkSearchWord(String id) {
        open();
        String[] columns = {helper.SEARCH_WORD};
        Cursor cursor = db.query(helper.SEARCH_TABLE, columns, helper.SEARCH_WORD_ID + "=" + id, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        } else
            return false;
    }

    public long insertSearchWord(String wId, String name) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.SEARCH_WORD, name);
        contentValues.put(helper.SEARCH_WORD_ID, wId);

        long id = db.insert(helper.SEARCH_TABLE, null, contentValues);
        db.close();
        return id;

    }

    public ArrayList<WordObject> getSearchList() {
        open();
        String[] columns = {helper.SEARCH_WORD_ID, helper.SEARCH_WORD};
        Cursor cursor = db.query(helper.SEARCH_TABLE, columns, null, null, null, null, null);
        ArrayList<WordObject> result = new ArrayList<>();
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                WordObject returnData = new WordObject();
                returnData.setWordId(cursor.getString(0));
                returnData.setWord(cursor.getString(1));
                result.add(returnData);
                cursor.moveToNext();
            }
            return result;
        } else {
            return null;
        }
    }

    public void deleteSearchHistory() {
        open();
        db.delete(helper.SEARCH_TABLE, null, null);
    }
}
