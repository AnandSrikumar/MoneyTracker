package com.example.moneytrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{
    private EditText bankName;
    private EditText bankNum;
    private EditText pass;
    private EditText conf;
    private Button set;
    private ImageView goBack;
    private DBHelper helper;
    private String TAG = "ForgotPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        activityManager();
    }
    private void activityManager(){
        bankName = findViewById(R.id.bank_select);
        bankNum = findViewById(R.id.bank_num_f);
        pass = findViewById(R.id.new_pass);
        conf = findViewById(R.id.confirm);
        set = findViewById(R.id.set_password_f);
        goBack = findViewById(R.id.go_back_f);
        helper = new DBHelper(this);
        set.setOnClickListener(this);
        goBack.setOnClickListener(this);


    }

    private boolean validateBankNum(){
        String name = bankName.getText().toString().toLowerCase().trim();
        String num = bankNum.getText().toString().toLowerCase().trim();
        if(helper.isBankExists(num)){
            Log.d(TAG, "the query->"+"select '"+"bank_name"+"' " +
                    "from '"+"BANKS"+"' where '"+"bank_num"+"' = '"+num+
                    "'");
            String res = helper.queryBankCols("bank_name", "bank_num", num);
            Log.d(TAG, res+" is the result");
            if(res.equalsIgnoreCase(name)){
                Log.d(TAG, "Bank validated");
                return true;
            }
        }

        return false;
    }
    private boolean validatePassword(){
        PasswordValidation pw = new PasswordValidation(helper);
        helper.deletePassword();
        String p = pass.getText().toString();
        String c = conf.getText().toString();
        String res = pw.addPassword(p, c);
        Toast.makeText(this, res, Toast.LENGTH_LONG).show();
        if(res.contains("successfully")){
            pass.setText("");pass.invalidate();
            conf.setText("");conf.invalidate();
            bankName.setText("");bankName.invalidate();
            bankNum.setText("");bankNum.invalidate();
            Log.d(TAG, "Password validated succesfully");
            return true;
        }

        return false;
    }


    @Override
    public void onClick(View view) {
        if(set.getId() == view.getId()){
            if(!(validateBankNum() && validatePassword())){
                Toast.makeText(this, "Bank name and number don't" +
                                " match or passwords don't match or password is not strong enough"
                        ,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "password changed successfully"
                        ,Toast.LENGTH_LONG).show();
            }
        }

        if(goBack.getId() == view.getId()){
            Intent in = new Intent(this, MainActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();
        }
    }
}