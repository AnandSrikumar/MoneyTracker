package com.example.moneytrack;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class GetStarted extends AppCompatActivity implements View.OnClickListener {
    private Button set;
    private EditText pass;
    private EditText confirm;
    private ImageView back;
    private PasswordValidation pwd;
    private DBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started);
        activityManager();
    }
    private void activityManager(){
        helper = new DBHelper(this);
        pwd = new PasswordValidation(helper);
        set = findViewById(R.id.go);
        back = findViewById(R.id.go_back);
        set.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == set.getId()){
            pass = findViewById(R.id.password_enter);
            confirm = findViewById(R.id.confirm);
            if(pass == null || confirm == null){
                Toast.makeText(this, "Field can't be empty", Toast.LENGTH_LONG).show();
            }
            else {
                String p = pass.getText().toString();
                String c = confirm.getText().toString();
                helper.deletePassword();
                String res = pwd.addPassword(p, c);
                Toast.makeText(this, res, Toast.LENGTH_LONG).show();
                if(res.contains("successfully")){
                    helper.deleteAllTables();
                }
                pass.setText("");
                confirm.setText("");
                pass.invalidate();
                confirm.invalidate();
            }
        }
        if(id == back.getId()){
            if(pass != null){
                pass.setText("");
                pass.invalidate();
            }
            if(confirm != null){
                confirm.setText("");
                confirm.invalidate();
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}