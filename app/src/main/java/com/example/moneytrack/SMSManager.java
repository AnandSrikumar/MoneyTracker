package com.example.moneytrack;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.util.Log;
import android.widget.Toast;

public class SMSManager extends AppCompatActivity {
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String TAG="SMSManager";
    private DBHelper helper;

    private Context con;

    public SMSManager(Context con){
        this.con = con;
        helper = new DBHelper(con);
    }

    public String getTodayDate(){
        String date = "";
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        return date;
    }
    public void readTodaySms(ContentResolver resolver){
        List<String> msg = new ArrayList<>();
        String date = getTodayDate();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy'T'hh:mm:ss");
        String selectedDate = date+"T"+"00:00:00";
        Log.d(TAG, "Inside read sms method");
        Date dateStart = null;
        try {
            dateStart = formatter.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();

        }

        String filter = "date>=" + dateStart.getTime();
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        Cursor cursor = resolver.query(SMS_INBOX, null, filter, null, null);
        Log.d(TAG, "got content resolver");
        if(cursor.moveToFirst()){
            Map<String, String> mapping = helper.getMapping("bank_num","bank_sms");
            Log.d(TAG, " got some banks "+mapping.size()+" ");
            do{
                String body = cursor.getString(cursor.getColumnIndex("body"));
                Log.d(TAG, body);
                for(String num: mapping.keySet()){
                    if((body.toLowerCase().contains("debited") ||
                            body.toLowerCase().contains("transaction of")) &&
                    body.toLowerCase().contains(num.toLowerCase())) {
                        char left = body.charAt(body.toLowerCase().
                                indexOf(num.toLowerCase()) -1);
                        char right = body.charAt(body.toLowerCase().
                                indexOf(num.toLowerCase()) +num.length());
                        if(left != ' ' || right != ' '){
                            continue;
                        }
                        Log.d(TAG, "found one bank message " + num);

                        try{
                            String ref = mapping.get(num);
                            String date_r = cursor.getString(cursor.getColumnIndex("date"));
                            String date_s = cursor.getString(cursor.getColumnIndex("date_sent"));
                            String amt = body.substring(ref.indexOf("$"),
                                    ref.indexOf(" ", ref.indexOf("$")+1)).trim();
                            Log.d(TAG, "the details: " + amt + " " + date_r + " " + date_s);
                        if (!helper.addDataToBank(num.toLowerCase(), amt, date, date_s)) {
                            Toast.makeText(con, "Some error occured", Toast.LENGTH_LONG).show();
                        }
                    }catch(Exception e){
                            Log.d(TAG, "Exception occured");
                            Log.e(TAG, "HERE->"+e.getMessage());
                        }

                    }
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
}
