package com.example.moneytrack.ui.removeBank;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moneytrack.DBHelper;
import com.example.moneytrack.R;


import java.util.ArrayList;
import java.util.List;
import android.util.Log;
public class RemoveBankFragment extends Fragment{

    private RemoveBankViewModel removeBankViewModel;

    private View root;
    LinearLayout layout;
    DBHelper helper;
    List<String> allChecked;
    String TAG = "RemoveBankFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        removeBankViewModel =
                ViewModelProviders.of(this).get(RemoveBankViewModel.class);
        root = inflater.inflate(R.layout.fragment_removebank, container, false);
        activityManager();
        return root;
    }
    private void activityManager(){
        Button rem = root.findViewById(R.id.remove);
        Button sel = root.findViewById(R.id.select_all);
        layout = root.findViewById(R.id.checkbox_id);
        helper = new DBHelper(getContext().getApplicationContext());
        allChecked = new ArrayList<>();

        addingCheckboxes();
        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(String removable: allChecked){
                    Log.d(TAG, removable+" is the name of the bank");
                    String msg = helper.deleteBTable(removable)?removable+" removed":"Not removed";
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, "Refreshing");
                Fragment currentFragment = getActivity().getSupportFragmentManager().
                        findFragmentById(R.id.nav_host_fragment);
                currentFragment = currentFragment.getChildFragmentManager().getFragments().get(0);
                Log.d(TAG, "got the fragment "+currentFragment.getTag());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
                Log.d(TAG, "refresh completed");
            }
        });
        sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void addingCheckboxes(){
        List<String> allBanks = helper.getAllBanks("bank_name");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        for(final String bank: allBanks){
            CheckBox b = new CheckBox(getContext());
            b.setText(bank);
            b.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dark, null));
            b.setPadding(2,4, 0, 2);
            b.setLayoutParams(params);
            b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) allChecked.add(bank);
                    else if(allChecked.contains(bank)) allChecked.remove(bank);
                }
            });
            layout.addView(b);
        }

    }

}