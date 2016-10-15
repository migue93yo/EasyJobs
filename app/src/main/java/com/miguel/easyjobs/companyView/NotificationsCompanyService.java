package com.miguel.easyjobs.companyView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.miguel.easyjobs.R;
import com.miguel.easyjobs.domain.Offer;
import com.miguel.easyjobs.model.InscriptionsTable;
import com.miguel.easyjobs.model.OffersTable;

import java.util.ArrayList;
import java.util.List;

public class NotificationsCompanyService extends Service {
    private Context context;
    private SearchInscriptions searchInscriptions;

    public NotificationsCompanyService() {
        this.context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        searchInscriptions = new SearchInscriptions(intent.getStringExtra("email"));
        if (!searchInscriptions.getStatus().equals(AsyncTask.Status.RUNNING)) {
            searchInscriptions.execute();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!searchInscriptions.getStatus().equals(AsyncTask.Status.FINISHED))
            searchInscriptions.cancel(true);
    }

    private class SearchInscriptions extends AsyncTask {

        private OffersTable offersTable;
        private String email;
        private int idOffer;

        public SearchInscriptions(String email) {
            this.email = email;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            offersTable = new OffersTable(context);
            List<Integer> idOffers = offersTable.getIds(email);

            //Búcamos el número de inscripciones por id
            InscriptionsTable inscriptionsTable = new InscriptionsTable(context);
            List<Integer> countIds1 = new ArrayList<>();
            for (int id : idOffers) {
                countIds1.add(inscriptionsTable.countIds(id));
            }

            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (idOffers.size() > 0) {
                    List<Integer> countIds2 = new ArrayList<>();
                    for (int id : idOffers) {
                        countIds2.add(inscriptionsTable.countIds(id));
                    }

                    //Comparamos si ha aumentado el número de inscripciones
                    int num = 0;
                    for (int i = 0; i < countIds1.size(); i++) {
                        if (countIds1.get(i) < countIds2.get(i)) {
                            idOffer = idOffers.get(i);
                            num++;
                        }
                    }

                    if (num > 0)
                        publishProgress(num);
                }

                idOffers = offersTable.getIds(email);
                countIds1 = new ArrayList<>();
                for (int id : idOffers) {
                    countIds1.add(inscriptionsTable.countIds(id));
                }
            }
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            int num = (int) values[0];
            Intent intent = null;
            String message = null;
            if (num == 1) {
                Offer offer = offersTable.get(idOffer);
                message = "Hay nuevas inscripciones en una oferta";
                intent = new Intent(NotificationsCompanyService.this, MyOfferActivity.class);
                intent.putExtra("offer", offer);
                intent.putExtra("id", idOffer);
            } else {
                message = "Hay nuevas inscripciones en varias ofertas";
                intent = new Intent(NotificationsCompanyService.this, CompanyContainerActivity.class);
                intent.putExtra("fragment", CompanyContainerActivity.OFFERS_LIST);
            }

            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .setContentIntent(pending);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, notif.build());
        }
    }
}
