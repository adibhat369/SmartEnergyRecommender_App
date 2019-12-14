package com.monash.user.smarter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.util.Date;

public class DBManager {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usage.db";
    private final Context context;
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBStructure.tableEntry.TABLE_NAME + " (" +
                    DBStructure.tableEntry.COLUMN_ID + INTEGER_TYPE +" PRIMARY KEY," +
                    DBStructure.tableEntry.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_HOUR + INTEGER_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_FRIDGE + REAL_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_AC + REAL_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_WASHINGMACHINE + REAL_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_TEMP + INTEGER_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_RES + INTEGER_TYPE + " );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBStructure.tableEntry.TABLE_NAME;

    String[] columns = {
            DBStructure.tableEntry.COLUMN_ID,
            DBStructure.tableEntry.COLUMN_DATE,
            DBStructure.tableEntry.COLUMN_HOUR,
            DBStructure.tableEntry.COLUMN_FRIDGE,
            DBStructure.tableEntry.COLUMN_AC,
            DBStructure.tableEntry.COLUMN_WASHINGMACHINE,
            DBStructure.tableEntry.COLUMN_TEMP,
            DBStructure.tableEntry.COLUMN_RES,};


    private MySQLiteOpenHelper myDBHelper;
    private SQLiteDatabase db;
    public DBManager(Context ctx) {
        this.context = ctx;
        myDBHelper = new MySQLiteOpenHelper(context);
    }

    public DBManager open() throws SQLException {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        myDBHelper.close();
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }

    public long insertUsagedate(Integer id, String udate, Integer hour, Float fridge, Float ac, Float wm, Integer temp,Integer resid) {

        ContentValues values = new ContentValues();

        values.put(DBStructure.tableEntry.COLUMN_ID, id);
        values.put(DBStructure.tableEntry.COLUMN_DATE, udate);
        values.put(DBStructure.tableEntry.COLUMN_HOUR, hour);
        values.put(DBStructure.tableEntry.COLUMN_FRIDGE, fridge);
        values.put(DBStructure.tableEntry.COLUMN_AC, ac);
        values.put(DBStructure.tableEntry.COLUMN_WASHINGMACHINE, wm);
        values.put(DBStructure.tableEntry.COLUMN_TEMP, temp);
        values.put(DBStructure.tableEntry.COLUMN_RES, resid);

        return db.insert(DBStructure.tableEntry.TABLE_NAME, null, values);
    }

    public int deleteUsagedate(String rowId) {
        String[] selectionArgs = { String.valueOf(rowId) };
        String selection = DBStructure.tableEntry.COLUMN_RES + " LIKE ?";
        return db.delete(DBStructure.tableEntry.TABLE_NAME, selection,selectionArgs );
    }

    public Cursor getAllUsers() {
        try {
            return db.query(DBStructure.tableEntry.TABLE_NAME, columns, null, null, null, null, null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
