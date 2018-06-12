package com.example.kolyannow.myapplication;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.ListView;
import android.widget.SimpleAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class KursActivity extends Activity {


    AutoCompleteTextView autoCompleteTextView;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<String> pair_list = new ArrayList<>();
    Button add;
    HashMap<String, String> map;
//    SimpleAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurs);
        new ParseKurs().execute();

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        listView = findViewById(R.id.list_viev);
        add = findViewById(R.id.button5);

//        map = new HashMap<>();
//        map.put("Pair","liza_rur");
//        map.put("Kurs","0.5");
//        arrayList.add(map);

        SimpleAdapter adapter = new SimpleAdapter(KursActivity.this, arrayList, R.layout.list_kurs,
                new String[]{"Pair", "Kurs"},
                new int[]{R.id.pair, R.id.kurs});
        listView.setAdapter(adapter);





    }

    public void addPair(View view){
        pair_list.add(autoCompleteTextView.getText().toString());

    }

    public void refrashe(View view){
        arrayList.clear();
        for (String pair: pair_list) {
            new ParseKursPair().execute(pair);
        }
        SimpleAdapter adapter = new SimpleAdapter(KursActivity.this, arrayList, R.layout.list_kurs,
                new String[]{"Pair", "Kurs"},
                new int[]{R.id.pair, R.id.kurs});
        listView.setAdapter(adapter);
    }


    private class ParseKursPair extends AsyncTask<String, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";


        @Override
        protected String doInBackground(String... strings) {
            // получаем данные с внешнего ресурса
            try {
                String urrrl = "https://yobit.net/api/3/ticker/"+strings[0];
                URL url = new URL(urrrl);

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

            JSONObject dataJsonObj = null;

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray array = dataJsonObj.names();
                JSONObject object = dataJsonObj.getJSONObject(array.getString(0));
                map = new HashMap<>();
                map.put("Pair",array.getString(0));
                map.put("Kurs",object.getString("last"));
                arrayList.add(map);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class ParseKurs extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
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

//            Log.d(LOG_TAG, strJson);

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
