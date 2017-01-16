package com.dw.andro.dictionary.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dw.andro.dictionary.Fragment.About;
import com.dw.andro.dictionary.Fragment.SettingsFragment;
import com.dw.andro.dictionary.MyApp;
import com.dw.andro.dictionary.NotificationReceiver.AlertReceiver;
import com.dw.andro.dictionary.BackgroundItemDecoration;
import com.dw.andro.dictionary.Fragment.Favorite;
import com.dw.andro.dictionary.Fragment.RecentSearch;
import com.dw.andro.dictionary.Fragment.SpellingCheck;
import com.dw.andro.dictionary.Fragment.WordODay;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.Adapter.SearchResultAdapter;
import com.dw.andro.dictionary.ServiceData;
import com.dw.andro.dictionary.DataObject.WordObject;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    SharedPreferences prefName;
    String userName;
    RecyclerView rvResultList;
    ArrayList<WordObject> wordList;
    LinearLayout homePage;
    NavigationView navigationView;
    SearchView searchView;
    MenuItem searchItem;
    MenuItem voiceSearchItem;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public Tracker t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        prefName = getApplicationContext().getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        userName = prefName.getString("name", "User");
        wordList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        ((TextView) nav_header.findViewById(R.id.nav_header_title)).setText("Hi " + userName);
        navigationView.addHeaderView(nav_header);
        navigationView.setNavigationItemSelectedListener(this);

        homePage = (LinearLayout) findViewById(R.id.home_page_content);
        rvResultList = (RecyclerView) findViewById(R.id.rv_result_list);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rvResultList.setLayoutManager(llm);
        rvResultList.addItemDecoration(new BackgroundItemDecoration(R.drawable.rv_even_item_bg, R.drawable.rv_odd_item_bg, mContext));


        setNotification();
        onNavigationItemSelected(navigationView.getMenu().getItem(0));


        t = ((MyApp) getApplication()).getTracker(MyApp.TrackerName.SEARCH_LIST_ACTIVITY_SCREEN);
        t.setScreenName("Search List Screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void setNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();//initializes to now
        Calendar cal_alertTime = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(dat);
        cal_alertTime.setTime(dat);
        cal_alertTime.set(Calendar.HOUR_OF_DAY, 13);//set the alarm time
        cal_alertTime.set(Calendar.MINUTE, 52);
        cal_alertTime.set(Calendar.SECOND, 00);
        if (cal_alertTime.before(cal_now)) {//if its in the past increment
            cal_alertTime.add(Calendar.DATE, 1);
        }
        Intent alertIntent = new Intent(this, AlertReceiver.class);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alertTime.getTimeInMillis(), PendingIntent.getBroadcast(this, 0, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.action_search);
        voiceSearchItem = menu.findItem(R.id.action_search_voice);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("search dictionary");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("newText", newText);
                CharSequence query = newText;
                if (query != null && TextUtils.getTrimmedLength(query) > 0) {
                    new AsyncTaskRunner().execute(String.valueOf(query));
                }
                return false;
            }
        });

        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                rvResultList.setVisibility(View.GONE);
                homePage.setVisibility(View.VISIBLE);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                rvResultList.setVisibility(View.VISIBLE);
                homePage.setVisibility(View.GONE);
                return true;  // Return true to expand action view
            }
        };
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        MenuItemCompat.setOnActionExpandListener(voiceSearchItem, expandListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_voice) {
            searchView.onActionViewExpanded();
            promptSpeechInput();
            rvResultList.setVisibility(View.VISIBLE);
            homePage.setVisibility(View.GONE);
            findViewById(R.id.action_search).performClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_wod) {
            fragment = new WordODay();
            setTitle(getResources().getString(R.string.app_name));
        } else if (id == R.id.nav_favorite) {
            fragment = new Favorite();
            setTitle("Favorite");
        } else if (id == R.id.nav_recent_search) {
            fragment = new RecentSearch();
            setTitle("Recent Search");
        } else if (id == R.id.nav_spelling_check) {
            fragment = new SpellingCheck();
            setTitle("Spelling Check");
        } else if (id == R.id.nav_about) {
            fragment = new About();
            setTitle("About");
        } else if (id == R.id.nav_user_settings) {
            fragment = new SettingsFragment();
            setTitle("Settings");
        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack("STACK");
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {

            return ServiceData.getWordList(param[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            WordResponse(result);


        }
    }

    private void WordResponse(String result) {
        if (result != null) {
            wordList.clear();
            try {
                JSONArray rootArray = new JSONArray(result);
                for (int i = 0; i < rootArray.length(); i++) {
                    WordObject setData = new WordObject();
                    JSONObject object = rootArray.getJSONObject(i);
                    setData.setWord(object.getString("Word"));
                    setData.setWordId(object.getString("ID"));

                    wordList.add(setData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONEXception", e.toString());
            }
        }
        rvResultList.setAdapter(new SearchResultAdapter(mContext, wordList));
        rvResultList.invalidate();
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchView.setQuery(result.get(0), true);

                    Toast.makeText(mContext, result.get(0), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }
}
