package com.example.user.ble.app;

/**
 * Created by User on 16-06-2015.
 */
public class Device {
        private int _id;
        private String _devName;
        private int _rssi;

        public Device() {

        }

        public Device(String devName, int rssi) {
            this._devName = devName;
            this._rssi = rssi;
        }

        public void setID(int id) {
            this._id = id;
        }

        public int getID() {
            return this._id;
        }

        public void setDeviceName(String devName) {
            this._devName =devName;
        }

        public String getDeviceName() {
            return this._devName;
        }

        public void setRssi(int rssi) {
            this._rssi = rssi;
        }

        public int getRssi() {
            return this._rssi;
        }
}

