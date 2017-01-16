package com.dw.andro.dictionary.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dw.andro.dictionary.Activity.WordDetailActivity;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.DataObject.WordObject;

import java.util.ArrayList;

/**
 * Created by dvayweb on 02/04/16.
 */
public class FavoriteListAdapter extends RecyclerView
        .Adapter<FavoriteListAdapter
        .DataObjectHolder> {

    private ArrayList<WordObject> result;
    Context mContext;
    int mPosition;
    private static int REQUEST_CODE = 100;
    Fragment fragment;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvResult;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvResult = (TextView) itemView.findViewById(R.id.result_word);

        }

    }

    public FavoriteListAdapter(Fragment favorite, Context context, ArrayList<WordObject> myDataset) {
        mContext = context;
        result = myDataset;
        fragment=favorite;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_result, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        mPosition = position;

        holder.tvResult.setText(result.get(position).getWord());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = result.get(position).getWordId();
                String word = result.get(position).getWord();

                Intent intent = new Intent(mContext, WordDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("word", word);
                intent.putExtra("flag", false);
//                mContext.startActivity(intent);
                fragment.startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}
