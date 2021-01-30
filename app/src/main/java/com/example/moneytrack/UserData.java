package com.example.moneytrack;

public class UserData {
    String editTextId;
    String bankNumID;
    int buttonId;

    public UserData(String editTextId, String bankNumID, int buttonId){
        this.editTextId = editTextId;
        this.bankNumID = bankNumID;
        this.buttonId = buttonId;
    }
    public int getButtonId(){
        return buttonId;
    }
    public String getBankNumID(){
        return bankNumID;
    }
    public String getEditTextId(){
        return editTextId;
    }
}
