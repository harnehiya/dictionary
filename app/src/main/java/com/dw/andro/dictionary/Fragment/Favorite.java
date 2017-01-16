package com.dw.andro.dictionary.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dw.andro.dictionary.Adapter.FavoriteListAdapter;
import com.dw.andro.dictionary.BackgroundItemDecoration;
import com.dw.andro.dictionary.MyApp;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.DataObject.WordObject;
import com.dw.andro.dictionary.database.DatabaseAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
    public class Favorite extends Fragment {

    Context mContext;
    RecyclerView rvFavoriteList;
    private FavoriteListAdapter adapter;
    DatabaseAdapter databaseAdapter;
    ArrayList<WordObject> favoriteList;
    public Tracker t;
    private static int REQUEST_CODE = 100;

    public Favorite() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        mContext = getActivity();
        databaseAdapter = new DatabaseAdapter(mContext);
        favoriteList = databaseAdapter.getFavoriteList();

        rvFavoriteList = (RecyclerView) v.findViewById(R.id.rv_favorite_list);
//        postObjectGetSearched();
        getFavorite();

        t = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.FAVORITE_FRAGMENT_SCREEN);
        t.setScreenName("Favorite Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        return v;
    }

    private void getFavorite() {
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rvFavoriteList.setLayoutManager(llm);
        rvFavoriteList.addItemDecoration(new BackgroundItemDecoration(R.drawable.rv_even_item_bg, R.drawable.rv_odd_item_bg, mContext));
        adapter = new FavoriteListAdapter(this,mContext, favoriteList);
        rvFavoriteList.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("main activity", requestCode + " = " + resultCode);

        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == 110)
            {
                if(favoriteList!=null)
                    favoriteList.clear();
                favoriteList=databaseAdapter.getFavoriteList();
                adapter = new FavoriteListAdapter(this,mContext, favoriteList);
//                adapter.notifyDataSetChanged();
                rvFavoriteList.setAdapter(adapter);
            }
        }
    }
            }

