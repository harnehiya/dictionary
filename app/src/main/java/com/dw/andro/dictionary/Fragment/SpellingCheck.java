package com.dw.andro.dictionary.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.dw.andro.dictionary.Activity.WordDetailActivity;
import com.dw.andro.dictionary.MyApp;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.SetData;
import com.dw.andro.dictionary.Adapter.SpellingCheckAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpellingCheck extends Fragment {

    Context mContext;
    ArrayList<SetData> wordList;
    AutoCompleteTextView autoWord;
    ListView spellingList;
    int threshold=1;
    ArrayList<SetData> suggestList;
    SpellingCheckAdapter adapter;
    public Tracker t;

    public SpellingCheck() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_spelling_check, container, false);
        mContext=getActivity();
        wordList=new ArrayList<>();

        suggestList=new ArrayList<>();
        spellingList= (ListView) v.findViewById(R.id.spelling_list);

        autoWord= (AutoCompleteTextView) v.findViewById(R.id.word);
        adapter=new SpellingCheckAdapter(mContext,suggestList);

        autoWord.setThreshold(threshold);

        spellingList.setAdapter(adapter);
        autoWord.setAdapter(adapter);
        autoWord.setDropDownHeight(0);
        autoWord.setDropDownWidth(0);

        spellingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String word_id = suggestList.get(position).getWord_id();
//                postObjectAddSearched(id);

                Intent intent = new Intent(mContext, WordDetailActivity.class);
                intent.putExtra("id", word_id);
                mContext.startActivity(intent);
            }
        });

        t = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.SPELLINGCHECK_FRAGMENT_SCREEN);
        t.setScreenName("Spelling Check Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        return v;

    }
}
