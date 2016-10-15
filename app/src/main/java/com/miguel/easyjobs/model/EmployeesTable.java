package com.miguel.easyjobs.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miguel.easyjobs.domain.Employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EmployeesTable {

    private TablesReaderDb openHelper;
    private SQLiteDatabase database;

    public EmployeesTable(Context context) {
        openHelper = new TablesReaderDb(context);
        database = openHelper.getWritableDatabase();
    }

    public boolean exist(String email) {
        boolean exist = false;
        if (email.length() > 0) {
            Cursor c = database.rawQuery(
                    "SELECT COUNT(email) FROM Trabajador " +
                            "WHERE email = \"" + email + "\"", null);

            c.moveToFirst();
            final int existEmployee = c.getInt(0);

            exist = (existEmployee == 1);
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
                    "SELECT COUNT(email) FROM Trabajador " +
                            "WHERE email = \"" + email + "\"" +
                            " AND contrasenia = \"" + pass + "\"", null);

            c.moveToFirst();
            final int existEmployee = c.getInt(0);

            return existEmployee == 1;
        }
        return false;
    }

    public Employee get(String email) {
        Cursor c = database.rawQuery(
                "SELECT * FROM Trabajador " +
                        "WHERE email = \"" + email + "\"", null);
        c.moveToFirst();

        Employee emp = new Employee();
        emp.setEmail(c.getString(c.getColumnIndex("email")));
        emp.setPass(c.getString(c.getColumnIndex("contrasenia")));
        emp.setFullname(c.getString(c.getColumnIndex("nombre")));
        emp.setProvince(c.getString(c.getColumnIndex("provincia")));
        emp.setPhone(c.getString(c.getColumnIndex("telefono")));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            emp.setBirthdate(format.parse(c.getString(c.getColumnIndex("f_nac"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return emp;
    }

    public boolean set(Employee employee) {
        ContentValues values = new ContentValues();

        values.put("email", employee.getEmail());
        values.put("contrasenia", employee.getPass());
        values.put("nombre", employee.getFullname());
        values.put("provincia", employee.getProvince());
        values.put("telefono", employee.getPhone());

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date = format.format(employee.getBirthdate());
        values.put("f_nac", date);

        return database.insert("Trabajador", null, values) > 0;
    }

    public int update(Employee employee) {

        ContentValues values = new ContentValues();
        values.put("nombre", employee.getFullname());
        values.put("provincia", employee.getProvince());
        values.put("telefono", employee.getPhone());

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date = format.format(employee.getBirthdate());
        values.put("f_nac", date);

        return database.update("Trabajador", values, "email = \"" + employee.getEmail() + "\"", null);
    }

}
