package com.peepal.shifoo;

/**
 * Created by User on 17-07-2015.
 */
public class Student
{
        private int _id;
        private String _name;
        private String _phone;
        private int _amount;
        private String _date;
        public Student(){}

        public Student(int id, String name, String phone,int amt,String date) {
        this._id = id;
        this._name=name;
        this._phone=phone;
        this._amount=amt;
        this._date=date;
    }

        public Student(String name, String phone,int amt,String date) {
        this._name=name;
        this._phone=phone;
        this._amount=amt;
        this._date=date;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() { return this._id; }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() { return this._name; }

    public void setPhone(String phone) { this._phone = phone; }

    public String getPhone() {
        return this._phone;
    }

    public void setAmount(int amt) {
        this._amount = amt;
    }

    public int getAmount() {
        return this._amount;
    }

    public void setDate(String date) {
        this._date = date;
    }

    public String getDate() { return this._date; }
}
