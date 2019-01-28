package com.pindelia.android.vigilsoft.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Utils {
    public static Bitmap ConvertStringToBitmap(String data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());

        return BitmapFactory.decodeStream(in);
    }

    public static String ConvertBitmapToString(Bitmap data) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        data.compress(Bitmap.CompressFormat.PNG, 0, stream);

        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    public static boolean isValide(String str) {
        return str.length() == 2;
    }


}
