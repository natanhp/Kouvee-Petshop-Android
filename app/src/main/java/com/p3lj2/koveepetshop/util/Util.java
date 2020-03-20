package com.p3lj2.koveepetshop.util;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import org.apache.commons.collections4.BidiMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    public static String getUriPath(Uri uri, Context context) {
        String wholeID = DocumentsContract.getDocumentId(uri);

        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        String filePath = "";

        int columnIndex;

        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }

        return filePath;
    }

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
        String outputFormated = "";
        outputFormated = outputDate.format(date);

        return outputFormated;
    }
}
