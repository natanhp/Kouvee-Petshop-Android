package com.p3lj2.koveepetshop.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductTransactionDetailModel;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.model.SupplierModel;

import org.apache.commons.collections4.BidiMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Response;


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

    public static String dateFormater(String oldFormat, String newFormat, String date) {
        Date inputDate = new Date();
        try {
            inputDate = new SimpleDateFormat(oldFormat).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Locale localeIndonesia = new Locale("in", "ID");
        DateFormat outputDate = new SimpleDateFormat(newFormat, localeIndonesia);

        return outputDate.format(Objects.requireNonNull(inputDate));
    }

    public static void createRestockInvoicePfg(Context context, List<ProductModel> productModels, SupplierModel supplierModel, String poCode, String date) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint borderPaint = new Paint();
        int y = 0;
        int pageNumber = 1;

        borderPaint.setStyle(Paint.Style.STROKE);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Rect rect = new Rect(8, 8, canvas.getWidth() - 8, canvas.getHeight() - 8);
        canvas.drawRect(rect, borderPaint);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.report_header);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 550, 150, true);
        int xHeader = (canvas.getWidth() - bitmapResized.getWidth() + 8) / 2;
        y += 9;
        canvas.drawBitmap(bitmapResized, xHeader, y, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        y += 10 + bitmapResized.getHeight();
        int xPOCode = canvas.getWidth() - (canvas.getWidth() / 3);
        canvas.drawText("NO : " + poCode, xPOCode, y, paint);
        y += paint.getTextSize() + 10;
        canvas.drawText("Tanggal : " + dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", date), xPOCode, y, paint);
        int xTo = xPOCode / 8;
        paint.setTypeface(Typeface.DEFAULT);
        canvas.drawText("Kepada Yth:", xTo, y, paint);
        y += paint.getTextSize() + 10;
        canvas.drawText(supplierModel.getName(), xTo, y, paint);
        y += paint.getTextSize() + 10;
        canvas.drawText(supplierModel.getAddress(), xTo, y, paint);
        y += paint.getTextSize() + 10;
        canvas.drawText(supplierModel.getPhoneNumber(), xTo, y, paint);
        y += 2 * (paint.getTextSize() + 10);
        canvas.drawText("Mohon untuk disediakan produk-produk berikut ini : ", xTo, y, paint);
        y += paint.getTextSize() + 10;
        List<ProductModel> productModelList = new ArrayList<>();
        ProductModel productModel = new ProductModel();
        productModel.setProductName("Nama Produk");
        productModel.setMeassurement("Satuan");
        productModelList.add(productModel);
        productModelList.addAll(productModels);

        for (int i = 0; i < productModelList.size(); i++) {
            canvas.drawLine(xTo, y, canvas.getWidth() - xTo, y, paint);
            canvas.drawLine(xTo, y, xTo, y + (2 * (paint.getTextSize())) + 15, paint);
            int xVerticalLine = xTo + 35;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            xVerticalLine += 300;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            xVerticalLine += 90;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            xVerticalLine = canvas.getWidth() - xTo;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            y += paint.getTextSize() + 10;
            if (i == 0) {
                canvas.drawText("No", xTo + 10, y, paint);
            } else {
                canvas.drawText(String.valueOf(i), xTo + 10, y, paint);
            }
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(productModelList.get(i).getProductName(), xTo + 50, y, paint);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(productModelList.get(i).getMeassurement(), xTo + 380, y, paint);
            if (i == 0) {
                canvas.drawText("Jumlah", xTo + 460, y, paint);
            } else  {
                canvas.drawText(String.valueOf(productModelList.get(i).getProductQuantity()), xTo + 460, y, paint);
            }
            y += paint.getTextSize() + 5;
            canvas.drawLine(xTo, y, canvas.getWidth() - xTo, y, paint);
            if (y >= canvas.getHeight() - 100) {
                y = 0;
                pdfDocument.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber++).create();
                page = pdfDocument.startPage(pageInfo);
                canvas = page.getCanvas();
                rect = new Rect(8, 8, canvas.getWidth() - 8, canvas.getHeight() - 8);
                canvas.drawRect(rect, borderPaint);
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.report_header_general);
                bitmapResized = Bitmap.createScaledBitmap(bitmap, 550, 150, true);
                xHeader = (canvas.getWidth() - bitmapResized.getWidth() + 8) / 2;
                y += 9;
                canvas.drawBitmap(bitmapResized, xHeader, y, paint);

                paint.setTextAlign(Paint.Align.LEFT);

                y += 10 + bitmapResized.getHeight();
                canvas.drawLine(xTo, y, canvas.getWidth() - xTo, y, paint);
            }
        }

        paint.setTextAlign(Paint.Align.RIGHT);
        Locale locale = new Locale("in", "ID");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", locale);
        canvas.drawText("Dicetak tanggal " + simpleDateFormat.format(Calendar.getInstance().getTime()), canvas.getWidth() - 50, canvas.getHeight() - 50, paint);

        pdfDocument.finishPage(page);
        File file = new File(context.getExternalFilesDir(null), "/" + poCode + ".pdf");
        Toast.makeText(context, "Surat pemesanan disimpan di " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
    }

    public static void createProductTransactionInvoice(Context context, ProductTransactionModel productTransactionModel, EmployeeModel employeeModel, double total, double discount) {
        String transactionId = productTransactionModel.getId();
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint borderPaint = new Paint();
        int y = 0;
        int pageNumber = 1;

        borderPaint.setStyle(Paint.Style.STROKE);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Rect rect = new Rect(8, 8, canvas.getWidth() - 8, canvas.getHeight() - 8);
        canvas.drawRect(rect, borderPaint);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.transaction_invoice_header);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 550, 150, true);
        int xHeader = (canvas.getWidth() - bitmapResized.getWidth() + 8) / 2;
        y += 9;
        canvas.drawBitmap(bitmapResized, xHeader, y, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        y += 10 + bitmapResized.getHeight();
        int xDate= canvas.getWidth() - (canvas.getWidth() / 3);
        canvas.drawText(dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy HH:mm", productTransactionModel.getCreatedAt()), xDate, y, paint);
        y += paint.getTextSize() + 30;
        canvas.drawText("CS    : " + productTransactionModel.getCsName(), xDate, y, paint);
        y += paint.getTextSize() + 8;
        canvas.drawText("Kasir : " + employeeModel.getName(), xDate, y, paint);
        int xTransactionCode = xDate / 8;
        y -= (paint.getTextSize() * 2) + 28;
        canvas.drawText(transactionId, xTransactionCode, y, paint);
        y += paint.getTextSize() + 20;
        canvas.drawText("Member : " + productTransactionModel.getCustomer().getName(), xTransactionCode, y, paint);
        y += paint.getTextSize() + 8;
        canvas.drawText("Telepon : " + productTransactionModel.getCustomer().getPhoneNumber(), xTransactionCode, y, paint);
        Bitmap productImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.product_line);
        Bitmap productImgResized = Bitmap.createScaledBitmap(productImg, 545, 45, true);
        xTransactionCode = (canvas.getWidth() - bitmapResized.getWidth() + 8) / 2;
        y += 20;
        canvas.drawBitmap(productImgResized, xTransactionCode, y, paint);
        List<ProductTransactionDetailModel> productTransactionDetailModels = new ArrayList<>();
        ProductTransactionDetailModel productTransactionDetailModel = new ProductTransactionDetailModel();
        productTransactionDetailModels.add(productTransactionDetailModel);
        productTransactionDetailModels.addAll(productTransactionModel.getProductTransactionkDetails());
        y += productImgResized.getHeight() + 10;
        for (int i = 0; i < productTransactionDetailModels.size(); i++) {
            canvas.drawLine(xTransactionCode, y, canvas.getWidth() - xTransactionCode, y, paint);
            canvas.drawLine(xTransactionCode, y, xTransactionCode, y + (2 * (paint.getTextSize())) + 15, paint);
            int xVerticalLine = xTransactionCode + 35;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            xVerticalLine += 220;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            xVerticalLine += 110;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            xVerticalLine += 80;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            xVerticalLine = canvas.getWidth() - xTransactionCode;
            canvas.drawLine(xVerticalLine, y, xVerticalLine, y + (2 * (paint.getTextSize())) + 15, paint);
            y += paint.getTextSize() + 10;
            if (i == 0) {
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText("No", xTransactionCode + 10, y, paint);
                paint.setTypeface(Typeface.DEFAULT);
            } else {
                canvas.drawText(String.valueOf(i), xTransactionCode + 10, y, paint);
            }

            ProductModel productModel = productTransactionDetailModels.get(i).getProductModel();

            paint.setTextAlign(Paint.Align.LEFT);
            if (i == 0) {
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText(context.getString(R.string.product_name), xTransactionCode + 50, y, paint);
                canvas.drawText(context.getString(R.string.price), xTransactionCode + 265, y, paint);
                canvas.drawText(context.getString(R.string.price), xTransactionCode + 455, y, paint);
                paint.setTypeface(Typeface.DEFAULT);
            } else {
                canvas.drawText(productModel.getProductName(), xTransactionCode + 50, y, paint);
                canvas.drawText("Rp " + productModel.getProductPrice(), xTransactionCode + 265, y, paint);
                canvas.drawText("Rp " + (productModel.getProductPrice() * productTransactionDetailModels.get(i).getItemQty()), xTransactionCode + 455, y, paint);
            }
            paint.setTextAlign(Paint.Align.CENTER);

            if (i == 0) {
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText(context.getString(R.string.product_quantity), xTransactionCode + 405, y, paint);
                paint.setTypeface(Typeface.DEFAULT);
            } else  {
                canvas.drawText(String.valueOf(productTransactionDetailModels.get(i).getItemQty()), xTransactionCode + 405, y, paint);
            }
            y += paint.getTextSize() + 5;
            canvas.drawLine(xTransactionCode, y, canvas.getWidth() - xTransactionCode, y, paint);
            if (y >= canvas.getHeight() - 240) {
                y = 0;
                pdfDocument.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber++).create();
                page = pdfDocument.startPage(pageInfo);
                canvas = page.getCanvas();
                rect = new Rect(8, 8, canvas.getWidth() - 8, canvas.getHeight() - 8);
                canvas.drawRect(rect, borderPaint);
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.report_header_general);
                bitmapResized = Bitmap.createScaledBitmap(bitmap, 550, 150, true);
                xHeader = (canvas.getWidth() - bitmapResized.getWidth() + 8) / 2;
                y += 9;
                canvas.drawBitmap(bitmapResized, xHeader, y, paint);

                paint.setTextAlign(Paint.Align.LEFT);

                y += 10 + bitmapResized.getHeight();
                canvas.drawLine(xTransactionCode, y, canvas.getWidth() - xTransactionCode, y, paint);
            }
        }

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Sub Total Rp " + total, canvas.getWidth() - 50, canvas.getHeight() - 100, paint);
        canvas.drawText("Diskon Rp " + discount, canvas.getWidth() - 50, canvas.getHeight() - 80, paint);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("TOTAL Rp " + (total - discount), canvas.getWidth() - 50, canvas.getHeight() - 50, paint);

        pdfDocument.finishPage(page);
        File file = new File(context.getExternalFilesDir(null), "/" + transactionId + ".pdf");
        Toast.makeText(context, "Surat pemesanan disimpan di " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
    }

    public static String retrofitErrorHandler(Response response) {
        Gson gson = new Gson();
        Type type = new TypeToken<ResponseSchema<ProductTransactionModel>>() {}.getType();
        ResponseSchema<Object> errorResponse = gson.fromJson(Objects.requireNonNull(response.errorBody()).charStream(), type);

        return errorResponse.getMessage();
    }
}
