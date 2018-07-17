package com.peepal.shifoodata;

/**
 * Created by User on 29-07-2015.
 */
public class Model{
    private String name;
    private String phone;
    private int amt;
    private String date;
    private boolean selected;

    public Model(String name,String phone,int amt,String date) {
        this.name = name;
        this.phone=phone;
        this.amt=amt;
        this.date=date;
        selected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }

    public int getAmount() {
        return amt;
    }

    public void setAmount(int amt) {
        this.amt = amt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
