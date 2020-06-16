package com.group10.fuzzyphotos.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileHelperTest {

    @Test
    public void testCreateTemporaryFile() {
        Context test_context = InstrumentationRegistry.getInstrumentation().getTargetContext(); // context

        File test_file = null; // set to null
        try {
            test_file = FileHelper.createTemporaryFile(test_context);
        } catch (IOException e) {
            fail("Creating file failed");
            e.printStackTrace();
        }
        assertNotNull(test_file); // Check if some file was created
    }

    @Test
    public void testGetFileExtension() {
        Context test_context = InstrumentationRegistry.getInstrumentation().getTargetContext(); // context

        String picName1 = "memeface.png";
        String PATH1 = Environment.getExternalStorageDirectory().getPath()+ picName1;
        File f1 = new File(PATH1);
        Uri test_uri1 = Uri.fromFile(f1); // uri 1

        String extension1 = FileHelper.GetFileExtension(test_uri1, test_context);

        String picName2 = "rounded_button.xml";
        String PATH2 = Environment.getExternalStorageDirectory().getPath()+ picName2;
        File f2 = new File(PATH2);
        Uri test_uri2 = Uri.fromFile(f2); // uri 2

        String extension2 = FileHelper.GetFileExtension(test_uri2, test_context);

        assertEquals("png", extension1);
        assertEquals("xml", extension2);
    }
}