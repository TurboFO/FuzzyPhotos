package com.group10.fuzzyphotos.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.test.platform.app.InstrumentationRegistry;

import com.group10.fuzzyphotos.R;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class BlurBuilderTest {

    @Test
    public void testBlur() {
        Context test_context = InstrumentationRegistry.getInstrumentation().getTargetContext(); // context

        Bitmap bitmap_normal = BitmapFactory.decodeResource(test_context.getResources(), R.drawable.memeface);
        Bitmap bitmap_blurred_1 = BlurBuilder.blur(test_context, bitmap_normal, 1);
        Bitmap bitmap_blurred_2 = BlurBuilder.blur(test_context, bitmap_normal, 2);
        Bitmap bitmap_blurred_3 = BlurBuilder.blur(test_context, bitmap_normal, 3);
        Bitmap bitmap_blurred_4 = BlurBuilder.blur(test_context, bitmap_normal, 4);
        Bitmap bitmap_blurred_5 = BlurBuilder.blur(test_context, bitmap_normal, 5);

        Boolean normal_same_as_1 = bitmap_normal.sameAs(bitmap_blurred_1);
        Boolean normal_same_as_2 = bitmap_normal.sameAs(bitmap_blurred_2);
        Boolean normal_same_as_3 = bitmap_normal.sameAs(bitmap_blurred_3);
        Boolean normal_same_as_4 = bitmap_normal.sameAs(bitmap_blurred_4);
        Boolean normal_same_as_5 = bitmap_normal.sameAs(bitmap_blurred_5);

        assertFalse(normal_same_as_1); // Should not be equal since on one image a  blurred effect is applied
        assertFalse(normal_same_as_2); // Different blur effect
        assertFalse(normal_same_as_3); // Different blur effect
        assertFalse(normal_same_as_4); // Different blur effect
        assertTrue(normal_same_as_5); // Both have no blur effect applied
    }
}