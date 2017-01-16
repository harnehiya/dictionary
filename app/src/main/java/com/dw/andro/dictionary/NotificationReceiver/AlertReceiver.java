package com.dw.andro.dictionary.NotificationReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dw.andro.dictionary.Activity.MainActivity;
import com.dw.andro.dictionary.DataObject.WordDetailObject;
import com.dw.andro.dictionary.DataObject.WordObject;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.ServiceData;
import com.dw.andro.dictionary.database.DatabaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dvayweb on 25/02/16.
 */
public class AlertReceiver extends BroadcastReceiver {

    String title;
    DatabaseAdapter databaseAdapter;
    WordObject wordObject;
    String dateTime,wod;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        title=context.getResources().getString(R.string.app_name);
        databaseAdapter=new DatabaseAdapter(context);
        wordObject = new WordObject();
        wordObject = databaseAdapter.getWOD();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        dateTime = df.format(c.getTime()).toString();

        if (isNetworkAvailable(context)&&!dateTime.equals(wordObject.getDateTime())) {
            new AsyncTaskWOD().execute();
        }

    }

    private void createNotification(Context context) {
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText("Hello " +"Today's Word of The Day is " +wod)
                .setDefaults(NotificationCompat.DEFAULT_SOUND);

        mBuilder.setContentIntent(pIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setAutoCancel(true);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }

    public class AsyncTaskWOD extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {

            return ServiceData.getWordOfTheDay();
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            saveWOD(result);
        }
    }

    private void saveWOD(String result) {

        if (result != null) {
            try {

                JSONObject rootObject = new JSONObject(result);

                wod=rootObject.getString("Word");
                Long id = databaseAdapter.insertWOD(rootObject.getString("Word"), rootObject.getString("ID"), dateTime);

                JSONArray description = rootObject.getJSONArray("Description");
                for (int i = 0; i < description.length(); i++) {
                    JSONObject object = description.getJSONObject(i);

                    databaseAdapter.insertWODDetail(object.getString("Meaning"),
                            object.getString("Description"), object.getString("Translation"), id);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONEXception", e.toString());
            }
            createNotification(context);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
