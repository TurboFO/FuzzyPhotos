package com.group10.fuzzyphotos.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.IOException;

public final class BlurBuilder {

    static final int BLUR_MODE_MAX = 5; // Max blur mode possible

    private BlurBuilder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Blurs URI instead of Bitmap
     */
    public static Bitmap blur(Context context, Uri uri, int blur_mode) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        return BlurBuilder.blur(context, bitmap, blur_mode);
    }

    /**
     * Takes bitmap image and blurs it with set 'blur_radius' radius
     * @return Bitmap with blur effect applied on it
     */
    public static Bitmap blur(Context context, Bitmap image, int blur_mode) {
        // Violation: attempting to blur image with mode higher than max mode
        if (blur_mode > BLUR_MODE_MAX) {
            return image; // Don't change anything to bitmap
        }

        BlurEntry entry = getBlurInfo(blur_mode); // Retrieve radius and scale

        // Change image resolution for increased blur effect
        int width = Math.round(image.getWidth() * entry.getBlurScale());
        int height = Math.round(image.getHeight() * entry.getBlurScale());

        // Apply scaling to original bitmap
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // Use renderscript for radius blur image
        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        intrinsicBlur.setRadius(entry.getBlurRadius());
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);

        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    /**
     * Retrieves a blur entry for a given 'blur_mode'. Blur entry contains information needed to blur
     * an image (scale, radius)
     */
    private static BlurEntry getBlurInfo(int blur_mode) {
        BlurEntry entry = null;
        switch (blur_mode) {
            case 1:
                entry = new BlurEntry(0.1f, 20); // Max blur
                break;
            case 2:
                entry = new BlurEntry(0.1f, 10);
                break;
            case 3:
                entry = new BlurEntry(0.3f, 15);
                break;
            case 4:
                entry = new BlurEntry(0.5f, 10);
                break;
            case 5:
                entry = new BlurEntry(1.0f, 1); // No blur
                break;
        }
        return entry;
    }
}
