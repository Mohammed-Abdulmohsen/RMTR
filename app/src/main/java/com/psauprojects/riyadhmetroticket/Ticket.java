package com.psauprojects.riyadhmetroticket;

public class Ticket {
    private int ticketid;
    private int passid;
    private byte journy_type;
    private byte train_type;
    private String dep_station;
    private String arr_station;
    private String dep_time;
    private String dep_date;
    private String ret_date;
    private String class_type;
    private byte adult;
    private byte child;
    private byte infant;
    private String seats;

    public Ticket() {
    }

    public Ticket(int ticketid, int passid, byte journy_type, byte train_type, String dep_station, String arr_station, String dep_time, String dep_date, String ret_date, String class_type, byte adult, byte child, byte infant, String seats) {
        this.ticketid = ticketid;
        this.passid = passid;
        this.journy_type = journy_type;
        this.train_type = train_type;
        this.dep_station = dep_station;
        this.arr_station = arr_station;
        this.dep_time = dep_time;
        this.dep_date = dep_date;
        this.ret_date = ret_date;
        this.class_type = class_type;
        this.adult = adult;
        this.child = child;
        this.infant = infant;
        this.seats = seats;
    }

    public int getTicketid() {
        return ticketid;
    }

    public void setTicketid(int ticketid) {
        this.ticketid = ticketid;
    }

    public int getPassid() {
        return passid;
    }

    public void setPassid(int passid) {
        this.passid = passid;
    }

    public byte getJournyType() {
        return journy_type;
    }

    public void setJournyType(byte journy_type) {
        this.journy_type = journy_type;
    }

    public byte getTrainType() {
        return train_type;
    }

    public void setTrainType(byte train_type) {
        this.train_type = train_type;
    }

    public String getDep_station() {
        return dep_station;
    }

    public void setDep_station(String dep_station) {
        this.dep_station = dep_station;
    }

    public String getArr_station() {
        return arr_station;
    }

    public void setArr_station(String arr_station) {
        this.arr_station = arr_station;
    }

    public String getDep_time() {
        return dep_time;
    }

    public void setDep_time(String dep_time) {
        this.dep_time = dep_time;
    }

    public String getDep_date() {
        return dep_date;
    }

    public void setDep_date(String dep_date) {
        this.dep_date = dep_date;
    }

    public String getRet_date() {
        return ret_date;
    }

    public void setRet_date(String ret_date) {
        this.ret_date = ret_date;
    }

    public String getClass_type() {
        return class_type;
    }

    public void setClass_type(String class_type) {
        this.class_type = class_type;
    }

    public byte getAdult() {
        return adult;
    }

    public void setAdult(byte adult) {
        this.adult = adult;
    }

    public byte getChild() {
        return child;
    }

    public void setChild(byte child) {
        this.child = child;
    }

    public byte getInfant() {
        return infant;
    }

    public void setInfant(byte infant) {
        this.infant = infant;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
}
