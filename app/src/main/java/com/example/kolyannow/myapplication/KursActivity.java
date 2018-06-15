package com.example.kolyannow.myapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.example.kolyannow.myapplication.adapter.KursPairAdapter;
import com.example.kolyannow.myapplication.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.kolyannow.myapplication.InvestActivity.LOG_TAG;

public class KursActivity extends Activity {


    AutoCompleteTextView autoCompleteTextView;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<Pair> pairs = new ArrayList<>();
    ArrayList<String> listPair = new ArrayList<>();
    ArrayList<String> pair_list = new ArrayList<>();
    Button add;
    HashMap<String, String> map;
    DecimalFormat df = new DecimalFormat("#.00");
    DatabaseHandler db;
    KursPairAdapter kursPairAdapter;
//    SimpleAdapter com.example.kolyannow.myapplication.adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurs);
        db = new DatabaseHandler(this);
        new  ParsePair().execute();
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        listView = findViewById(R.id.list_viev);
        add = findViewById(R.id.button5);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (db.getPairCount() != 0) {
            ParseKursPairThread();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            kursPairAdapter = new KursPairAdapter(KursActivity.this, pairs);
//            SimpleAdapter adapter = new SimpleAdapter(KursActivity.this, arrayList, R.layout.list_kurs,
//                    new String[]{"Pair", "Kurs"},
//                    new int[]{R.id.pair, R.id.kurs});
//            listView.setAdapter(adapter);
        }
    };

//    @SuppressLint("HandlerLeak")
//    Handler handlerParsPair = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            ArrayAdapter<String> com.example.kolyannow.myapplication.adapter = new ArrayAdapter<>(KursActivity.this, R.layout.support_simple_spinner_dropdown_item, listPair);
//            autoCompleteTextView.setAdapter(com.example.kolyannow.myapplication.adapter);
//        }
//    };


    //    @Override
//    protected void onResume() {
//        super.onResume();
//        refreshe();
//
//    }

    public void addPair(View view){
        db.addPair(new Pair(autoCompleteTextView.getText().toString()));
//        pair_list.add(autoCompleteTextView.getText().toString());
        ParseKursPairThread();

    }

//    public void refreshe(){
//        arrayList.clear();
//        List<Pair> pairs = db.getAllPairs();
//        for (Pair pair: pairs) {
//            new ParseKursPair().execute(pair.getName());
//        }
//    }

    public void refrashe(View view){
        ParseKursPairThread();
    }


    public void ParseKursPairThread() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                arrayList.clear();
                List<Pair> pairs = db.getAllPairs();
                for (Pair pair: pairs) {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    String resultJson = "";

                    try {
                        URL url = new URL("https://yobit.net/api/3/ticker/"+pair.getName());

                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();

                        InputStream inputStream = urlConnection.getInputStream();
                        StringBuffer buffer = new StringBuffer();

                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }

                        resultJson = buffer.toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONObject dataJsonObj = null;

                    try {
                        dataJsonObj = new JSONObject(resultJson);
                        JSONArray array = dataJsonObj.names();
                        JSONObject object = dataJsonObj.getJSONObject(array.getString(0));
                        map = new HashMap<>();
                        map.put("Pair",array.getString(0));
                        map.put("Kurs", df.format(Double.parseDouble(object.getString("last"))));
                        arrayList.add(map);

//                        SimpleAdapter com.example.kolyannow.myapplication.adapter = new SimpleAdapter(KursActivity.this, arrayList, R.layout.list_kurs,
//                                new String[]{"Pair", "Kurs"},
//                                new int[]{R.id.pair, R.id.kurs});
//                        listView.setAdapter(com.example.kolyannow.myapplication.adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(0);

                }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

//    private class ParseKursPair extends AsyncTask<String, Void, String> {
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String resultJson = "";
//
//
//        @Override
//        protected String doInBackground(String... strings) {
//            // получаем данные с внешнего ресурса
//            try {
//                String urrrl = "https://yobit.net/api/3/ticker/"+strings[0];
//                URL url = new URL(urrrl);
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                }
//
//                resultJson = buffer.toString();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return resultJson;
//        }
//
//
//        @Override
//        protected void onPostExecute(String strJson) {
//            super.onPostExecute(strJson);
//
//            JSONObject dataJsonObj = null;
//
//            try {
//                dataJsonObj = new JSONObject(strJson);
//                JSONArray array = dataJsonObj.names();
//                JSONObject object = dataJsonObj.getJSONObject(array.getString(0));
//                map = new HashMap<>();
//                map.put("Pair",array.getString(0));
//                map.put("Kurs", df.format(Double.parseDouble(object.getString("last"))));
//                arrayList.add(map);
//
//                SimpleAdapter com.example.kolyannow.myapplication.adapter = new SimpleAdapter(KursActivity.this, arrayList, R.layout.list_kurs,
//                        new String[]{"Pair", "Kurs"},
//                        new int[]{R.id.pair, R.id.kurs});
//                listView.setAdapter(com.example.kolyannow.myapplication.adapter);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

//    public void ParsePairThread(){
//        Runnable runnable = new Runnable(){
//
//            @Override
//            public void run() {
//                pair_list.clear();
//                HttpURLConnection urlConnection = null;
//                BufferedReader reader = null;
//                String resultJson = "";
//
//                try {
//                    URL url = new URL("https://yobit.net/api/3/info");
//
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.connect();
//
//                    InputStream inputStream = urlConnection.getInputStream();
//                    StringBuffer buffer = new StringBuffer();
//
//                    reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        buffer.append(line);
//                    }
//
//                    resultJson = buffer.toString();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                JSONObject dataJsonObj = null;
//
//                try {
//                    dataJsonObj = new JSONObject(resultJson);
//                    JSONObject pair = dataJsonObj.getJSONObject("pairs");
//                    JSONArray pairs = pair.names();
//
//                    for (int i = 0; i < pairs.length(); i++){
//                        pair_list.add(pairs.getString(i));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////                ArrayAdapter<String> com.example.kolyannow.myapplication.adapter = new ArrayAdapter<>(KursActivity.this, R.layout.support_simple_spinner_dropdown_item, list);
////                autoCompleteTextView.setAdapter(com.example.kolyannow.myapplication.adapter);
//                handlerParsPair.sendEmptyMessage(0);
//
//            }
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();
//
//    }

    private class ParsePair extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
//             получаем данные с внешнего ресурса
            try {
                URL url = new URL("https://yobit.net/api/3/info");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку

            Log.d(LOG_TAG, strJson);

            JSONObject dataJsonObj = null;
            ArrayList<String> list = new ArrayList<>();

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONObject pair = dataJsonObj.getJSONObject("pairs");
                JSONArray pairs = pair.names();

                for (int i = 0; i < pairs.length(); i++){
                    list.add(pairs.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(KursActivity.this, R.layout.support_simple_spinner_dropdown_item, list);
            autoCompleteTextView.setAdapter(adapter);
        }
    }
}
