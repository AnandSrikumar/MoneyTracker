package com.example.moneytrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PermissionInfoCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.security.Permission;
import java.security.PermissionCollection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button go;
    private Button getStarted;
    private TextView forgotPassword;
    private EditText password;
    private PasswordValidation validation;
    private final String TAG ="MainActivity";
    DBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "In oncreate method");
        checkPermission(Manifest.permission.READ_SMS, 100);
        activityManager();
    }

    private void activityManager(){
        helper = new DBHelper(this);
        go = findViewById(R.id.go);
        getStarted = findViewById(R.id.get_started);
        forgotPassword= findViewById(R.id.forgot_password);
        go.setOnClickListener(this);
        getStarted.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        validation = new PasswordValidation(helper);
        Log.d(TAG, "Initiated all objects");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == go.getId()){
            try {
                password = findViewById(R.id.password_enter);
                String status = validation.validatePassword(password.getText().toString());
                Log.d(TAG, "Got password status " + status);
                Log.d(TAG, "Validating");
                if (!status.isEmpty()) {
                    Log.d(TAG, "Password status....");
                    Toast.makeText(this, status, Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(this, details_page.class);
                    startActivity(intent);
                }
            }catch (Exception e){
                Log.e(TAG, "Error occured");
                Log.e(TAG,e.getMessage());
                Toast.makeText(this,
                        "COuldn't validate password", Toast.LENGTH_LONG)
                        .show();
            }
        }
        if(id == getStarted.getId()){
            if(password != null) {
                password.setText("");
                password.invalidate();
            }
            Intent intent = new Intent(this, GetStarted.class);
            startActivity(intent);
            finish();
        }
        if(id == forgotPassword.getId()){
            Intent in = new Intent(this, ForgotPassword.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();
        }
    }
    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            MainActivity.this,
                            new String[] { permission },
                            requestCode);
        }
        else {

        }
    }
}