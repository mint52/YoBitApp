package com.example.kolyannow.myapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kolyannow.myapplication.Pair;
import com.example.kolyannow.myapplication.R;
import com.example.kolyannow.myapplication.database.DatabaseHandler;

import java.util.ArrayList;

import static com.example.kolyannow.myapplication.R.layout.list_kurs;

public class KursPairAdapter extends ArrayAdapter<Pair>{

    DatabaseHandler db;
//    Context context;
//    LayoutInflater layoutInflater;
//    ArrayList<Pair> pairs;

//    public KursPairAdapter(Context context, ArrayList<Pair> pairs) {
//        this.context = context;
//        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.pairs = pairs;
//    }
//
    public KursPairAdapter(Context context, ArrayList<Pair> pairs){
        super(context, list_kurs, pairs);
    }
//
//
//    @Override
//    public int getCount() {
//        return pairs.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return pairs.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public void remove(@Nullable Pair object) {
               db.deletePair(object);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        View view = convertView;
        Pair pair = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(list_kurs, null);
        }
        // заполняем View в пункте списка данными

        ((TextView)convertView.findViewById(R.id.pair)).setText(pair.getName());
        ((TextView)convertView.findViewById(R.id.kurs)).setText(pair.getPrice());
        return convertView;

    }

//    Pair getPair(int position){
//        return (Pair) getItem(position);
//    }
}
