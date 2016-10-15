package com.miguel.easyjobs.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miguel.easyjobs.domain.Title;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TitlesTable {

    private TablesReaderDb openHelper;
    private SQLiteDatabase database;

    public TitlesTable(Context context) {
        openHelper = new TablesReaderDb(context);
        database = openHelper.getWritableDatabase();
    }

    public List<Title> findByEmail(String email) {
        List<Title> titleList = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM Titulo" +
                " WHERE trabajador_email = \"" + email + "\"", null);
        while (c.moveToNext()) {
            Title t = new Title();

            t.setId(c.getInt(c.getColumnIndex("id")));
            t.setEmail(c.getString(c.getColumnIndex("trabajador_email")));
            t.setTitle(c.getString(c.getColumnIndex("titulo")));
            t.setCenter(c.getString(c.getColumnIndex("centro")));
            t.setProvince(c.getString(c.getColumnIndex("provincia")));

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

            try {
                t.setDateStart(format.parse(c.getString(c.getColumnIndex("f_ini"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                t.setDateEnd(format.parse(c.getString(c.getColumnIndex("f_fin"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            titleList.add(t);
        }
        return titleList;
    }

    public long set(Title title) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateStart = format.format(title.getDateStart());
        String dateFinish = format.format(title.getDateEnd());

        ContentValues values = new ContentValues();

        values.put("trabajador_email", title.getEmail());
        values.put("titulo", title.getTitle());
        values.put("centro", title.getCenter());
        values.put("provincia", title.getProvince());
        values.put("f_ini", dateStart);
        values.put("f_fin", dateFinish);

        return database.insert("Titulo", null, values);
    }

    public long update(Title title) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateStart = format.format(title.getDateStart());
        String dateFinish = format.format(title.getDateEnd());

        ContentValues values = new ContentValues();

        values.put("titulo", title.getTitle());
        values.put("centro", title.getCenter());
        values.put("provincia", title.getProvince());
        values.put("f_ini", dateStart);
        values.put("f_fin", dateFinish);
        return database.update("Titulo", values, "id = " + String.valueOf(title.getId()), null);
    }

    public boolean delete(int id) {
        return database.delete("Titulo", "id = " + String.valueOf(id), null) > 0;
    }

}
