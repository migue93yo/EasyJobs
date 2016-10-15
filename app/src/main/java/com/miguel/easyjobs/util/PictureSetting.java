package com.miguel.easyjobs.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureSetting {

    public static final String RUTE_IMAGE = Environment.getExternalStorageDirectory() + File.separator
            + "EasyJobs";

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int width = 0;
        int height = 0;

        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = 1000;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getNormalOrientation(Bitmap bitmap, String path) {
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(new File(path).getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap cropBitmap(Bitmap original) {
        int width = original.getWidth();
        int height = original.getHeight();

        if (original.getWidth() > original.getHeight()) {
            width = original.getHeight();
        } else if (original.getHeight() > original.getWidth()) {
            height = original.getWidth();
        }

        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);

        Rect srcRect = new Rect(0, 0, original.getWidth(), original.getHeight());
        Rect dstRect = new Rect(0, 0, width, height);

        int dx = (srcRect.width() - dstRect.width()) / 2;
        int dy = (srcRect.height() - dstRect.height()) / 2;

        srcRect.inset(Math.max(0, dx), Math.max(0, dy));
        dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

        canvas.drawBitmap(original, srcRect, dstRect, null);

        original.recycle();

        return croppedImage;
    }

    public static Bitmap resizePicture(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);

        return resizedBitmap;

    }

    public static String getPath(Activity context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static boolean savePicture(Bitmap picture, String file) {
        boolean ok = false;
        File myPath = new File(RUTE_IMAGE + File.separator + file);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            picture.compress(Bitmap.CompressFormat.PNG, 10, fos);
            fos.flush();
            ok = true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ok;
    }

    public static Bitmap loadPicture(Activity context, String fileName, int pictureId) {
        ContentResolver cr = context.getContentResolver();
        File newFile = new File(PictureSetting.RUTE_IMAGE + File.separator + fileName + ".png");
        Uri uri = Uri.fromFile(newFile);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
        } catch (IOException e) {
            Drawable drawable = context.getResources().getDrawable(pictureId);
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        return bitmap;
    }

}
