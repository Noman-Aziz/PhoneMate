package com.dextrotechnologies.phonemate;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileMan {
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

    public static JSONObject downloadFile(String path) {
        if (path == null)
            return null;

        File file = new File(path);

        if (file.exists()) {

            int size = (int) file.length();
            byte[] data = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(data, 0, data.length);
                JSONObject object = new JSONObject();
                object.put("type", "download");
                object.put("name", file.getName());
                object.put("buffer", data);
                buf.close();

                return object;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
