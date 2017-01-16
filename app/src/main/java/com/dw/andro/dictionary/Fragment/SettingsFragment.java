package com.dw.andro.dictionary.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dw.andro.dictionary.DataObject.LangListObject;
import com.dw.andro.dictionary.MyApp;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.ServiceData;
import com.dw.andro.dictionary.database.DatabaseAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    String IEMI;
    TableRow rowLanguage, rowLevel, rowSearch;
    ArrayList<LangListObject> langList;
    ArrayList<String> lang;
    String langName, langId, levelId;
    TextView sLanguage, sLevel;
    Context mContext;
    DatabaseAdapter databaseAdapter;
    public Tracker t;
    SharedPreferences userSettingPref;
    SharedPreferences.Editor ed;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        mContext = getActivity();

        userSettingPref = mContext.getSharedPreferences("SettingsPref", Context.MODE_PRIVATE);
        databaseAdapter=new DatabaseAdapter(mContext);
        sLanguage= (TextView) v.findViewById(R.id.tv_language_selected);
        sLevel= (TextView) v.findViewById(R.id.tv_level_selected);
        rowLanguage = (TableRow) v.findViewById(R.id.language_row);
        rowLevel = (TableRow) v.findViewById(R.id.level_row);
        rowSearch = (TableRow) v.findViewById(R.id.delete_search_row);

        sLanguage.setText(userSettingPref.getString("lang", "not found"));
        sLevel.setText(userSettingPref.getString("level", "not found"));
//        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        IEMI = telephonyManager.getDeviceId();
        new AsyncTaskGetUserPreferences().execute(IEMI);

        rowLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(mContext)) {
                    if (langId != null) {
                        CharSequence[] items = lang.toArray(new CharSequence[lang.size()]);
                        int selected = Integer.parseInt(langId) - 1;
                        getUpdateDialog(items, "lang", selected);
                    }

                } else
                    Toast.makeText(mContext,"No internet",Toast.LENGTH_SHORT).show();
            }
        });
        rowLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(mContext)) {
                    int selectedIndex = 0;
                    CharSequence[] items = {"Beginner", "Intermediate", "Advance"};
                    if (levelId != null) {
                        selectedIndex = getSelectedLevelPos(levelId);
                    }

                    getUpdateDialog(items, "level", selectedIndex);
                } else
                    Toast.makeText(mContext,"No internet",Toast.LENGTH_SHORT).show();
            }
        });

        rowSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Searches")
                        .setMessage("Are you sure you want to delete searches history?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                databaseAdapter.deleteSearchHistory();
                                Toast.makeText(getActivity(), "Search history deleted successfully", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        t = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.SETTINGS_FRAGMENT_SCREEN);
        t.setScreenName("Settings Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        return v;
    }


    private void getUpdateDialog(final CharSequence[] items, final String flag, int selected) {
//        ll.setBackgroundColor(Color.RED);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Update preferences");
        builder.setSingleChoiceItems(items, selected,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ed = userSettingPref.edit();
                        if (flag.equals("level")) {
                            getLevelId((String) items[item]);
                            ed.putString("level", String.valueOf(items[item]));
                            ed.commit();
                            sLevel.setText(userSettingPref.getString("level", "not found"));
                        } else if (flag.equals("lang")) {
                            langId = langList.get(item).getId();
                            ed.putString("lang", langList.get(item).getLanguage());
                            ed.commit();
                            sLanguage.setText(userSettingPref.getString("lang", "not found"));
                        }
                        updateUserData();
                        dialog.cancel();
                        new AsyncTaskGetUserPreferences().execute(IEMI);
//                        ll.setBackgroundColor(Color.GRAY);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public class AsyncTaskGetUserPreferences extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {

            return ServiceData.getUserPreferences(param[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            getSavedPreferences(result);
        }
    }

    private void getSavedPreferences(String result) {
        if (result != null) {
            langList = new ArrayList<>();
            lang = new ArrayList<>();
            try {

                JSONObject object = new JSONObject(result);
                langName = object.getString("Language");
                langId = object.getString("LanguageId");
                levelId = object.getString("Level");

//                sLanguage.setText(langName);
                if(levelId!=null){
//                    sLevel.setText(getLevelName(levelId));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONEXception", e.toString());
            }
        }
        new AsyncTaskLangList().execute();
    }


    public class AsyncTaskLangList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {

            return ServiceData.getLanguageList();
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            getLangList(result);
        }
    }

    private void getLangList(String result) {

        if (result != null) {
            try {

                JSONArray rootArray = new JSONArray(result);

                for (int i = 0; i < rootArray.length(); i++) {
                    JSONObject object = rootArray.getJSONObject(i);

                    LangListObject language = new LangListObject();
                    language.setId(object.getString("ID"));
                    language.setLanguage(object.getString("Title"));

                    lang.add(object.getString("Title"));
                    langList.add(language);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONEXception", e.toString());
            }
        }
    }


    private void getLevelId(String itemSelection) {
        switch (itemSelection) {
            case "Beginner":
                levelId = "B";
                break;

            case "Intermediate":
                levelId = "I";
                break;

            case "Advance":
                levelId = "A";
                break;
        }
    }

    private String getLevelName(String level) {
        String levelName = null;
        switch (level) {
            case "I":
                levelName = "Intermediate";
                break;
            case "B":
                levelName = "Beginner";
                break;
            case "A":
                levelName = "Advance";
                break;
        }
        return levelName;
    }

    private int getSelectedLevelPos(String levelId) {
        int selectedIndex = 0;
        switch (levelId) {
            case "B":
                selectedIndex = 0;
                break;
            case "I":
                selectedIndex = 1;
                break;
            case "A":
                selectedIndex = 2;
                break;
        }
        return selectedIndex;
    }


    public void updateUserData() {
        JSONObject updateDetail;
//        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        String IEMI = telephonyManager.getDeviceId();

        updateDetail = new JSONObject();
        try {
            updateDetail.put("Level", levelId);
            updateDetail.put("LanguageId", langId);
            updateDetail.put("IEMICode", IEMI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new updateUserPreferences().execute(updateDetail);
    }

    public class updateUserPreferences extends AsyncTask<JSONObject, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(JSONObject... param) {
            return ServiceData.updateUserPreferencesService(param[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            getConfirmation(result);
            progressDialog.dismiss();

        }
    }

    private void getConfirmation(String result) {
        if (result != null) {
            try {
                JSONObject root = new JSONObject(result);
                String response = root.getString("ResponseCode");

                if (response.equals("100")) {
                    Toast.makeText(mContext, "Your preferences updated successfully", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("update preferences", e.toString());
            }
        } else {
            Toast.makeText(mContext, "Server Error!! Please try again", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
