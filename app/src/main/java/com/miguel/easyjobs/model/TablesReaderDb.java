package com.miguel.easyjobs.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TablesReaderDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "easyjobs.db";
    public static final int DATABASE_VERSION = 1;

    public TablesReaderDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creación de tablas
        db.execSQL(TablesDataSource.CREATE_EMPLOYEE_TABLE);
        db.execSQL(TablesDataSource.CREATE_COMPANY_TABLE);
        db.execSQL(TablesDataSource.CREATE_OFFER_TABLE);
        db.execSQL(TablesDataSource.CREATE_INSCRIPTION_TABLE);
        db.execSQL(TablesDataSource.CREATE_EXPERIENCE_TABLE);
        db.execSQL(TablesDataSource.CREATE_TITLE_TABLE);

        //Insersión de usuario admin
        db.execSQL(TablesDataSource.ADMIN_EMPLOYEE);
        db.execSQL(TablesDataSource.ADMIN_COMPANY);
        db.execSQL(TablesDataSource.ADMIN_EXPERIENCES);
        db.execSQL(TablesDataSource.ADMIN_EXPERIENCES_TODAY);
        db.execSQL(TablesDataSource.ADMIN_TITLES);
        db.execSQL(TablesDataSource.ADMIN_NEW_OFFERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
