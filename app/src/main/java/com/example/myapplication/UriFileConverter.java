package com.example.myapplication;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UriFileConverter {
    // Convert single Uri to a temp File by copying its content
    public static File uriToFile(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) return null;

        // Create a temp file in cache directory
        String fileName = "temp_image_" + System.currentTimeMillis() + ".jpg"; // or extract extension
        File tempFile = new File(context.getCacheDir(), fileName);

        FileOutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[4096];
        int length;

        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return tempFile;
    }

    // Convert list of Uris to list of Files
    public static List<File> urisToFiles(Context context, List<Uri> uris) {
        List<File> files = new ArrayList<>();
        for (Uri uri : uris) {
            try {
                File file = uriToFile(context, uri);
                if (file != null) {
                    files.add(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // You can handle errors here if needed
            }
        }
        return files;
    }
}
