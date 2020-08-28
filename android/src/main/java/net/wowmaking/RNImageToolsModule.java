package net.wowmaking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Base64;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


import java.io.File;

public class RNImageToolsModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNImageToolsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @ReactMethod
    public void merge(String encodedImage1, String encodedImage2, Promise promise) {
        byte[] decodedString1 = Base64.decode(encodedImage1, Base64.DEFAULT);
        byte[] decodedString2 = Base64.decode(encodedImage2, Base64.DEFAULT);
        Bitmap firstBmp = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
        Bitmap secondBmp = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
        Bitmap editBmp = Bitmap.createBitmap(firstBmp.getWidth(), firstBmp.getHeight()+secondBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(editBmp);
        canvas.drawBitmap(firstBmp, new Matrix(), null);

        Rect srcRectFirst = new Rect(0, 0, firstBmp.getWidth(), firstBmp.getHeight());
        Rect dstRectFirst = new Rect(0, secondBmp.getHeight(), canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(firstBmp, srcRectFirst, dstRectFirst, null);

        Rect srcRectSecond = new Rect(0, 0, secondBmp.getWidth(), secondBmp.getHeight());
        Rect dstRectSecond = new Rect(0, 0, canvas.getWidth(), canvas.getHeight()-firstBmp.getHeight());
        canvas.drawBitmap(secondBmp, srcRectSecond, dstRectSecond, null);

        File file = Utility.createRandomJPGFile(reactContext);
        Utility.writeBMPToPNGFile(editBmp, file, promise);

        promise.resolve("file://" + file.toString());
    }

    @Override
    public String getName() {
        return "RNImageTools";
    }
}
