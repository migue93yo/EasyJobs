package com.miguel.easyjobs.employeeView;

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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.miguel.easyjobs.R;
import com.miguel.easyjobs.adapters.ExperiencesAdapter;
import com.miguel.easyjobs.adapters.TitlesAdapter;
import com.miguel.easyjobs.domain.Employee;
import com.miguel.easyjobs.domain.Experience;
import com.miguel.easyjobs.domain.Title;
import com.miguel.easyjobs.model.EmployeesTable;
import com.miguel.easyjobs.model.ExperiencesTable;
import com.miguel.easyjobs.model.TitlesTable;
import com.miguel.easyjobs.util.PictureSetting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfileEmployeeFragment extends Fragment {

    private TextView drawerName;
    private TextView drawerEmail;
    private ImageView drawerImage;
    private FABToolbarLayout morph;
    private FloatingActionButton fab;
    private SharedPreferences sp;

    private Intent newExperience;
    private Intent newTitle;

    private ImageView picture;
    private TextView fullname;
    private TextView province;
    private TextView birthday;
    private TextView phone;
    private RecyclerView listViewExp;
    private RecyclerView listViewtitles;
    private ImageButton editProfile;
    private CardView cardExperiences;
    private CardView cardTitles;
    private TextView empty;

    private String fileImage;
    private String email;
    private Uri uri;

    private final int PHOTO_CODE = 100;
    private final int GALLERY_CODE = 200;
    private final int READ_EXTERNAL_CODE = 300;

    private final int CHOOSE_PHOTO = 0;
    private final int CHOOSE_GALLERY = 1;
    private final int CHOOSE_CANCEL = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_employee, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.float_button_extended);
        morph = (FABToolbarLayout) view.findViewById(R.id.fabtoolbar);

        View uno = view.findViewById(R.id.action_experience);
        View dos = view.findViewById(R.id.action_education);

        fab.setOnClickListener(new onClickButton());
        uno.setOnClickListener(new onClickButton());
        dos.setOnClickListener(new onClickButton());

        newExperience = new Intent(getActivity(), NewExperienceActivity.class);
        newTitle = new Intent(getActivity(), NewTitleActivity.class);

        picture = (ImageView) view.findViewById(R.id.profile_employee_image);
        editProfile = (ImageButton) view.findViewById(R.id.profile_employee_edit);
        fullname = (TextView) view.findViewById(R.id.profile_employee_fullname);
        province = (TextView) view.findViewById(R.id.profile_employee_province);
        birthday = (TextView) view.findViewById(R.id.profile_employee_birthday);
        phone = (TextView) view.findViewById(R.id.profile_employee_phone);
        listViewExp = (RecyclerView) view.findViewById(R.id.profile_employee_experienceslist);
        listViewtitles = (RecyclerView) view.findViewById(R.id.profile_employee_educationlist);
        cardExperiences = (CardView) view.findViewById(R.id.profile_employee_cardview_experiences);
        cardTitles = (CardView) view.findViewById(R.id.profile_employee_cardview_titles);
        empty = (TextView) view.findViewById(R.id.profile_employee_empty);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view_employee);

        View drawerHeader = navigationView.getHeaderView(0);
        drawerName = (TextView) drawerHeader.findViewById(R.id.drawer_name);
        drawerEmail = (TextView) drawerHeader.findViewById(R.id.drawer_email);
        drawerImage = (ImageView) drawerHeader.findViewById(R.id.drawer_photo);

        sp = getActivity().getSharedPreferences("logued", Context.MODE_PRIVATE);
        email = sp.getString("email", null);
        fileImage = email + ".png";

        new SearchEmployee().execute();
        return view;
    }

    public class onClickButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.float_button_extended) {
                morph.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        morph.hide();
                    }
                }, 3000);
            }

            morph.hide();

            if (v.getId() == R.id.action_experience) {
                startActivity(newExperience);
                getActivity().finish();
            } else if (v.getId() == R.id.action_education) {
                startActivity(newTitle);
                getActivity().finish();
            }
        }
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
                        picture.setImageBitmap(bitmap);
                        drawerImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GALLERY_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    File file = new File(PictureSetting.RUTE_IMAGE);
                    if (!file.exists())
                        file.mkdir();

                    Uri path = data.getData();
                    ContentResolver cr = getActivity().getContentResolver();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(cr, path);
                        bitmap = PictureSetting.getNormalOrientation(bitmap, PictureSetting.getPath(getActivity(), path));
                        bitmap = PictureSetting.cropBitmap(bitmap);
                        bitmap = PictureSetting.resizePicture(bitmap, 640, 640);
                        bitmap = PictureSetting.getRoundedCornerBitmap(bitmap);
                        PictureSetting.savePicture(bitmap, fileImage);
                        picture.setImageBitmap(bitmap);
                        drawerImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PHOTO_CODE || requestCode != READ_EXTERNAL_CODE)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class SearchEmployee extends AsyncTask<String, String, String> {

        private SharedPreferences.Editor edit;
        private Employee emp;

        @Override
        protected String doInBackground(String... params) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Cargar datos del usuario
                    EmployeesTable empTable = new EmployeesTable(getActivity());
                    emp = empTable.get(email);

                    fullname.setText(emp.getFullname());
                    province.setText(emp.getProvince());
                    phone.setText(emp.getPhone());

                    int year = getAge(emp.getBirthdate());

                    birthday.setText(String.valueOf(year) + " "
                            + getResources().getString(R.string.year));
                    drawerEmail.setText(email);
                    drawerName.setText(fullname.getText());

                    //Editar datos de perfil
                    editProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("employee", emp);
                            Intent i = new Intent(getActivity(), EditProfileEmployeeActivity.class);
                            i.putExtras(bundle);
                            startActivity(i);
                            getActivity().finish();
                        }
                    });

                    //Cargar foto
                    Bitmap bitmap = PictureSetting.loadPicture(getActivity(), email, R.drawable.buddyicon);
                    picture.setImageBitmap(bitmap);
                    drawerImage.setImageBitmap(bitmap);

                    File rute = new File(PictureSetting.RUTE_IMAGE + File.separator + sp.getString("email", "") + ".png");
                    uri = Uri.fromFile(rute);
                    picture.setOnClickListener(new View.OnClickListener() {
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
                                                //Dar permisos para usar la cámara
                                                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                                                        Manifest.permission.CAMERA);
                                                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                                    openCamera();
                                                } else {
                                                    ActivityCompat.requestPermissions(getActivity(),
                                                            new String[]{Manifest.permission.CAMERA},
                                                            PHOTO_CODE);
                                                }

                                                //Dar permisos para leer en la SD
                                                permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                                                        Manifest.permission.READ_EXTERNAL_STORAGE);
                                                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(getActivity(),
                                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                            READ_EXTERNAL_CODE);
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

                    //Cargar la lista de experiencias
                    RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity());
                    listViewExp.setLayoutManager(lManager);

                    ExperiencesTable expTable = new ExperiencesTable(getActivity());
                    final List<Experience> experiencesList = expTable.findByEmail(sp.getString("email", null));
                    ExperiencesAdapter expAdapter = new ExperiencesAdapter(getActivity(), experiencesList);
                    listViewExp.setAdapter(expAdapter);

                    if (listViewExp.getAdapter().getItemCount() == 0) {
                        cardExperiences.setVisibility(View.GONE);
                    }

                    expAdapter.setOnItemClickListener(new ExperiencesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("experience", experiencesList.get(position));
                            Intent i = new Intent(getActivity(), EditExperienceActivity.class);
                            i.putExtras(bundle);
                            startActivity(i);
                            getActivity().finish();
                        }
                    });

                    //Cargar la lista de títulos
                    RecyclerView.LayoutManager lManager2 = new LinearLayoutManager(getActivity());
                    listViewtitles.setLayoutManager(lManager2);

                    TitlesTable titlesTable = new TitlesTable(getActivity());
                    final List<Title> titlesList = titlesTable.findByEmail(email);
                    TitlesAdapter titleAdapter = new TitlesAdapter(getActivity(), titlesList);
                    listViewtitles.setAdapter(titleAdapter);

                    titleAdapter.setOnItemClickListener(new TitlesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("title", titlesList.get(position));
                            Intent i = new Intent(getActivity(), EditTitleActivity.class);
                            i.putExtras(bundle);
                            startActivity(i);
                            getActivity().finish();
                        }
                    });

                    if (listViewtitles.getAdapter().getItemCount() == 0) {
                        cardTitles.setVisibility(View.GONE);
                    }

                    if (cardExperiences.getVisibility() == View.GONE
                            && cardTitles.getVisibility() == View.GONE) {
                        empty.setVisibility(View.VISIBLE);
                    }

                    //Activar notificaciones
                    edit = sp.edit();
                    if (!sp.getBoolean("logued", false)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                                .setTitle(getResources().getString(R.string.app_name))
                                .setMessage(getResources().getString(R.string.dialog_ask_notification))
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getActivity(), NotificationsEmployeeService.class);
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
                        Intent i = new Intent(getActivity(), NotificationsEmployeeService.class);
                        getActivity().startService(i);
                    }

                }
            });
            return null;
        }

    }

    private int getAge(Date birthdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        int myBirthday;
        int today;

        myBirthday = Integer.valueOf(dateFormat.format((birthdate)));
        today = Integer.parseInt(dateFormat.format(new Date()));
        return (today - myBirthday) / 10000;
    }

}
