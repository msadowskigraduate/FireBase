package com.s17983.msadowski.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends SuperActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.prefs);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , OptionsActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = findViewById(R.id.superTest);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , ProductListActivity.class);
                startActivity(intent);
            }
        });

        VIEW_ADDRESS = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);


        changeColorOnAllViews(VIEW_ADDRESS);
        changeTextSizeOnAllViews(VIEW_ADDRESS);
    }

}
