package com.miguel.easyjobs.companyView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Company;
import com.miguel.easyjobs.model.CompanyTable;
import com.miguel.easyjobs.util.PictureSetting;

import java.io.File;
import java.io.IOException;

public class ProfileCompanyFragment extends Fragment {

    private TextView drawerName;
    private TextView drawerEmail;
    private ImageView drawerImage;

    private ImageView imageView;
    private TextView fullname;
    private TextView province;
    private TextView description;
    private ImageButton editProfile;

    private String fileImage;
    private String email;
    private Uri uri;

    private SharedPreferences sp;

    private final int PHOTO_CODE = 100;
    private final int GALLERY_CODE = 200;

    private final int CHOOSE_PHOTO = 0;
    private final int CHOOSE_GALLERY = 1;
    private final int CHOOSE_CANCEL = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_company, container, false);

        imageView = (ImageView) view.findViewById(R.id.profile_company_image);
        editProfile = (ImageButton) view.findViewById(R.id.profile_company_edit);
        fullname = (TextView) view.findViewById(R.id.profile_company_name);
        province = (TextView) view.findViewById(R.id.profile_company_province);
        description = (TextView) view.findViewById(R.id.profile_company_description_contain);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view_company);

        View drawerHeader = navigationView.getHeaderView(0);
        drawerName = (TextView) drawerHeader.findViewById(R.id.drawer_name);
        drawerEmail = (TextView) drawerHeader.findViewById(R.id.drawer_email);
        drawerImage = (ImageView) drawerHeader.findViewById(R.id.drawer_photo);

        sp = getActivity().getSharedPreferences("logued", Context.MODE_PRIVATE);
        email = sp.getString("email", null);
        fileImage = email + ".png";

        new SearchCompany().execute();
        return view;
    }

    private void openCamera() {
        File file = new File(PictureSetting.RUTE_IMAGE);
        if (!file.exists())
            file.mkdir();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        try {
            startActivityForResult(intent, PHOTO_CODE);
        } catch (SecurityException ex) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.camera))
                    .setMessage(getResources().getString(R.string.camera_not_permise))
                    .setPositiveButton(getResources().getString(R.string.dialog_accept), null);
            dialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;
        switch (requestCode) {
            case PHOTO_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    ContentResolver cr = getActivity().getContentResolver();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                        bitmap = PictureSetting.getNormalOrientation(bitmap, PictureSetting.RUTE_IMAGE + File.separator + fileImage);
                        bitmap = PictureSetting.cropBitmap(bitmap);
                        bitmap = PictureSetting.resizePicture(bitmap, 640, 640);
                        bitmap = PictureSetting.getRoundedCornerBitmap(bitmap);
                        PictureSetting.savePicture(bitmap, fileImage);
                        imageView.setImageBitmap(bitmap);
                        drawerImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GALLERY_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri path = data.getData();
                    ContentResolver cr = getActivity().getContentResolver();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(cr, path);
                        bitmap = PictureSetting.getNormalOrientation(bitmap, PictureSetting.getPath(getActivity(), path));
                        bitmap = PictureSetting.cropBitmap(bitmap);
                        bitmap = PictureSetting.resizePicture(bitmap, 640, 640);
                        bitmap = PictureSetting.getRoundedCornerBitmap(bitmap);
                        PictureSetting.savePicture(bitmap, fileImage);
                        imageView.setImageBitmap(bitmap);
                        drawerImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PHOTO_CODE)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private class SearchCompany extends AsyncTask<String, String, String> {

        private Company company;
        private SharedPreferences.Editor edit;

        @Override
        protected String doInBackground(String... params) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Cargar datos del usuario
                    CompanyTable table = new CompanyTable(getActivity());
                    company = table.get(email);

                    fullname.setText(company.getName());
                    province.setText(company.getProvince());
                    description.setText(company.getDescription());

                    drawerEmail.setText(email);
                    drawerName.setText(fullname.getText());

                    //Editar datos de perfil
                    editProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), EditProfileCompanyActivity.class);
                            i.putExtra("company", company);
                            startActivity(i);
                            getActivity().finish();
                        }
                    });

                    //Cargar foto
                    Bitmap bitmap = PictureSetting.loadPicture(getActivity(), company.getEmail(), R.drawable.buddyicon);
                    imageView.setImageBitmap(bitmap);
                    drawerImage.setImageBitmap(bitmap);

                    File newFile = new File(PictureSetting.RUTE_IMAGE + File.separator + fileImage);
                    uri = Uri.fromFile(newFile);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String[] options = {getResources().getString(R.string.making_photo)
                                    , getResources().getString(R.string.choose_gallery)
                                    , getResources().getString(R.string.dialog_cancel)};
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                                    .setTitle(getResources().getString(R.string.choose_an_option))
                                    .setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int position) {
                                            if (position == CHOOSE_PHOTO) {
                                                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                                                        Manifest.permission.CAMERA);
                                                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                                    openCamera();
                                                } else {
                                                    ActivityCompat.requestPermissions(getActivity(),
                                                            new String[]{Manifest.permission.CAMERA},
                                                            PHOTO_CODE);
                                                }
                                            } else if (position == CHOOSE_GALLERY) {
                                                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                                startActivityForResult(gallery, GALLERY_CODE);
                                            } else if (position == CHOOSE_CANCEL) {
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                            dialog.show();
                        }
                    });

                    //Activar notificaciones
                    edit = sp.edit();
                    if (!sp.getBoolean("logued", false)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                                .setTitle(getResources().getString(R.string.app_name))
                                .setMessage(getResources().getString(R.string.dialog_ask_notification))
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getActivity(), NotificationsCompanyService.class);
                                        i.putExtra("email", email);
                                        getActivity().startService(i);
                                        edit.putBoolean("notification", true);
                                        edit.commit();
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        edit.putBoolean("notification", false);
                                        edit.commit();
                                    }
                                });
                        dialog.show();

                        edit.putBoolean("logued", true);
                        edit.commit();
                    } else if (sp.getBoolean("notification", true)) {
                        Intent i = new Intent(getActivity(), NotificationsCompanyService.class);
                        i.putExtra("email", email);
                        getActivity().startService(i);
                    }

                }
            });
            return null;
        }

    }

}
