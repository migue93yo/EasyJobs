package com.miguel.easyjobs.employeeView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Inscription;
import com.miguel.easyjobs.model.InscriptionsTable;

import java.util.List;

public class NotificationsEmployeeService extends Service {

    private Context context;
    private OfferState offerState;

    public NotificationsEmployeeService() {
        this.context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        offerState = new OfferState();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!offerState.getStatus().equals(AsyncTask.Status.RUNNING)) {
            offerState.execute();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!offerState.getStatus().equals(AsyncTask.Status.FINISHED))
            offerState.cancel(true);
    }

    private class OfferState extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            SharedPreferences sp = getSharedPreferences("logued", Context.MODE_PRIVATE);
            InscriptionsTable table = new InscriptionsTable(context);
            List<Inscription> list = table.findByEmail(sp.getString("email", ""));
            while (true) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<Inscription> checkList = table.findByEmail(sp.getString("email", ""));

                for (int i = 0; i < checkList.size(); i++) {
                    Inscription inscr = list.get(i);
                    Inscription check = checkList.get(i);

                    if (!inscr.getState().equals(check.getState())) {
                        publishProgress();
                    }
                }

                list = table.findByEmail(sp.getString("email", ""));
            }
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            Intent intent = new Intent(context, CandidaturesFragment.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(getResources().getString(R.string.dialog_change_candidature))
                    .setContentIntent(pending);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, notif.build());
        }
    }

}
