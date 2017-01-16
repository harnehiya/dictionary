package com.dw.andro.dictionary.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dw.andro.dictionary.MyApp;
import com.dw.andro.dictionary.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment {
    Context mContext;
    TextView link1, link2, link3, link4, tvReviewRating;
    public Tracker t;

    public About() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        mContext = getActivity().getApplicationContext();
        tvReviewRating = (TextView) v.findViewById(R.id.tv_review_rating);
        link1 = (TextView) v.findViewById(R.id.link1);
        link2 = (TextView) v.findViewById(R.id.link2);
        link3 = (TextView) v.findViewById(R.id.link3);
        link4 = (TextView) v.findViewById(R.id.link4);

        tvReviewRating.setText(
                Html.fromHtml("<a href=\"https://play.google.com/store/apps/details?id=com.dw.andro.valentinesdayapp\"><b>Submit a review in google play store </b> </a>"));
        tvReviewRating.setMovementMethod(LinkMovementMethod.getInstance());

        link1.setText(Html.fromHtml("<a href=\"http://dvayweb.com\"><b>Website</b></a> "));
        link1.setMovementMethod(LinkMovementMethod.getInstance());
        link1 = (TextView) v.findViewById(R.id.link1);

        link2.setText(Html.fromHtml("<a href=\"http://dvayweb.com/mobile/vmessagebox/privacy-policy.html\"><b>Privacy Policy</b></a> "));
        link2.setMovementMethod(LinkMovementMethod.getInstance());
        link1 = (TextView) v.findViewById(R.id.link1);

        link3.setText(Html.fromHtml("<a href=\"http://twitter.com/DvayWeb\"><b>Twitter</b></a> "));
        link3.setMovementMethod(LinkMovementMethod.getInstance());
        link1 = (TextView) v.findViewById(R.id.link1);

        link4.setText(Html.fromHtml("<a href=\"http://facebook.com/dvayweb\"><b>Facebook</b></a> "));
        link4.setMovementMethod(LinkMovementMethod.getInstance());

        t = ((MyApp) getActivity().getApplication()).getTracker(MyApp.TrackerName.ABOUT_FRAGMENT_SCREEN);
        t.setScreenName("About Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());

//        AdView mAdView = (AdView) v.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("0123456789ABCDEF")
//                .build();
//        mAdView.loadAd(adRequest);

        return v;
    }

}
