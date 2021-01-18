package com.example.moneytrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String PASSWORD_TB = "password";
    public static final String BANKS = "banks";
    public static final String CREATE_PASSWORD = "create table "+PASSWORD_TB+" (password varchar2(100))";
    public static final String CREATE_BANKS = "create table "+BANKS+" (bank_name varchar2(100) unique" +
            ", bank_sms varchar2(100) , bank_id varchar2(50), bank_num varchar2(20) unique)";

    public SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_PASSWORD);
        db.execSQL(CREATE_BANKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+PASSWORD_TB);
        db.execSQL("drop table if exists "+BANKS);
        onCreate(db);

    }
    public boolean addPassword(String password){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
        long r = db.insert(PASSWORD_TB, null, values);
        db.close();
        return r != -1;
    }
    public boolean addBank(String bName, String bNo, String sms){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bank_name", bName);
        values.put("bank_num",bNo);
        values.put("bank_sms",sms);
        long r = db.insert(BANKS, null, values);
        String cr = "create table "+bNo+
                "(amount varchar2(10),  date varchar2(25), date_sent varchar2(25))";
        try {
            db.execSQL(cr);
        }catch(Exception e){

        }
        return r == -1? false:true;
    }


    public void deletePassword(){
        db = this.getWritableDatabase();
        db.delete(PASSWORD_TB, null, null);
    }

    public boolean checkIfExist(){
        db = this.getWritableDatabase();
        Cursor r = db.rawQuery("select* from "+PASSWORD_TB, null);
        if(r.getCount() > 0){
            return true;
        }
        return false;
    }
    public String queryPassword(){
        db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select password from "+PASSWORD_TB, null);
        r.moveToFirst();
        return r.getString(0).toString();
    }
    public List<String> getAllBanks(String col){
        List<String> allBanks = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor r;
        try {
            r = db.rawQuery("select "+col+ " from " + BANKS, null);
            r.moveToFirst();
            allBanks.add(r.getString(0));
            while(r.moveToNext()){
                allBanks.add(r.getString(0));
            }
        }catch (Exception e){

        }
        return allBanks;
    }
    public Map<String, String> getMapping(String c1, String c2){
        Map<String, String> mapping = new HashMap<>();
        db = this.getReadableDatabase();
        Cursor r;

        try {
            r = db.rawQuery("select "+c1+","+c2+ " from " + BANKS, null);

            r.moveToFirst();
            mapping.put(r.getString(0), r.getString(1));
            while(r.moveToNext()){
                mapping.put(r.getString(0), r.getString(1));
            }
        }catch (Exception e){

        }
        return mapping;
    }

    public boolean deleteBTable(String name){
        db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select bank_num from '"+BANKS+"' where bank_name='"+name+"'"
        ,null);
        String num = r.moveToFirst()?r.getString(0):"";
        db.delete(BANKS, "bank_name = "+"'"+name+"'", null);
        try {
            db.delete(num, null, null);
        }catch(Exception e){

        }
        return !isBankExists(name);
    }
    public boolean isBankExists(String bName){
        db = getReadableDatabase();
        return db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + bName + "'", null).getCount()>0;
    }
    public boolean addDataToBank(String bNum, String amt, String date, String dateSent){
        db = this.getWritableDatabase();
        ContentValues values;
        long r = 0;

        Cursor cr = db.rawQuery("select * from "+bNum+" where date_sent = '"+dateSent+"'",null);
        if(cr.getCount()>0){
            return true;
        }
        values = new ContentValues();
        values.put("amount",amt );

        values.put("date",date);
        values.put("date_sent",dateSent);
        r = db.insert(bNum, null, values);
        return r != -1;
    }

    public List<String> amoutMapping(String bName, String date, int w){
        List<String> nameAmt = new ArrayList<>();
        db = getReadableDatabase();
        Cursor r;
        r = db.rawQuery("select bank_num from "+BANKS+" " +
                "where bank_name='"+bName+"'", null);
        String bTable = r.moveToFirst()?r.getString(0):"";
        if(bTable.isEmpty()) return nameAmt;
        String[] m = date.split("/");
        String m1 = m [0], m2 = m[2];
        if(isBankExists(bTable)){
             r = w==0? db.rawQuery("select amount from "+bTable+
                    " where date = '"+date+"'", null)
             :db.rawQuery("select amount from "+bTable+
                     " where date like '"+m1+"%"+m2+"'", null);
             if(r.moveToFirst()){
                 nameAmt.add(r.getString(0));
                 while(r.moveToNext()){
                     nameAmt.add(r.getString(0));
                 }
             }
        }

        return nameAmt;
    }

    public void deleteAllTables(){
        List<String> bTables = getAllBanks("bank_num");
        db = this.getWritableDatabase();
        for(String tName: bTables){
            db.delete(tName, null, null);
        }
        db.delete(BANKS, null, null);
    }

    public String queryBankCols(String getterCol, String comparerCol, String colVal){
        db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select "+getterCol+" " +
                "from '"+BANKS+"' where "+comparerCol+" = '"+colVal+
                "'", null);
        String nm= r.moveToFirst()?r.getString(0):"";
        return nm;
    }

}