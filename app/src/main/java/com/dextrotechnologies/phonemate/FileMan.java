package com.dextrotechnologies.phonemate;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileMan {

    private static final int BUFFER_SIZE = 4096; // 4KB

    public static JSONArray walk(String pathReq) {

        JSONArray values = new JSONArray();


        // Read all files sorted into the values-array
        String path = Environment.getExternalStorageDirectory().toString() + pathReq;


        File directory = new File(path);
        File[] files = directory.listFiles();

        for (File file : files) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", file.getName());
                obj.put("path", file.getAbsolutePath());
                values.put(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return values;
    }

    public static String downloadFile(String pathReq) {
        if (pathReq == null)
            return "Path Not Given";

        String path = Environment.getExternalStorageDirectory().toString() + pathReq;

        File file = new File(path);
        JSONObject object = new JSONObject();

        if (file.exists()) {

            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            long fileSize = new File(path).length();
            byte[] allBytes = new byte[(int) fileSize];

            int bytesRead = 0;
            try {
                bytesRead = inputStream.read(allBytes);
            } catch (IOException e) {
                Log.e("FILEMSG", path + e.getMessage() );
            }

            return Base64.encodeToString(allBytes, Base64.DEFAULT);

        } else{
            Log.e("FILEMSG", "GOUCHI NOT" + path );
        }

        return "File Not Found";
    }
}
