package net.wowmaking;

import android.graphics.Bitmap;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

final class Utility {
    static void handleError(Exception e, Promise promise) {
        e.printStackTrace(System.err);
        promise.reject(e);
    }

    static File createRandomJPGFile(ReactContext context) {
        String filename = UUID.randomUUID().toString() + ".jpg";
        return new File(context.getFilesDir(), filename);
    }


    static void writeBMPToPNGFile(Bitmap bmp, File file, Promise promise) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (IOException e) {
            handleError(e, promise);
        }
    }
}
