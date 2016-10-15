package com.miguel.easyjobs.util;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.miguel.easyjobs.R;

/**
 * Created by guadaltel on 9/06/16.
 */
public class Validator {

    private Activity context;

    public Validator(Activity context) {
        this.context = context;
    }

    public boolean titleValidator(EditText title) {
        boolean ok = false;
        String titleText = title.getText().toString();
        if (titleText.length() == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_title_empty), Toast.LENGTH_SHORT).show();
        } else if (titleText.length() > 150) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_unvalidate_title), Toast.LENGTH_SHORT).show();
        } else {
            ok = true;
        }
        return ok;
    }

    public boolean centerValidator(EditText center) {
        boolean ok = false;
        String centerText = center.getText().toString();
        if (centerText.length() == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_center_empty), Toast.LENGTH_SHORT).show();
        } else if (centerText.length() > 50) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_unvalidate_center), Toast.LENGTH_SHORT).show();
        } else {
            ok = true;
        }
        return ok;
    }

    public boolean provinceValidator(EditText province) {
        String provinceText = province.getText().toString();
        if (provinceText.length() == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_province_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean durationValidator(EditText duration) {
        boolean ok = false;
        String dateStartText = duration.getText().toString();
        if (dateStartText.equals("")) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_date_empty), Toast.LENGTH_SHORT).show();
        } else {
            ok = true;
        }
        return ok;
    }

    public boolean dateStartValidator(EditText dateStart) {
        boolean ok = false;
        String dateStartText = dateStart.getText().toString();
        if (dateStartText.equals("")) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_datestart_empty), Toast.LENGTH_SHORT).show();
        } else {
            ok = true;
        }
        return ok;
    }

    public boolean dateEndValidator(EditText dateFinish, Switch switchButton) {
        boolean ok = false;
        String dateFinishText = dateFinish.getText().toString();
        if (dateFinishText.equals("") && !switchButton.isChecked()) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_datefinish_empty), Toast.LENGTH_SHORT).show();
        } else {
            ok = true;
        }
        return ok;
    }

    public boolean nameValidator(EditText name) {
        if (name.getText().toString().length() == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_name_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean emailValidator(EditText email) {
        boolean ok = true;
        if (!email.getText().toString().contains("@")
                || !email.getText().toString().contains(".")
                || email.getText().toString().length() < 3
                || email.getText().toString().length() > 100) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_unvalidate_email), Toast.LENGTH_LONG).show();
            ok = false;
        }
        return ok;
    }

    public boolean passValidator(EditText pass) {
        boolean ok = true;
        if (pass.getText().toString().length() < 6 || pass.getText().toString().length() > 12) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_pass_range), Toast.LENGTH_LONG).show();
            ok = false;
        }
        return ok;
    }

    public boolean phoneValidator(EditText phone) {
        boolean ok = true;
        if (phone.getText().toString().length() < 9 || phone.getText().toString().length() > 10) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_incorrect_format_phone), Toast.LENGTH_LONG).show();
            ok = false;
        } else {
            try {
                Integer.parseInt(phone.getText().toString());
            } catch (NumberFormatException ex) {
                Toast.makeText(context, context.getResources().getString(R.string.toast_phone_number_only), Toast.LENGTH_LONG).show();
                ok = false;
            }
        }
        return ok;
    }

    public boolean jobValidator(EditText job) {
        boolean ok = false;
        String jobText = job.getText().toString();
        if (jobText.length() == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_job_empty), Toast.LENGTH_SHORT).show();
        } else if (jobText.length() > 50) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_unvalidate_job), Toast.LENGTH_SHORT).show();
        } else {
            ok = true;
        }
        return ok;
    }

    public boolean companyValidator(EditText company) {
        boolean ok = false;
        String companyText = company.getText().toString();
        if (companyText.length() == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_company_empty), Toast.LENGTH_SHORT).show();
        } else if (companyText.length() > 50) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_unvalidate_company), Toast.LENGTH_SHORT).show();
        } else {
            ok = true;
        }
        return ok;
    }

    public boolean descriptionValidator(EditText description) {
        boolean ok = false;
        String descriptionText = description.getText().toString();
        if (descriptionText.length() == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_description_empty), Toast.LENGTH_SHORT).show();
        } else {
            ok = true;
        }
        return ok;
    }

}
