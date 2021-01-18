package com.example.moneytrack.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moneytrack.R;
import com.example.moneytrack.DBHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.moneytrack.SMSManager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private String TAG="HomeFragment";
    private HomeViewModel homeViewModel;
    private TextView today_t;
    private TextView month_t;
    private TextView currency;
    private ScrollView banks;
    private DBHelper helper;
    private List<String> allBanks;
    private View root;
    private float tamt =0.0f, mamt =0.0f;
    private LinearLayout ll;
    private Map<Integer,String> clicked = new HashMap<>();
    private Spinner dropDown;
    private String selectedBank;
    SMSManager manager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        activityManager();
        return root;
    }
    public void activityManager(){
        try {
            dropDown = root.findViewById(R.id.drop_down_bank);
            currency = root.findViewById(R.id.currency);
            currency.setText("Spending displayed in rupees");
            helper = new DBHelper(getContext().getApplicationContext());
            Log.d(TAG, "Successfully created DB object");

            manager = new SMSManager(getContext().getApplicationContext());
            manager.readTodaySms(getActivity().getContentResolver());
            addingTextViews();
            displayTotal();
            ImageButton day_c =  root.findViewById(R.id.check_a_day_button);
            ImageButton mon_c = root.findViewById(R.id.check_a_month_button);
            day_c.setOnClickListener(this);
            mon_c.setOnClickListener(this);
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
            Log.d(TAG,"Exception occured");
        }
    }

    private void addingTextViews() {
        allBanks = helper.getAllBanks("bank_name");
        Log.d(TAG, "got all banks "+allBanks);
        ll = root.findViewById(R.id.scroll_lv);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        Log.d(TAG, "Added layout parameters");
        if(allBanks.isEmpty()){
            TextView view = new TextView(getContext());
            view.setText("NO BANKS AVAILABLE AT THE MOMENT");
            view.setTextSize(18);
            view.setLayoutParams(params);
            ll.addView(view);
            Log.d(TAG, "Added a view");
        }
        else{

            for(String name:allBanks){
                makingLinearLayoutTop(name);

            }
        }
        allBanks.add("---All banks---");
        Log.d(TAG, "came out of the for loop  -->" );
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, allBanks);
        Log.d(TAG, "Array adapter created");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Log.d(TAG, "Array Adapter propery set");
        dropDown.setAdapter(adapter);
        Log.d(TAG, "added the spinner");
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBank = adapter.getItem(i).toString();
                Log.d(TAG," inside item click method "+ selectedBank);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void makingLinearLayoutTop(String bName){
        String num = helper.queryBankCols("bank_num", "bank_name",bName);
        LinearLayout main = new LinearLayout(getContext());
        //main.setBackground(ResourcesCompat.getDrawable(
         //       getResources(), R.drawable.full_border, null
        //));
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(5,5,5,5);
        LinearLayout top = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 5f);
        top.setLayoutParams(params);

        top.setBackground(ResourcesCompat.
               getDrawable(getResources(), R.drawable.ll_background,null));
        ImageView image = new ImageView(getContext());
        image.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        image.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        image.setImageResource(R.drawable.edit_symbol);
        top.addView(getView(bName, 2.25f));
        top.addView(getView(num, 2.25f));
        top.addView(image);
        main.addView(top);
        main.addView(verticles(bName));

        main.addView(getView(" ",1f));
        ll.addView(main);
    }
    private LinearLayout verticles(String bankName){
        LinearLayout main = new LinearLayout(getContext());
        main.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,2f));
        main.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.ll_background, null
        ));
        main.addView(getDets("TODAY", 0,1f, bankName));
        main.addView(getDets("MONTH", 1,1f, bankName));

        return main;
    }
    private LinearLayout getDets(String val1, int which, float weight, String bankName){
        LinearLayout dets = new LinearLayout(getContext());
        dets.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, weight
        ));
        dets.setOrientation(LinearLayout.VERTICAL);
        String amt = getTodayAmout(bankName, which, manager.getTodayDate());

        dets.addView(getView(val1, -1));
        dets.addView(getView(amt, -1));
        return dets;
    }

    private TextView getView(String value, float weight){
        TextView view1 = new TextView(getContext());
        if(weight != -1) {
            view1.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, weight));

        }
        else{
            view1.setLayoutParams(new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        view1.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dark, null));
        view1.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        view1.setText(value);
        return view1;
    }


    public String getTodayAmout(String bName, int wh, String date){
        String amt = "";

        Log.d(TAG, "inside amount method");
        Log.d(TAG, "todays date as per code: "+date);

        List<String> bankAm = helper.amoutMapping(bName, date, wh);
        float total = 0.0f;
        Log.d(TAG, "total sms for "+bName+" "+bankAm.size());
        for(String a: bankAm){
            try{
                total += Float.parseFloat(a);
            }catch (NumberFormatException ne){
                a = a.replaceAll("[a-zA-Z]","").trim();
                total += Float.parseFloat(a);
                Log.d(TAG, a+" is not a number");
            }
        }
        if(wh == 0) tamt += total;
        else mamt += total;

        amt = total+"";
        return amt;
    }

    private void displayTotal(){
        TextView tt = root.findViewById(R.id.today_total);
        TextView mt = root.findViewById(R.id.month_total);
        tt.setText(String.valueOf(tamt));
        mt.setText(String.valueOf(mamt));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.check_a_day_button){
            querying("",0);
        }
        if(view.getId() == R.id.check_a_month_button){
            querying("", 1);
        }
    }

    private void querying(String dateFormat, int which){
        TextView view;
        if(which == 0){
            view = root.findViewById(R.id.display_day);
        }
        else{
            view = root.findViewById(R.id.display_month);
        }
        EditText info;
        String dateF = "";
        if(which == 0){
            info = root.findViewById(R.id.check_a_day);
        }
        else{
            info = root.findViewById(R.id.check_a_month);
        }
        dateF = info.getText().toString();
        if(!validateDate(dateF, which)){
            Toast.makeText(getContext(),
                    "Date format for day MM/DD/YYYY, for month MM/YYYY",
                    Toast.LENGTH_LONG).show();
            return;
        }
        String mf[] = dateF.split("/");
        String amt = which == 0?getTodayAmout(selectedBank, which, dateF):
                getTodayAmout(selectedBank, 1,mf[0]+"/01/"+mf[1]);
        view.setText(amt);
        view.invalidate();
    }
    private boolean validateDate(String date, int wh){
        String[] parts = date.split("/");
        int m=0, d=0;
        if(wh == 0){
            if(parts.length < 3) return false;
            if(!(parts[0].length() == 2 && parts[1].length()==2 && parts[2].length()==4))
                return false;
            d = Integer.parseInt(parts[1].trim());
            m = Integer.parseInt(parts[0].trim());
            if( d> 31 || m > 12) return false;
        }
        else{
            if(parts.length < 2) return false;
            m = Integer.parseInt(parts[0].trim());
            if(m > 12) return false;
        }

        return true;
    }
}