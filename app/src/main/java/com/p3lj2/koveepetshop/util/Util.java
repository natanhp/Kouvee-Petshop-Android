package com.p3lj2.koveepetshop.util;

import android.app.AlertDialog;
import android.content.Context;

import org.apache.commons.collections4.BidiMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static <K, V> K getKey(BidiMap<K, V> map, V value) {
        return map.inverseBidiMap().get(value);
    }

    public static AlertDialog.Builder confirmationDialog(String title, String message, Context context) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message);
    }

    public static String dateFormater(String date) {
        Locale locale = new Locale("in", "ID");
        DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", locale);
        String outputFormated = "";
        try {
            Date formatedInput = inputDate.parse(date);
            if (formatedInput != null) {
                outputFormated = outputDate.format(formatedInput);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputFormated;
    }

    public static String stdDateFormater(Date date) {
        DateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
        String outputFormated;
        outputFormated = outputDate.format(date);

        return outputFormated;
    }
}
