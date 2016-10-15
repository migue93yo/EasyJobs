package com.miguel.easyjobs.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miguel.easyjobs.domain.Company;

public class CompanyTable {

    private TablesReaderDb openHelper;
    private SQLiteDatabase database;

    public CompanyTable(Context context) {
        openHelper = new TablesReaderDb(context);
        database = openHelper.getWritableDatabase();
    }

    public boolean exist(String email) {
        boolean exist = false;
        if (email.length() > 0) {
            Cursor c = database.rawQuery(
                    "SELECT COUNT(email) FROM Empresa " +
                            "WHERE email = \"" + email + "\"", null);
            c.moveToFirst();
            final int existCompany = c.getInt(0);

            exist = (existCompany == 1);
        }
        return exist;
    }

    public boolean exist(String email, String pass) {

        boolean emptyPass = false;

        if (pass.length() == 0) {
            emptyPass = true;
        }

        if (!emptyPass) {
            Cursor c = database.rawQuery(
                    "SELECT COUNT(email) FROM Empresa " +
                            "WHERE email = \"" + email + "\" " +
                            "AND contrasenia = \"" + pass + "\"", null);
            c.moveToFirst();
            final int existCompany = c.getInt(0);

            return existCompany == 1;
        }
        return false;
    }

    public void set(Company company) {
        ContentValues values = new ContentValues();

        values.put("email", company.getEmail());
        values.put("contrasenia", company.getPass());
        values.put("nombre", company.getName());
        values.put("provincia", company.getProvince());

        database.insert("Empresa", null, values);
    }

    public Company get(String email) {
        Cursor c = database.rawQuery(
                "SELECT * FROM Empresa " +
                        "WHERE email = \"" + email + "\"", null);
        Company company = new Company();
        if (c.moveToFirst()) {
            company.setEmail(c.getString(c.getColumnIndex("email")));
            company.setPass(c.getString(c.getColumnIndex("contrasenia")));
            company.setName(c.getString(c.getColumnIndex("nombre")));
            company.setProvince(c.getString(c.getColumnIndex("provincia")));
            company.setDescription(c.getString(c.getColumnIndex("descripcion")));
        }

        return company;
    }

    public String getName(String email) {
        Cursor c = database.rawQuery("SELECT nombre FROM Empresa " +
                "WHERE email = \"" + email + "\"", null);
        String name = null;
        if (c.moveToFirst()) {
            name = c.getString(0);
        }
        return name;
    }

    public boolean update(Company company) {

        ContentValues values = new ContentValues();
        values.put("nombre", company.getName());
        values.put("provincia", company.getProvince());
        values.put("descripcion", company.getDescription());

        return database.update("Empresa", values, "email = \"" + company.getEmail() + "\"", null) > 0;
    }

}
