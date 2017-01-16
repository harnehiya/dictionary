package com.dw.andro.dictionary.Fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dw.andro.dictionary.MyApp;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.ServiceData;
import com.dw.andro.dictionary.DataObject.WordDetailObject;
import com.dw.andro.dictionary.DataObject.WordObject;
import com.dw.andro.dictionary.database.DatabaseAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordODay extends Fragment {

    Context mContext;
    TextView tvWOD;
    LinearLayout wodDescription;
    WordObject wordObject;
    ToggleButton favorite;
    public Tracker t;

    ArrayList<WordDetailObject> wordDetail;
    DatabaseAdapter databaseAdapter;
    String dateTime;

    public WordODay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_word_oday, container, false);
        mContext = getActivity();
        tvWOD = (TextView) v.findViewById(R.id.tv_word);
        favorite = (ToggleButton) v.findViewById(R.id.tgl_favorite);
//        favorite.setChecked(false);

        databaseAdapter = new DatabaseAdapter(mContext);

        wordObject = new WordObject();
        wordDetail = new ArrayList<>();

        wordObject = databaseAdapter.getWOD();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        dateTime = df.format(c.getTime()).toString();


        wodDescription = (LinearLayout) v.findViewById(R.id.description);

        if (wordObject != null && dateTime.equals(wordObject.getDateTime())) {
            wordDetail = databaseAdapter.getWODDetail(wordObject.getWODId());
            showDetail(wordDetail);

        } else {
            if (isNetworkAvailable(mContext)) {
                new AsyncTaskWOD().execute();

            } else
            if (wordObject != null){
                wordDetail = databaseAdapter.getWODDetail(wordObject.getWODId());
                showDetail(wordDetail);
            }else
                Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }


        favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (favorite.isChecked()&&!databaseAdapter.checkFavoriteWord(wordObject.getWordId())) {

                    databaseAdapter.insertFavoriteWord(wordObject.getWordId(), wordObject.getWord());
                    for (int i = 0; i < wordDetail.size(); i++) {
                        databaseAdapter.insertFavoriteDetail(wordDetail.get(i).getMeaning(), wordDetail.get(i).getDescription(),
                                wordDetail.get(i).getTranslation(), Long.parseLong(wordObject.getWordId()));
                    }

                } else {
                    databaseAdapter.removeFavoriteDetail(wordObject.getWordId());
                    databaseAdapter.removeFavoriteWord(wordObject.getWordId());
                }
            }
        });

        t = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.WOD_FRAGMENT_SCREEN);
        t.setScreenName("Word of The Day Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        return v;
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
            responseWOD(result);
        }
    }

    private void responseWOD(String result) {

        if (result != null) {
            try {

                JSONObject rootObject = new JSONObject(result);
                wordObject = new WordObject();

                wordObject.setWordId(rootObject.getString("ID"));
                wordObject.setWord(rootObject.getString("Word"));
                Long id = databaseAdapter.insertWOD(rootObject.getString("Word"), rootObject.getString("ID"), dateTime);

                JSONArray description = rootObject.getJSONArray("Description");
                for (int i = 0; i < description.length(); i++) {
                    JSONObject object = description.getJSONObject(i);
                    WordDetailObject detailInstance = new WordDetailObject();

                    detailInstance.setMeaning(object.getString("Meaning"));
                    detailInstance.setDescription(object.getString("Description"));
                    detailInstance.setTranslation(object.getString("Translation"));

                    databaseAdapter.insertWODDetail(object.getString("Meaning"),
                            object.getString("Description"), object.getString("Translation"), id);
                    wordDetail.add(detailInstance);
                }
                showDetail(wordDetail);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONEXception", e.toString());
            }
        }
    }

    private void showDetail(ArrayList<WordDetailObject> detail) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        tvWOD.setText(wordObject.getWord());
        for (int i = 0; i < detail.size(); i++) {

            View view = inflater.inflate(R.layout.list_word_description, wodDescription, false);
            wodDescription.addView(view);
            TextView tvMeaning = (TextView) view.findViewById(R.id.meaning);
            TextView tvDescription = (TextView) view.findViewById(R.id.description);
            TextView tvTranslation = (TextView) view.findViewById(R.id.translation);
            tvMeaning.setText(detail.get(i).getMeaning());
            tvDescription.setText(detail.get(i).getDescription());
            tvTranslation.setText(detail.get(i).getTranslation());
        }

        if (databaseAdapter.checkFavoriteWord(wordObject.getWordId())) {
            favorite.setChecked(true);
        } else {
            favorite.setChecked(false);
        }


    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
