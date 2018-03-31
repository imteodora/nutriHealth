package com.nutrihealth.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by erkut on 10/24/17.
 */

public class BitmapUtils {

    private static final int SCALE_FACTOR = 250;

    public static Bitmap decodeUri(Context context, Uri imageUri) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));
        int newHeight;
        int newWidth;
        if(bitmap.getHeight() > bitmap.getWidth()) {
            newHeight = SCALE_FACTOR;
            newWidth  = (int) (bitmap.getWidth() * ((float) SCALE_FACTOR / bitmap.getHeight()));
        } else {
            newHeight = (int) (bitmap.getHeight() * ((float) SCALE_FACTOR / bitmap.getWidth()));
            newWidth  = SCALE_FACTOR;
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        bitmap.recycle();
        return scaledBitmap;
    }

    public static String encodeBitmapToBase64(Bitmap bitmap) {
        String result = "";
        if(bitmap == null) return result;
        try {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS);
            result = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
            byteArrayOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap resizeImage(Context context, Bitmap bitmap) throws FileNotFoundException {
     //   Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));
        int newHeight;
        int newWidth;
        if(bitmap.getHeight() > SCALE_FACTOR) {
            newHeight = SCALE_FACTOR;
            newWidth  = (int) (bitmap.getWidth() * ((float) SCALE_FACTOR / bitmap.getHeight()));
        } else if (bitmap.getWidth() > SCALE_FACTOR) {
            newHeight = (int) (bitmap.getHeight() * ((float) SCALE_FACTOR / bitmap.getWidth()));
            newWidth  = SCALE_FACTOR;
        } else {
            newWidth = bitmap.getWidth();
            newHeight = bitmap.getHeight();
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        bitmap.recycle();
        return scaledBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap source, int rotation) {
        Matrix matrix = new Matrix();
        matrix.setRotate(rotation, source.getWidth() / 2, source.getHeight() / 2);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }





}
