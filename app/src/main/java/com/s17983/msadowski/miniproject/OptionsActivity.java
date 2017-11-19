package com.s17983.msadowski.miniproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends SuperActivity{

    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

    RadioButton blueText;
    RadioButton blackText;

    public void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences("preference_name" , 0);
        editor = settings.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Layout controls - text size

        RadioButton largeText = (RadioButton) findViewById( R.id.textSizeLarge );
        largeText.setOnClickListener( new View.OnClickListener() {
            public void onClick( View view ) {
                Toast.makeText(getBaseContext().getApplicationContext(), "Large Text Selected", Toast.LENGTH_SHORT).show();
                savePreferances(font , "25" );
            }
        } );

        RadioButton smallText = ( RadioButton ) findViewById( R.id.textSizeSmall );
        smallText.setOnClickListener( new View.OnClickListener() {
            public void onClick( View view ) {
                Toast.makeText(getBaseContext().getApplicationContext(), "Small Text Selected", Toast.LENGTH_SHORT).show();
                savePreferances(font , "12" );
            }
        } );

        //Layout - Color
        blueText = (RadioButton) findViewById( R.id.blueText);
        blackText = ( RadioButton ) findViewById( R.id.blackText );

        VIEW_ADDRESS = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        changeColorOnAllViews(VIEW_ADDRESS);
        changeTextSizeOnAllViews(VIEW_ADDRESS);
    }
    public void colorChange(View v)
    {
        Toast.makeText(getBaseContext().getApplicationContext(), "Click!", Toast.LENGTH_SHORT).show();
        String colorCode = "";
        if(blueText.isChecked())
        {
            Log.i("Currently checked" , "blue");
            colorCode = "blue";
        }
        else if(blackText.isChecked())
        {
            Log.i("Currently checked" , "black");
            colorCode = "black";
        }

        savePreferances(color, colorCode);
        changeColorOnAllViews((ViewGroup) findViewById(R.id.parentView));
        changeTextSizeOnAllViews((ViewGroup) findViewById(R.id.parentView));
    }

    public void returnOnClick(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
