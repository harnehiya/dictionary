package com.dw.andro.dictionary.Fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dw.andro.dictionary.BackgroundItemDecoration;
import com.dw.andro.dictionary.MyApp;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.Adapter.SearchResultAdapter;
import com.dw.andro.dictionary.ServiceData;
import com.dw.andro.dictionary.DataObject.WordObject;
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
public class RecentSearch extends Fragment {

    Context mContext;
    RecyclerView rvHistoryList;
    private SearchResultAdapter adapter;
    ArrayList<WordObject> searchList;
    DatabaseAdapter databaseAdapter;
    public Tracker t;

    public RecentSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recent_search, container, false);
        mContext = getActivity();
        databaseAdapter = new DatabaseAdapter(mContext);
        searchList = new ArrayList<>();

        searchList = databaseAdapter.getSearchList();
        rvHistoryList = (RecyclerView) v.findViewById(R.id.rv_history_list);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rvHistoryList.setLayoutManager(llm);

        if (searchList != null) {
            rvHistoryList.addItemDecoration(new BackgroundItemDecoration(R.drawable.rv_even_item_bg, R.drawable.rv_odd_item_bg, mContext));
            adapter = new SearchResultAdapter(mContext, searchList);
            rvHistoryList.setAdapter(adapter);
        } else {
            Toast.makeText(mContext, "Your search list is empty", Toast.LENGTH_SHORT).show();
        }
//        postObjectGetSearched();

        t = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.RECENT_SEARCHES_FRAGMENT_SCREEN);
        t.setScreenName("Recent Searches Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        return v;
    }

//    private void postObjectGetSearched() {
//        JSONObject obj = new JSONObject();
//        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        String IEMICode = telephonyManager.getDeviceId();
//        try {
//            obj.put("IEMICode", IEMICode);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new AsyncTaskHistory().execute(obj);
//    }
//
//    public class AsyncTaskHistory extends AsyncTask<JSONObject, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
//
//        }
//
//
//        @Override
//        protected String doInBackground(JSONObject... param) {
//
//            return ServiceData.getHistoryService(param[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // execution of result of Long time consuming operation
//            getHistory(result);
//
//
//        }
//    }
//
//    private void getHistory(String result) {
//
//        if (result != null) {
//            try {
//
//                JSONArray rootArray = new JSONArray(result);
//
//                ArrayList<WordObject> historyList = new ArrayList<>();
//
//                for (int i = 0; i < rootArray.length(); i++) {
//                    WordObject setData = new WordObject();
//                    JSONObject object = rootArray.getJSONObject(i);
//                    setData.setWord(object.getString("Word"));
//                    setData.setWordId(object.getString("ID"));
//                    historyList.add(setData);
//                }
//
//                LinearLayoutManager llm = new LinearLayoutManager(mContext);
//                rvHistoryList.setLayoutManager(llm);
//                rvHistoryList.addItemDecoration(new BackgroundItemDecoration(R.drawable.rv_even_item_bg, R.drawable.rv_odd_item_bg, mContext));
//                adapter = new SearchResultAdapter(mContext, historyList);
//                rvHistoryList.setAdapter(adapter);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.e("JSONEXception", e.toString());
//            }
//        }
//    }
}
