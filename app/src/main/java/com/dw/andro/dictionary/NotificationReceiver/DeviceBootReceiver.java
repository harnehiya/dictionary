package com.dw.andro.dictionary.NotificationReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dvayweb on 26/02/16.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        setBootNotification();
    }

    private void setBootNotification() {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();//initializes to now
        Calendar cal_alertTime = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(dat);
        cal_alertTime.setTime(dat);
        cal_alertTime.set(Calendar.HOUR_OF_DAY, 17);//set the alarm time
        cal_alertTime.set(Calendar.MINUTE, 40);
        cal_alertTime.set(Calendar.SECOND, 00);
        if (cal_alertTime.before(cal_now)) {//if its in the past increment
            cal_alertTime.add(Calendar.DATE, 1);
        }
        Intent alertIntent = new Intent(context, AlertReceiver.class);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alertTime.getTimeInMillis(), PendingIntent.getBroadcast(context, 0, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
