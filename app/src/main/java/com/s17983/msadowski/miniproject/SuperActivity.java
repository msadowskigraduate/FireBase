package com.s17983.msadowski.miniproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SuperActivity extends AppCompatActivity {
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;


    protected static final String font = "font";
    protected static final String color = "color";

    public static String fontValue;
    public static int colorValue;

    protected ViewGroup VIEW_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences("preference_name" , 0);
        editor = settings.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super);
    }

    protected void savePreferances(String key , String value)
    {
        editor.putString(key,value);
        editor.commit();
    }

    protected String loadPreferances(String key)
    {
        String value = settings.getString(key, "12.0");
        fontValue = value;
        return value;
    }

    public void changeColorOnAllViews(ViewGroup parent)
    {
        for (int count=0; count < parent.getChildCount(); count++){
            View view = parent.getChildAt(count);
            if(view instanceof TextView){
                ((TextView)view).setTextColor(loadColor());
            } else if(view instanceof ViewGroup){
                changeColorOnAllViews((ViewGroup)view);
            }
        }
    }

    public void changeTextSizeOnAllViews(ViewGroup parent)
    {
        for (int count=0; count < parent.getChildCount(); count++){
            View view = parent.getChildAt(count);
            if(view instanceof TextView){
                ((TextView)view).setTextSize(Float.parseFloat(loadPreferances(font)));
            } else if(view instanceof ViewGroup){
                changeTextSizeOnAllViews((ViewGroup)view);
            }
        }
    }

    public int loadColor()
    {
        String colorCode = loadPreferances(color);
        switch (colorCode)
        {
            case "blue":
                colorValue = Color.BLUE;
                return Color.BLUE;

            case "black":
                colorValue = Color.BLACK;
                return Color.BLACK;

            default:
                colorValue = Color.BLACK;
                return Color.BLACK;

        }
    }
}
