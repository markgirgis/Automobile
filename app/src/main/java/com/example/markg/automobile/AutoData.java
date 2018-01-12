package com.example.markg.automobile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by markg on 2017-12-23.
 */

public class AutoData {
    private double price;
    private double kilos;
    private double liters;
    private String date;
    private int year;
    private int month;

    AutoData(){
        this(0.0,0.0,0.0);
    }

    AutoData(double liters, double kilos, double price ){
        setLiters(liters);
        setKilos(kilos);
        setPrice(price);
        setDate();
    }

    AutoData(String s){
        String[] ss = s.split(" ");
        setLiters(Double.parseDouble(ss[0]));
        setKilos(Double.parseDouble(ss[2]));
        setPrice(Double.parseDouble(ss[4]));

        String[] sss = s.split("\n");
        setDate(sss[1]);
    }

    public void setLiters(double x){
        liters = x;
    }

    public double getLiters(){
        return liters;
    }

    public void setKilos(double x){
        kilos = x;
    }

    public double getKilos() {
        return kilos;
    }

    public void setPrice(double x){
        price = x;
    }

    public double getPrice() {
        return price;
    }

    public void setDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        date = dateFormat.format(cal.getTime());
        setYear(date);
        setMonth(date);
    }


    public void setDate(String date) {
        this.date = date;
        setYear(date);
        setMonth(date);
    }

    public String getDate() {
        return date;
    }

    public void setYear(String date){
        String[] splitDate = date.split("/");
        setYear(Integer.parseInt(splitDate[0]));
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(String date){
        String[] splitDate = date.split("/");
        setMonth(Integer.parseInt(splitDate[1]));
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }


    @Override
    public String toString() {
        return (liters +" liters " + kilos+" KM. " + price +" $ \n" + getDate());
    }


}
