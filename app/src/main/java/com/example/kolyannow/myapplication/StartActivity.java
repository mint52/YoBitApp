package com.example.kolyannow.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
    }

    public void invest (View view){
        Intent invest = new Intent(StartActivity.this, InvestActivity.class);
        startActivity(invest);
    }

    public void kurs (View view){
        Intent kurs = new Intent(StartActivity.this, KursActivity.class);
        startActivity(kurs);
    }
}
