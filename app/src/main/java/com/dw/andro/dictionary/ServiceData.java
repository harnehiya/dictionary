package com.dw.andro.dictionary;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dvayweb on 19/02/16.
 */
public class ServiceData {

    private static String SERVICE_URL = "http://192.168.1.5:8080/api/";
    private static String sResponse = null;
    private static String METHOD_NAME;

    public static String getLanguageList() {

        METHOD_NAME = "Dictionary/LanguageList/";

// HTTP Get
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setRequestMethod("GET"); // http verb
            urlConnection.setRequestProperty("Accept", "text/json");

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();

            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {

            Log.e("ServiceData Exception", e.toString());
        }

        return sResponse;
    }


    public static String getWordList(String queryWord) {

        METHOD_NAME = "Dictionary/SearchWord/";

// HTTP Get
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME + queryWord);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setRequestMethod("GET"); // http verb
            urlConnection.setRequestProperty("Accept", "text/json");

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();

            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {

            Log.e("ServiceData Exception", e.toString());
        }

        return sResponse;
    }

    public static String getWordDetail(String queryWord) {

        METHOD_NAME = "Dictionary/SearchWordDetails/";

// HTTP Get
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME + queryWord);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setRequestMethod("GET"); // http verb
            urlConnection.setRequestProperty("Accept", "text/json");

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();

            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {

            Log.e("ServiceData Exception", e.toString());
        }

        return sResponse;
    }

    public static String postHistoryService(JSONObject jsonObject) {
        METHOD_NAME = "AddSearchedWord/";
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST"); // http verb
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            OutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.writeBytes(jsonObject.toString());
            wr.write(jsonObject.toString().getBytes());

            wr.flush();

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();
            wr.close();
            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {
//            return e.getMessage();
            Log.e("PostComment Exception", e.toString());
        }

        return sResponse;
    }

    public static String getHistoryService(JSONObject jsonObject) {
        METHOD_NAME = "GetSearchedWord/";
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST"); // http verb
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            OutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.writeBytes(jsonObject.toString());
            wr.write(jsonObject.toString().getBytes());

            wr.flush();

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();
            wr.close();
            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {
//            return e.getMessage();
            Log.e("PostComment Exception", e.toString());
        }

        return sResponse;
    }

    public static String postAddFavService(JSONObject jsonObject) {
        METHOD_NAME = "AddFavouriteWord/";
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST"); // http verb
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            OutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.writeBytes(jsonObject.toString());
            wr.write(jsonObject.toString().getBytes());

            wr.flush();

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();
            wr.close();
            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {
//            return e.getMessage();
            Log.e("Service Exception", e.toString());
        }

        return sResponse;
    }

    public static String postRemoveFavService(JSONObject jsonObject) {
        METHOD_NAME = "DeleteWord/";
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST"); // http verb
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            OutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.writeBytes(jsonObject.toString());
            wr.write(jsonObject.toString().getBytes());

            wr.flush();

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();
            wr.close();
            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {
//            return e.getMessage();
            Log.e("PostComment Exception", e.toString());
        }

        return sResponse;
    }

    public static String postGetFavService(JSONObject jsonObject) {
        METHOD_NAME = "GetFavouriteWord/";
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST"); // http verb
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            OutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.writeBytes(jsonObject.toString());
            wr.write(jsonObject.toString().getBytes());

            wr.flush();

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();
            wr.close();
            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {
//            return e.getMessage();
            Log.e("PostComment Exception", e.toString());
        }

        return sResponse;
    }

    public static String getSpellingCheck(String queryWord) {

        METHOD_NAME = "Dictionary/CheckSpelling/";

// HTTP Get
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME + queryWord);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setRequestMethod("GET"); // http verb
            urlConnection.setRequestProperty("Accept", "text/json");

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();

            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {

            Log.e("ServiceData Exception", e.toString());
        }

        return sResponse;
    }

    public static String getWordOfTheDay() {

        METHOD_NAME = "GetWordOfTheDay/";

// HTTP Get
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setRequestMethod("GET"); // http verb
            urlConnection.setRequestProperty("Accept", "text/json");

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();

            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {

            Log.e("ServiceData Exception", e.toString());
        }

        return sResponse;
    }

    public static String PostUserIdService(JSONObject jsonObject) {
        METHOD_NAME = "AddUser/";
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST"); // http verb
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            OutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.writeBytes(jsonObject.toString());
            wr.write(jsonObject.toString().getBytes());

            wr.flush();

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();
            wr.close();
            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {
//            return e.getMessage();
            Log.e("PostComment Exception", e.toString());
        }

        return sResponse;
    }

    public static String getUserPreferences(String userId) {

        METHOD_NAME = "Dictionary/UserPrefrence/";

// HTTP Get
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME + userId);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setRequestMethod("GET"); // http verb
            urlConnection.setRequestProperty("Accept", "text/json");

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();

            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {

            Log.e("ServiceData Exception", e.toString());
        }

        return sResponse;
    }
    public static String updateUserPreferencesService(JSONObject jsonObject) {
        METHOD_NAME = "UpdateUserPrefrence";
        try {

            URL url = new URL(SERVICE_URL + METHOD_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true); // this will tell that you will read "something"
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST"); // http verb
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            OutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.writeBytes(jsonObject.toString());
            wr.write(jsonObject.toString().getBytes());

            wr.flush();

            InputStream in = urlConnection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            sResponse = total.toString();
            wr.close();
            in.close();
            urlConnection.disconnect();

        } catch (Exception e) {
//            return e.getMessage();
            Log.e("PostComment Exception", e.toString());
        }

        return sResponse;
    }

}
