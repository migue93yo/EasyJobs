package com.miguel.easyjobs.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miguel.easyjobs.domain.Experience;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExperiencesTable {

    private TablesReaderDb openHelper;
    private SQLiteDatabase database;

    public ExperiencesTable(Context context) {
        openHelper = new TablesReaderDb(context);
        database = openHelper.getWritableDatabase();
    }

    public List<Experience> findByEmail(String email) {
        List<Experience> list = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM Experiencia" +
                " WHERE trabajador_email = \"" + email + "\"", null);

        while (c.moveToNext()) {
            Experience exp = new Experience();

            exp.setId(c.getInt(c.getColumnIndex("id")));
            exp.setEmail(c.getString(c.getColumnIndex("trabajador_email")));
            exp.setJob(c.getString(c.getColumnIndex("puesto")));
            exp.setCompany(c.getString(c.getColumnIndex("empresa")));
            exp.setProvince(c.getString(c.getColumnIndex("provincia")));

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

            try {
                exp.setDateStart(format.parse(c.getString(c.getColumnIndex("f_ini"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                exp.setDateEnd(format.parse(c.getString(c.getColumnIndex("f_fin"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            list.add(exp);
        }
        return list;
    }

    public boolean set(Experience experience) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateStart = format.format(experience.getDateStart());
        String dateFinish = format.format(experience.getDateEnd());

        ContentValues values = new ContentValues();

        values.put("trabajador_email", experience.getEmail());
        values.put("puesto", experience.getJob());
        values.put("empresa", experience.getCompany());
        values.put("provincia", experience.getProvince());
        values.put("f_ini", dateStart);
        values.put("f_fin", dateFinish);

        return database.insert("Experiencia", null, values) > 0;
    }

    public boolean update(Experience experience) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateStart = format.format(experience.getDateStart());
        String dateFinish = format.format(experience.getDateEnd());

        ContentValues values = new ContentValues();

        values.put("puesto", experience.getJob());
        values.put("empresa", experience.getCompany());
        values.put("provincia", experience.getProvince());
        values.put("f_ini", dateStart);
        values.put("f_fin", dateFinish);

        return database.update("Experiencia", values, "id = " + String.valueOf(experience.getId()), null) > 0;
    }

    public boolean delete(int id) {
        return database.delete("Experiencia", "id = " + String.valueOf(id), null) > 0;
    }

}
