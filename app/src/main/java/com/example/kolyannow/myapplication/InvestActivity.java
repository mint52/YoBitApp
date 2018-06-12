package com.example.kolyannow.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class InvestActivity extends AppCompatActivity {

    EditText money_count;
    EditText purchase_price;
    EditText sales_price;
    EditText invest_day;
    EditText percent_day;
    TextView result;
    public static String LOG_TAG = "my_log";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        new ParseTask().execute();

        money_count = findViewById(R.id.money_count);
        purchase_price = findViewById(R.id.purchase_price);
        sales_price = findViewById(R.id.sales_price);
        invest_day = findViewById(R.id.invest_day);
        percent_day = findViewById(R.id.percent_day);
        result = findViewById(R.id.result);



    }


    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("https://yobit.net/api/3/ticker/liza_rur");

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
            String secondName = "";

            try {
                dataJsonObj = new JSONObject(strJson);
//                JSONArray friends = dataJsonObj.getJSONArray("liza_rur");

                // 1. достаем инфо о втором друге - индекс 1
                JSONObject secondFriend = dataJsonObj.getJSONObject("liza_rur");
//                purchase_price.setText(secondFriend.getString("avg"));
                sales_price.setText(secondFriend.getString("last"));
//                Log.d(LOG_TAG, "Второе имя: " + secondName);

                // 2. перебираем и выводим контакты каждого друга
//                for (int i = 0; i < friends.length(); i++) {
//                    JSONObject friend = friends.getJSONObject(i);
//
//                    JSONObject contacts = friend.getJSONObject("contacts");
//
//                    String phone = contacts.getString("mobile");
//                    String email = contacts.getString("email");
//                    String skype = contacts.getString("skype");
//
//                    Log.d(LOG_TAG, "phone: " + phone);
//                    Log.d(LOG_TAG, "email: " + email);
//                    Log.d(LOG_TAG, "skype: " + skype)
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void Clickbutton (View v){

        double day = Double.parseDouble(String.valueOf(invest_day.getText()));
        double a = Double.parseDouble(String.valueOf(money_count.getText()));
        double a1 = Double.parseDouble(String.valueOf(money_count.getText()));
        double p = Double.parseDouble(String.valueOf(percent_day.getText()));
        for (int i = 0; i < day; i++) {
            a += a * p / 100;
        }
                DecimalFormat df = new DecimalFormat("#.00");
                String res = "";
        if (purchase_price.getText().toString().equals("")){
           res = "Монет в конце инвеста: "+df.format(a)+"\n"+
                    "Стоимость монет: "+df.format( a * Double.parseDouble(String.valueOf(sales_price.getText())))+" Руб.";
        }
        else {
            res = "Профит в процентах: " + df.format((a - a1) / a1 * 100) + "\n" +
                    "Монет в конце инвеста: " + df.format(a) + "\n" +
                    "Стоимость монет: " + df.format(a * Double.parseDouble(String.valueOf(sales_price.getText()))) + " Руб.\n" +
                    "Профит в рублях чистыми: " + df.format(a * Double.parseDouble(String.valueOf(sales_price.getText()))
                    - Double.parseDouble(String.valueOf(money_count.getText())) * Double.parseDouble(String.valueOf(purchase_price.getText())))+" Руб.";
        }
        result.setText(res);

    }

    public void Clickbutton2 (View v){
        new ParseTask().execute();
    }
}
