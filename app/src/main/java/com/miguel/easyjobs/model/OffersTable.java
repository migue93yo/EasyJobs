package com.miguel.easyjobs.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.domain.SearchCriteria;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OffersTable {

    private TablesReaderDb openHelper;
    private SQLiteDatabase database;
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;

    public OffersTable(Context context) {
        openHelper = new TablesReaderDb(context);
        database = openHelper.getWritableDatabase();
    }

    public Offer get(int id) {
        Cursor c = database.rawQuery("SELECT * FROM Oferta WHERE id = " + id, null);
        c.moveToFirst();
        Offer offer = new Offer();
        offer.setId(c.getInt(c.getColumnIndex("id")));
        offer.setEmail(c.getString(c.getColumnIndex("empresa_email")));
        offer.setTitle(c.getString(c.getColumnIndex("titulo")));
        offer.setProvince(c.getString(c.getColumnIndex("provincia")));
        offer.setJob(c.getString(c.getColumnIndex("puesto")));
        offer.setDuration(Date.valueOf(c.getString(c.getColumnIndex("duracion"))));
        offer.setContractType(c.getString(c.getColumnIndex("tipo_contrato")));
        offer.setWorkDay(c.getString(c.getColumnIndex("jornada")));
        offer.setDescription(c.getString(c.getColumnIndex("descripcion")));
        if (c.getInt(c.getColumnIndex("estado")) == 1) {
            offer.setState(true);
        } else {
            offer.setState(false);
        }

        return offer;
    }

    public ArrayList<Offer> findByCriteria(SearchCriteria criteria) {

        String sql = "SELECT * FROM Oferta " +
                "WHERE estado = 1";

        if (!criteria.getKeyword().equals("")) {
            sql = sql.concat(" AND titulo LIKE \"%" + criteria.getKeyword() + "%\"");
        }

        if (!criteria.getProvince().equals("")) {
            sql = sql.concat(" AND provincia LIKE \"%" + criteria.getProvince() + "%\"");
        }

        if (!criteria.getJob().equals("")) {
            sql = sql.concat(" AND puesto LIKE \"%" + criteria.getJob() + "%\"");

        }

        ArrayList<Offer> list = new ArrayList<>();
        Cursor c = database.rawQuery(sql, null);
        while (c.moveToNext()) {
            Offer offer = new Offer();
            offer.setId(c.getInt(c.getColumnIndex("id")));
            offer.setEmail(c.getString(c.getColumnIndex("empresa_email")));
            offer.setTitle(c.getString(c.getColumnIndex("titulo")));
            offer.setProvince(c.getString(c.getColumnIndex("provincia")));
            offer.setJob(c.getString(c.getColumnIndex("puesto")));
            offer.setDuration(Date.valueOf(c.getString(c.getColumnIndex("duracion"))));
            offer.setContractType(c.getString(c.getColumnIndex("tipo_contrato")));
            offer.setWorkDay(c.getString(c.getColumnIndex("jornada")));
            offer.setDescription(c.getString(c.getColumnIndex("descripcion")));
            if (c.getInt(c.getColumnIndex("estado")) == 1) {
                offer.setState(true);
            } else {
                offer.setState(false);
            }
            list.add(offer);
        }
        return list;
    }

    public ArrayList<Offer> findByState(int state) {
        ArrayList<Offer> list = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM Oferta WHERE estado = " + String.valueOf(state), null);
        while (c.moveToNext()) {
            Offer offer = new Offer();
            offer.setId(c.getInt(c.getColumnIndex("id")));
            offer.setEmail(c.getString(c.getColumnIndex("empresa_email")));
            offer.setTitle(c.getString(c.getColumnIndex("titulo")));
            offer.setProvince(c.getString(c.getColumnIndex("provincia")));
            offer.setJob(c.getString(c.getColumnIndex("puesto")));
            offer.setDuration(Date.valueOf(c.getString(c.getColumnIndex("duracion"))));
            offer.setContractType(c.getString(c.getColumnIndex("tipo_contrato")));
            offer.setWorkDay(c.getString(c.getColumnIndex("jornada")));
            offer.setDescription(c.getString(c.getColumnIndex("descripcion")));
            if (c.getInt(c.getColumnIndex("estado")) == ACTIVE) {
                offer.setState(true);
            } else {
                offer.setState(false);
            }
            list.add(offer);
        }
        return list;
    }

    public List<Integer> getIds(String email) {
        Cursor c = database.rawQuery("SELECT id FROM Oferta " +
                "WHERE empresa_email = \"" + email + "\"", null);

        ArrayList<Integer> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(c.getInt(c.getColumnIndex("id")));
        }
        return list;
    }

    public boolean set(Offer offer) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String duration = format.format(offer.getDuration());

        ContentValues values = new ContentValues();

        values.put("empresa_email", offer.getEmail());
        values.put("titulo", offer.getTitle());
        values.put("provincia", offer.getProvince());
        values.put("puesto", offer.getJob());
        values.put("duracion", duration);
        values.put("tipo_contrato", offer.getContractType());
        values.put("jornada", offer.getWorkDay());
        values.put("descripcion", offer.getDescription());
        if (offer.isState())
            values.put("estado", 1);
        else
            values.put("estado", 0);

        return database.insert("Oferta", null, values) > 0;
    }

    public boolean setVisibility(int id, boolean visibility) {
        ContentValues values = new ContentValues();
        if (visibility)
            values.put("estado", 1);
        else
            values.put("estado", 0);
        return database.update("Oferta", values, "id = " + String.valueOf(id), null) > 0;
    }

    public boolean delete(int id) {
        return database.delete("Oferta", "id = " + String.valueOf(id), null) == 1;
    }

}
