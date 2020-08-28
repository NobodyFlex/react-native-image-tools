
package net.wowmaking;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import java.io.File;
import java.util.HashMap;

public class RNImageToolsModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNImageToolsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @ReactMethod
    public void merge(encodedImage1, encodedImage2, Promise promise) {
        byte[] decodedString1 = Base64.decode(encodedImage1, Base64.DEFAULT)
        byte[] decodedString2 = Base64.decode(encodedImage2, Base64.DEFAULT)
        Bitmap firstBmp = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString.length);
        Bitmap secondBmp = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString.length);
        // Bitmap firstBmp = Utility.bitmapFromUriString(uriStrings.getString(0), promise, reactContext);
        // Bitmap secondBmp = Utility.bitmapFromUriString(uriStrings.getString(1), promise, reactContext);
        Bitmap editBmp = Bitmap.createBitmap(firstBmp.getWidth(), firstBmp.getHeight()+secondBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(editBmp);
        canvas.drawBitmap(firstBmp, new Matrix(), null);

        Rect srcRectFirst = new Rect(0, 0, firstBmp.getWidth(), firstBmp.getHeight());
        Rect dstRectFirst = new Rect(0, secondBmp.getHeight(), canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(firstBmp, srcRectFirst, dstRectFirst, null);

        Rect srcRectSecond = new Rect(0, 0, secondBmp.getWidth(), secondBmp.getHeight());
        Rect dstRectSecond = new Rect(0, 0, canvas.getWidth(), canvas.getHeight()-firstBmp.getHeight());
        canvas.drawBitmap(secondBmp, srcRectSecond, dstRectSecond, null);

        File file = Utility.createRandomPNGFile(reactContext);
        Utility.writeBMPToPNGFile(editBmp, file, promise);

        promise.resolve("file://" + file.toString());
    }

    @Override
    public String getName() {
        return "RNImageTools";
    }
}
