package com.miguel.easyjobs.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.miguel.easyjobs.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static EditText dateStart;
    private static EditText dateEnd;
    private static Activity context;

    public static int DATE_START_TYPE = 0;
    public static int DATE_END_TYPE = 1;
    public static int BIRTHDATE = 2;
    public static int OFFER = 3;
    private static int modeType;

    public static DatePickerFragment newInstance(Activity c, EditText ds, EditText de, int type) {
        dateStart = ds;
        dateEnd = de;
        context = c;
        modeType = type;
        return new DatePickerFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker datePicker = dialog.getDatePicker();


        if (modeType == OFFER) {
            datePicker.setMinDate(c.getTimeInMillis());
        } else {
            datePicker.setMaxDate(c.getTimeInMillis());
        }

        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (modeType == DATE_START_TYPE) {
            dateStart.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
            if (!dateEnd.getText().toString().isEmpty() && !dateValidator()) {
                dateStart.setText("");
                Toast.makeText(context, getResources().getString(R.string.toast_unvalidate_datestart), Toast.LENGTH_SHORT).show();
            }
        } else if (modeType == DATE_END_TYPE) {
            dateEnd.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
            if (!dateStart.getText().toString().isEmpty() && !dateValidator()) {
                dateEnd.setText("");
                Toast.makeText(context, getResources().getString(R.string.toast_unvalidate_datefinish), Toast.LENGTH_SHORT).show();
            }
        } else if (modeType == BIRTHDATE || modeType == OFFER) {
            dateStart.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
        }

    }

    private boolean dateValidator() {
        boolean ok = false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date ds = dateFormat.parse(dateStart.getText().toString());
            Date de = dateFormat.parse(dateEnd.getText().toString());

            if (ds.before(de)) {
                ok = true;
            }
        } catch (ParseException ex) {
            ex.getMessage();
        }
        return ok;
    }

}
