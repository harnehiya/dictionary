package com.dw.andro.dictionary.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dw.andro.dictionary.Activity.WordDetailActivity;
import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.DataObject.WordObject;

import java.util.ArrayList;

/**
 * Created by dvayweb on 20/02/16.
 */
public class SearchResultAdapter extends RecyclerView
        .Adapter<SearchResultAdapter
        .DataObjectHolder> {

    private ArrayList<WordObject> result;
    Context mContext;
    int mPosition;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvResult;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvResult = (TextView) itemView.findViewById(R.id.result_word);

        }

    }

    public SearchResultAdapter(Context context,ArrayList<WordObject> myDataset) {
        mContext = context;
        result = myDataset;
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
                String id=result.get(position).getWordId();
                String word=result.get(position).getWord();

//                postObjectAddSearched(id);
                if (isNetworkAvailable(mContext)) {
                    Intent intent = new Intent(mContext, WordDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("flag", true);
                    intent.putExtra("word", word);
                    mContext.startActivity(intent);
                } else
                    Toast.makeText(mContext,"Please check your internet connection",Toast.LENGTH_SHORT).show();
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

//    private void postObjectAddSearched(String id){
//        JSONObject obj=new JSONObject();
//        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        IEMICode = telephonyManager.getDeviceId();
//        try {
//            obj.put("WordId",id);
//            obj.put("IEMICode",IEMICode);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new AsyncTaskAddSearched().execute(obj);
//    }
//
//    public class AsyncTaskAddSearched extends AsyncTask<JSONObject, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... param) {
//
//            return ServiceData.postHistoryService(param[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // execution of result of Long time consuming operation
//            addHistoryConfirm(result);
//
//
//        }
//    }
//
//    private void addHistoryConfirm(String result) {
//        if(result != null) {
////            setResult(100);
//        }
//    }


}
