package com.dw.andro.dictionary.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dw.andro.dictionary.R;
import com.dw.andro.dictionary.ServiceData;
import com.dw.andro.dictionary.SetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvayweb on 04/03/16.
 */
public class SpellingCheckAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    ArrayList<String> resultList = new ArrayList<>();
    List<String> suggestedWords = new ArrayList<>();
    ArrayList<SetData> suggestList;

    public SpellingCheckAdapter(Context context, ArrayList<SetData> suggestList) {
        mContext = context;
        this.suggestList = suggestList;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_search_result, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.result_word)).setText(resultList.get(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    new AsyncTaskCheckWord().execute(String.valueOf(constraint));

                    // Assign the data to the FilterResults
                    filterResults.values = suggestedWords;
                    filterResults.count = suggestedWords.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    public class AsyncTaskCheckWord extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {

            return ServiceData.getSpellingCheck(param[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            WordResponse(result);
        }
    }

    private void WordResponse(String result) {
        if (result != null) {
            suggestedWords.clear();
            suggestList.clear();
            try {
                JSONArray rootArray = new JSONArray(result);
                for (int i = 0; i < rootArray.length(); i++) {
                    SetData setData = new SetData();
                    JSONObject object = rootArray.getJSONObject(i);
                    suggestedWords.add(object.getString("Word"));
                    setData.setResult_Word(object.getString("Word"));
                    setData.setWord_id(object.getString("ID"));
                    suggestList.add(setData);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONEXception", e.toString());
            }
            notifyDataSetChanged();
        }

    }


}
