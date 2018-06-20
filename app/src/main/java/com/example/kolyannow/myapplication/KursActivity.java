package com.example.kolyannow.myapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    ArrayList<Pair> pairsList = new ArrayList<>();
    Button add;
    HashMap<String, String> map;
    DecimalFormat df = new DecimalFormat("#.00");
    DatabaseHandler db;
    KursPairAdapter kursPairAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurs);
        db = new DatabaseHandler(this);
        new ParsePair().execute();
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        listView = findViewById(R.id.list_viev);
        add = findViewById(R.id.button5);
        registerForContextMenu(listView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                deletePair(info.position);    //метод, выполняющий действие при удалении пункта меню
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deletePair(int position) {
        Pair pair = kursPairAdapter.getItem(position);
        db.deletePairToName(pair);
        ParseKursPairThread();
        Toast.makeText(getApplicationContext(), "Пара " + pair.getName() + " удалена", Toast.LENGTH_SHORT).show();
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

            kursPairAdapter = new KursPairAdapter(KursActivity.this, pairsList);
            listView.setAdapter(kursPairAdapter);

        }
    };

    public void addPair(View view) {
        db.addPair(new Pair(autoCompleteTextView.getText().toString()));
        ParseKursPairThread();

    }

    public void refrashe(View view) {
        ParseKursPairThread();
    }

    public void ParseKursPairThread() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
//                arrayList.clear();
                pairsList.clear();
                List<Pair> pairs = db.getAllPairs();
                for (Pair pair : pairs) {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    String resultJson = "";

                    try {
                        URL url = new URL("https://yobit.net/api/3/ticker/" + pair.getName());

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

                    JSONObject dataJsonObj;

                    try {
                        dataJsonObj = new JSONObject(resultJson);
                        JSONArray array = dataJsonObj.names();
                        JSONObject object = dataJsonObj.getJSONObject(array.getString(0));

                        pairsList.add(new Pair(array.getString(0), df.format(Double.parseDouble(object.getString("last")))));

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


    private class ParsePair extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {

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

            JSONObject dataJsonObj = null;
            ArrayList<String> list = new ArrayList<>();

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONObject pair = dataJsonObj.getJSONObject("pairs");
                JSONArray pairs = pair.names();

                for (int i = 0; i < pairs.length(); i++) {
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
