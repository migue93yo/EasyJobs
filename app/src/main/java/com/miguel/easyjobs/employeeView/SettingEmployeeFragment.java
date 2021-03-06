package com.miguel.easyjobs.employeeView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.miguel.easyjobs.MainActivity;
import com.miguel.easyjobs.R;

public class SettingEmployeeFragment extends Fragment {

    private Switch notification;
    private Intent main;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_employee, container, false);

        main = new Intent(getActivity(), MainActivity.class);

        sp = getActivity().getSharedPreferences("logued", Context.MODE_PRIVATE);
        TextView closeSesion = (TextView) view.findViewById(R.id.setting_employee_session_close);
        closeSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.dialog_close_session))
                        .setMessage(getResources().getString(R.string.dialog_ask_close_sesion))
                        .setPositiveButton(getResources().getString(R.string.dialog_accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putBoolean("logued", false);
                                edit.putBoolean("notification", false);
                                edit.commit();

                                Intent notif = new Intent(getActivity(), NotificationsEmployeeService.class);
                                getActivity().stopService(notif);

                                getActivity().startActivity(main);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dialog_cancel), null);
                dialog.show();
            }
        });

        notification = (Switch) view.findViewById(R.id.setting_employee_notification_switch);
        if (sp.getBoolean("notification", true)) {
            notification.setChecked(true);
        }

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notif = new Intent(getActivity(), NotificationsEmployeeService.class);
                SharedPreferences.Editor edit = sp.edit();
                if (notification.isChecked()) {
                    getActivity().startService(notif);
                    edit.putBoolean("notification", true);
                    edit.commit();
                    Snackbar.make(v, getResources().getString(R.string.dialog_notification_enabled), Snackbar.LENGTH_SHORT).show();
                } else {
                    getActivity().stopService(notif);
                    edit.putBoolean("notification", false);
                    edit.commit();
                    Snackbar.make(v, getResources().getString(R.string.dialog_notification_disabled), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
