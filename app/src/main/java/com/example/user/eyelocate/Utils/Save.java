package com.example.user.eyelocate.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Save {

    public static void save(Context ctx, String name, String value){

        SharedPreferences s = ctx.getSharedPreferences("clipcodes", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = s.edit();
        edt.putString(name, value);
        edt.apply();
    }

    public static String read(Context ctx, String name, String defaultvalue){

        SharedPreferences s = ctx.getSharedPreferences("clipcodes", Context.MODE_PRIVATE);
        return s.getString(name, defaultvalue);
    }
}
