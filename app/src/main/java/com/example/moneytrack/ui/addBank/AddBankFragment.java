package com.example.moneytrack.ui.addBank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moneytrack.R;
import com.example.moneytrack.DBHelper;
import android.util.Log;
import android.widget.Toast;

public class AddBankFragment extends Fragment {

    private AddBankViewModel addBankViewModel;
    View root;
    private String TAG="AddBankFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addBankViewModel =
                ViewModelProviders.of(this).get(AddBankViewModel.class);
        root = inflater.inflate(R.layout.fragment_addbank, container, false);
        activityManager();
        return root;
    }
    public void activityManager(){
        final EditText sms = root.findViewById(R.id.bank_sms);
        final EditText bankName = root.findViewById(R.id.bank_name);
        final EditText bankNum = root.findViewById(R.id.bank_num);
        final DBHelper helper = new DBHelper(getContext().getApplicationContext());
        Log.d(TAG, "database object created");
        Button add = root.findViewById(R.id.add_bank);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sms != null && bankName != null && bankNum != null){
                    String smsStr = sms.getText().toString().toLowerCase();
                    String bankNameStr= bankName.getText().toString().toLowerCase();
                    String bankNumStr = bankNum.getText().toString().toLowerCase();
                    Log.d(TAG, "Strings extracted");
                    if(!helper.addBank(bankNameStr, bankNumStr, smsStr)){
                        Toast.makeText(getContext(), "FAILED TO ADD BANK", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "failed to add to database");
                    }
                    else{
                        Toast.makeText(getContext(), "Successfully added to database", Toast.LENGTH_LONG).show();
                        bankName.setText("");bankNum.setText("");sms.setText("");
                        bankName.invalidate();
                        bankNum.invalidate();
                        sms.invalidate();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Please fill the fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}