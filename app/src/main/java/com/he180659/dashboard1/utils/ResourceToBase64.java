package com.he180659.dashboard1.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.he180659.dashboard1.model.Product;

public class ResourceToBase64 {

    public static String resourceToBase64(Context context, int resourceId) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
            // Resize để tránh kích thước quá lớn
            bitmap = resizeBitmap(bitmap, 600, 600);
            return Product.bitmapToBase64(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > maxWidth || height > maxHeight) {
            float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
            width = Math.round(width * ratio);
            height = Math.round(height * ratio);

            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        return bitmap;
    }
}