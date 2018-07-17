package com.peepal.shifoo;

/**
 * Created by User on 01-07-2015.
 */
public class Message
{
    private int _id;
    private String _name;
    private String _phone;
    private int _amount;
    private String _remarks;
    private String _date;
    private int _paid;
    private String _txnid;
    //Empty default constructor;
    public Message(){}

    public Message(int id, String name, String phone,int amt,String remarks,String date,int paid,String txnid) {
        this._id = id;
        this._name=name;
        this._phone=phone;
        this._amount=amt;
        this._remarks=remarks;
        this._date=date;
        this._paid=paid;
        this._txnid=txnid;
    }

    public Message(String name, String phone,int amt,String remarks,String date,int paid,String txnid) {
        this._name=name;
        this._phone=phone;
        this._amount=amt;
        this._remarks=remarks;
        this._date=date;
        this._paid=paid;
        this._txnid=txnid;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    public void setPhone(String phone) {
        this._phone = phone;
    }

    public String getPhone() {
        return this._phone;
    }

    public void setAmount(int amt) {
        this._amount = amt;
    }

    public int getAmount() {
        return this._amount;
    }

    public void setRemarks(String remarks) {
        this._remarks = remarks;
    }

    public String getRemarks() {
        return this._remarks;
    }

    public void setDate(String date) {
        this._date = date;
    }

    public String getDate() {
        return this._date;
    }

    public void setPaid(int paid) {
        this._paid = paid;
    }

    public int getPaid() {
        return this._paid;
    }

    public void setTxnid(String txnid) {
        this._txnid = txnid;
    }

    public String getTxnid() {return this._txnid; }

}

