package com.cybermoosemoosemedia.honeydo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HoneyDoRemindersDbAdapter {

    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "HoneyDoRemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_honeydo";
    private static final String TABLE_NAME = "tbl_honeydo";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";

    public HoneyDoRemindersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    //CREATE
    //id will be created automatically
    public void createReminder(String name, boolean important) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, name);
        values.put(COL_IMPORTANT, important ? 1 : 0);
        mDb.insert(TABLE_NAME, null, values);
    }

    //overloaded to take a honeyDoDataModel
    public long createReminder(HoneyDoDataModel honeyDoDataModel) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, honeyDoDataModel.getContent());
        values.put(COL_IMPORTANT, honeyDoDataModel.getImportant());

        // Inserting Row

        return mDb.insert(TABLE_NAME, null, values);
    }

    //READ
    public HoneyDoDataModel fetchReminderById(int id) {

        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_CONTENT, COL_IMPORTANT}, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();
        return new HoneyDoDataModel(
                cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_CONTENT),
                cursor.getInt(INDEX_IMPORTANT)
        );
    }

    public Cursor fetchAllReminders() {
        Cursor mCursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_CONTENT, COL_IMPORTANT},
                null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //UPDATE
    public void updateReminder(HoneyDoDataModel honeyDoDataModel) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, honeyDoDataModel.getContent());
        values.put(COL_IMPORTANT, honeyDoDataModel.getImportant());
        mDb.update(TABLE_NAME, values,
                COL_ID + "=?", new String[]{String.valueOf(honeyDoDataModel.getId())});
    }

    //DELETE
    public void deleteReminderById(int nId) {
        mDb.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(nId)});
    }

    public void deleteAllReminders() {
        mDb.delete(TABLE_NAME, null, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
