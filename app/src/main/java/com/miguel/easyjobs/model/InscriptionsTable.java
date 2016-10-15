package com.miguel.easyjobs.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miguel.easyjobs.domain.Inscription;

import java.util.ArrayList;
import java.util.List;

public class InscriptionsTable {
    private TablesReaderDb tablesReaderDB;
    private SQLiteDatabase database;

    public InscriptionsTable(Context context) {
        tablesReaderDB = new TablesReaderDb(context);
        database = tablesReaderDB.getWritableDatabase();
    }

    public long countEmail(int id) {
        Cursor c = database.rawQuery("SELECT COUNT(trabajador_email) FROM Inscripcion" +
                " WHERE oferta_id = " + String.valueOf(id), null);
        c.moveToFirst();
        return c.getLong(0);
    }

    public int countIds(int id) {
        Cursor c = database.rawQuery("SELECT COUNT(oferta_id) FROM Inscripcion" +
                " WHERE oferta_id = " + String.valueOf(id), null);
        c.moveToFirst();
        return c.getInt(0);
    }

    public Inscription get(int id, String email) {
        Cursor c = database.rawQuery("SELECT estado FROM Inscripcion" +
                " WHERE oferta_id = " + String.valueOf(id) +
                " AND trabajador_email = \"" + email + "\"", null);

        Inscription inscription = new Inscription();
        if (c.moveToFirst()) {
            inscription.setEmail(email);
            inscription.setId(id);
            inscription.setState(c.getString(c.getColumnIndex("estado")));
        }
        return inscription;
    }

    public boolean set(Inscription inscription) {
        ContentValues values = new ContentValues();
        values.put("oferta_id", inscription.getId());
        values.put("trabajador_email", inscription.getEmail());

        long num = database.insert("Inscripcion", null, values);
        return num > 0;
    }

    public boolean delete(int id) {
        return database.delete("Inscripcion", "oferta_id = " + String.valueOf(id), null) > 0;
    }

    public boolean delete(int id, String email) {
        long num = database.delete("Inscripcion", "oferta_id = " + String.valueOf(id)
                + " AND trabajador_email = \"" + email + "\"", null);

        return num > 0;
    }

    public boolean isCheck(int id, String email) {
        Cursor c = database.rawQuery("SELECT oferta_id FROM Inscripcion " +
                "WHERE oferta_id = " + String.valueOf(id) + " " +
                "AND trabajador_email = \"" + email + "\"", null);
        return c.moveToFirst();
    }

    public List<Inscription> findByEmail(String email) {
        Cursor c = database.rawQuery("SELECT * FROM Inscripcion " +
                "WHERE trabajador_email = \"" + email + "\"", null);
        List<Inscription> list = new ArrayList<>();
        while (c.moveToNext()) {
            Inscription inscription = new Inscription();
            inscription.setEmail(email);
            inscription.setId(c.getInt(c.getColumnIndex("oferta_id")));
            inscription.setState(c.getString(c.getColumnIndex("estado")));
            list.add(inscription);
        }
        return list;
    }

    public List<Inscription> findById(int id) {
        Cursor c = database.rawQuery("SELECT * FROM Inscripcion " +
                "WHERE oferta_id = " + String.valueOf(id), null);
        List<Inscription> list = new ArrayList<>();
        while (c.moveToNext()) {
            Inscription inscription = new Inscription();
            inscription.setEmail(c.getString(c.getColumnIndex("trabajador_email")));
            inscription.setId(id);
            inscription.setState(c.getString(c.getColumnIndex("estado")));
            list.add(inscription);
        }
        return list;
    }

    public boolean update(Inscription inscription) {
        ContentValues values = new ContentValues();
        values.put("estado", inscription.getState());

        return database.update("Inscripcion", values, "trabajador_email = \"" + inscription.getEmail() + "\"" +
                " AND oferta_id = " + inscription.getId(), null) > 0;
    }

}
