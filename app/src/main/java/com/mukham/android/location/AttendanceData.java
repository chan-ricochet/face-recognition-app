package com.mukham.android.location;

public class AttendanceData {

    private String attId;
    private String attName;
    private String attTime;

    public AttendanceData(){
    }

    public void setAttId(String id) {
        this.attId = id;
    }

    public void setAttName(String name) { this.attName = name; }
    public void setAttTime(String time) {
        this.attTime = time;
    }

    public AttendanceData(String attId, String attName, String attTime) {
        this.attId = attId;
        this.attName = attName;
        this.attTime = attTime;
    }

    public String getAttId() {
        return attId;
    }

    public String getAttName() {
        return attName;
    }

    public String getAttTime() {
        return attTime;
    }



}

