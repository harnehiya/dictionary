package com.dw.andro.dictionary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dw.andro.dictionary.DataObject.LangListObject;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.ServiceData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Context mContext;
    SharedPreferences userDetailPref;
    SharedPreferences.Editor ed;
    Spinner spinnerLevel, spinnerLanguage;
    String levelSelected = null;
    EditText etName, etMail;
    ProgressDialog progressDialog;
    ArrayList<LangListObject> langList;
    ArrayList<String> lang;
    String langId = null;
    SharedPreferences userSettingPref;
    SharedPreferences.Editor edSetting;
    String itemSelection,langSelection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mContext = this;
        userSettingPref = mContext.getSharedPreferences("SettingsPref", Context.MODE_PRIVATE);
        etName = (EditText) findViewById(R.id.et_name);
        etMail = (EditText) findViewById(R.id.et_mail);

        userDetailPref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (userDetailPref.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        langList = new ArrayList<>();
        lang = new ArrayList<>();
//        lang.add("Select Language");
        new AsyncTaskLangList().execute();

        spinnerLevel = (Spinner) findViewById(R.id.spinner_level);


        ArrayAdapter<CharSequence> adapterLevel = ArrayAdapter.createFromResource(this,
                R.array.level_array, android.R.layout.simple_spinner_item);


        spinnerLevel.setAdapter(adapterLevel);

        spinnerLevel.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.spinner_level:
                itemSelection = adapterView.getItemAtPosition(i).toString();
                onLevelSelection(itemSelection);
                break;

            case R.id.spinner_language:
                langId = langList.get(i).getId();
                langSelection = langList.get(i).getLanguage();

                break;

            default:
                break;
        }

    }

    private void onLevelSelection(String itemSelection) {
        switch (itemSelection) {
            case "Beginner":
                levelSelected = "B";
                break;

            case "Intermediate":
                levelSelected = "I";
                break;

            case "Advance":
                levelSelected = "A";
                break;

            case "Select level":
                levelSelected = "null";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.e("Spinner", "On Nothing Selected Called");
    }

    public void onContinue(View view) {
        if (isNetworkAvailable(mContext)) {

            if (!etName.getText().toString().matches("") && !etMail.getText().toString().matches("")) {
                if (levelSelected != null) {
//                    if (langId != null) {
                        userDetailPref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                        ed = userDetailPref.edit();
                        ed.putString("name", etName.getText().toString());
                        ed.putBoolean("activity_executed", true);
                        ed.commit();
                        edSetting = userSettingPref.edit();
                        edSetting.putString("level", itemSelection);
                        edSetting.putString("lang", langSelection);
                        edSetting.commit();

//                        postUserData();
//                    } else {
//                        Toast.makeText(this, "Please select language first", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Toast.makeText(this, "Please select level first", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill the fields first", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(mContext, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void postUserData() {
        JSONObject userDetail;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String IEMI = telephonyManager.getDeviceId();

        userDetail = new JSONObject();
        try {
            userDetail.put("Name", etName.getText().toString());
            userDetail.put("EmailMobile", etMail.getText().toString());
            userDetail.put("Level", levelSelected);
            userDetail.put("LanguageId", langId);
            Log.e("LangId", langId);
            userDetail.put("IEMICode", IEMI);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PostUserIdAsyncTask().execute(userDetail);
    }

    public class PostUserIdAsyncTask extends AsyncTask<JSONObject, Void, String> {

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
            return ServiceData.PostUserIdService(param[0]);
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
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Submit userDetail", e.toString());
            }
        } else {
            Toast.makeText(mContext, "Server Error!! Please try again", Toast.LENGTH_SHORT).show();
        }
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
            responseLangList(result);
        }
    }

    private void responseLangList(String result) {

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
                    spinnerLanguage = (Spinner) findViewById(R.id.spinner_language);
                    spinnerLanguage.setPrompt("Select Language");
                    ArrayAdapter<String> adapterLanguage = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, lang);
                    adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLanguage.setAdapter(adapterLanguage);
                    spinnerLanguage.setOnItemSelectedListener(this);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONEXception", e.toString());
            }
        }
    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}