package com.dw.andro.dictionary;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by dvayweb on 13/04/16.
 */
public class MyApp extends Application {

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {

        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.app_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    public enum TrackerName {
        SEARCH_LIST_ACTIVITY_SCREEN,
        WORD_TRANSLATION_ACTIVITY_SCREEN,
        WOD_FRAGMENT_SCREEN,
        FAVORITE_FRAGMENT_SCREEN,
        RECENT_SEARCHES_FRAGMENT_SCREEN,
        SPELLINGCHECK_FRAGMENT_SCREEN,
        SETTINGS_FRAGMENT_SCREEN,
        ABOUT_FRAGMENT_SCREEN;
    }
}
