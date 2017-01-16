package com.dw.andro.dictionary.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dw.andro.dictionary.MyApp;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.ServiceData;
import com.dw.andro.dictionary.DataObject.WordDetailObject;
import com.dw.andro.dictionary.DataObject.WordObject;
import com.dw.andro.dictionary.database.DatabaseAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WordDetailActivity extends AppCompatActivity {

    Context mContext;
    TextView tvWord;
    LinearLayout view_description;
    ToggleButton favorite;
    String id;
    ArrayList<WordDetailObject> wordDetail;
    DatabaseAdapter databaseAdapter;
    WordObject wordObject;
    boolean getDetailFlag;
    public Tracker t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        mContext = this;
        wordObject = new WordObject();
        databaseAdapter = new DatabaseAdapter(mContext);
        favorite = (ToggleButton) findViewById(R.id.tgl_favorite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Definitions");
        tvWord = (TextView) findViewById(R.id.tv_word);
        view_description = (LinearLayout) findViewById(R.id.description);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String word = intent.getStringExtra("word");
        getDetailFlag = intent.getBooleanExtra("flag", true);
        Log.e("getDetailFlag", String.valueOf(getDetailFlag));
        if (getDetailFlag == true) {
            if (databaseAdapter.checkFavoriteWord(id)) {
                favorite.setChecked(true);
            } else {
                favorite.setChecked(false);
            }

            if (!databaseAdapter.checkSearchWord(id)) {
                databaseAdapter.insertSearchWord(id, word);
            }
            new AsyncTaskWordDetail().execute(String.valueOf(id));
        } else {
            favorite.setChecked(true);
            wordObject.setWord(word);
            wordDetail = databaseAdapter.getFavoriteDetail(id);
            showDetail(wordDetail);
        }
        favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (favorite.isChecked()&&!databaseAdapter.checkFavoriteWord(wordObject.getWordId())) {
                    databaseAdapter.insertFavoriteWord(wordObject.getWordId(), wordObject.getWord());
                    for (int i = 0; i < wordDetail.size(); i++) {
                        databaseAdapter.insertFavoriteDetail(wordDetail.get(i).getMeaning(), wordDetail.get(i).getDescription(),
                                wordDetail.get(i).getTranslation(), Long.parseLong(id));
                    }

                } else {
                    databaseAdapter.removeFavoriteDetail(id);
                    databaseAdapter.removeFavoriteWord(id);
                }
                setResult(110);
            }
        });

        t = ((MyApp) getApplication()).getTracker(MyApp.TrackerName.WORD_TRANSLATION_ACTIVITY_SCREEN);
        t.setScreenName("Word Detail Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());



    }


    public class AsyncTaskWordDetail extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {

            return ServiceData.getWordDetail(param[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            wordDetail(result);


        }
    }

    private void wordDetail(String result) {

        if (result != null) {
            try {

                JSONObject rootObject = new JSONObject(result);

                wordObject.setWordId(rootObject.getString("ID"));
                wordObject.setWord(rootObject.getString("Word"));

                JSONArray description = rootObject.getJSONArray("Description");
                wordDetail = new ArrayList<>();
                for (int i = 0; i < description.length(); i++) {
                    JSONObject object = description.getJSONObject(i);
                    WordDetailObject detailInstance = new WordDetailObject();

                    detailInstance.setMeaning(object.getString("Meaning"));
                    detailInstance.setDescription(object.getString("Description"));
                    detailInstance.setTranslation(object.getString("Translation"));

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
        LayoutInflater inflater = LayoutInflater.from(this);
        tvWord.setText(wordObject.getWord());

        for (int i = 0; i < detail.size(); i++) {
            View view = inflater.inflate(R.layout.list_word_description, view_description, false);
            view_description.addView(view);

            TextView tvMeaning = (TextView) view.findViewById(R.id.meaning);
            TextView tvDescription = (TextView) view.findViewById(R.id.description);
            TextView tvTranslation = (TextView) view.findViewById(R.id.translation);
            tvMeaning.setText(detail.get(i).getMeaning());
            tvDescription.setText(detail.get(i).getDescription());
            tvTranslation.setText(detail.get(i).getTranslation());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.word_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_share:
                onShare();
                return true;

            case android.R.id.home:

//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onShare() {

        String msg = tvWord.getText().toString() + "\n";

        StringBuilder builder = new StringBuilder(msg);
        for (int i = 0; i < wordDetail.size(); i++) {
            builder.append(wordDetail.get(i).getMeaning()+"  "+wordDetail.get(i).getDescription()+"  "+wordDetail.get(i).getTranslation()+"\n");
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        sendIntent.setType("text/plain");
        String title = getResources().getString(R.string.chooser_title_msg);
        Intent chooser = Intent.createChooser(sendIntent, title);

        // Verify the intent will resolve to at least one activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }

    }

}
