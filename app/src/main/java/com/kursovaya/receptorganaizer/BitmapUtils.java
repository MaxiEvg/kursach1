package com.kursovaya.receptorganaizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

    public static String saveBitmapToFile(Context context, Bitmap bitmap, String fileName) throws IOException {
        File directory = context.getExternalFilesDir(null);
        File imageFile = new File(directory, fileName);
        FileOutputStream fos = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.WEBP, 90, fos);
        fos.close();
        return imageFile.getAbsolutePath();
    }

    public static Bitmap loadBitmapFromFile(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    public static Bitmap getBitmapFromIntent(Context context, Intent data) throws IOException {
        Uri imageUri = data.getData();
        assert imageUri != null;
        InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        assert imageStream != null;
        imageStream.close();
        return bitmap;
    }
}
