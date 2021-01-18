package com.example.moneytrack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidation {

    private DBHelper helper;

    public PasswordValidation(DBHelper helper){
        this.helper = helper;
    }

    public String validatePassword(String password){
        if(!helper.checkIfExist()){
            return "No password! Please set password by clicking GET STARTED";
        }
        if(!helper.queryPassword().equals(password)){
            return "Incorrect password";
        }

        return "";
    }
    public String addPassword(String password, String confirm){
        if(!password.equals(confirm)){
            return "Passwords mismatch! Try again";
        }
        if(!regexMatcher(password)){
            return "Password should have 1 caps, 1 small, 1 digit, 1 special char and at least 8 characters";
        }

        return helper.addPassword(password)?"Password set successfully": "Password was not set!";
    }
    public boolean regexMatcher(String password){
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return password==null?false:m.matches();
    }
}
