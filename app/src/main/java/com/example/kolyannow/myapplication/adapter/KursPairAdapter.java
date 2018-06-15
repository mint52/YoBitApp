package com.example.kolyannow.myapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kolyannow.myapplication.Pair;
import com.example.kolyannow.myapplication.R;

import java.util.ArrayList;

public class KursPairAdapter extends BaseAdapter{
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Pair> pairs;

    public KursPairAdapter(Context context, ArrayList<Pair> pairs) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pairs = pairs;
    }


    @Override
    public int getCount() {
        return pairs.size();
    }

    @Override
    public Object getItem(int position) {
        return pairs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.activity_kurs, parent, false );
        }

        Pair pair = getPair(position);

        // заполняем View в пункте списка данными

        ((TextView)view.findViewById(R.id.pair)).setText(pair.getName());
        ((TextView)view.findViewById(R.id.kurs)).setText(pair.getPrice());
        return view;
    }

    Pair getPair(int position){
        return (Pair) getItem(position);
    }
}
