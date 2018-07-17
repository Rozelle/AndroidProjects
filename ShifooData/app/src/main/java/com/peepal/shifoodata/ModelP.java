package com.peepal.shifoodata;

/**
 * Created by User on 30-07-2015.
 */
public class ModelP {
    private String name;
    private String phone;
    private int amt;
    private String date;
    private String remarks;
    private String txnid;
    private boolean selected;

    public ModelP(String name,String phone,int amt,String date,String remarks,String txnid) {
        this.name = name;
        this.phone=phone;
        this.amt=amt;
        this.date=date;
        this.remarks=remarks;
        this.txnid=txnid;
        selected = false;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }

    public int getAmount() { return amt; }

    public void setAmount(int amt) { this.amt = amt; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }

    public void setRemarks(String remarks) {
        this.remarks=remarks;
    }

    public String getRemarks() { return remarks; }

    public void setTxnid(String txnid) {
        this.txnid=txnid;
    }

    public String getTxnid() { return txnid; }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

