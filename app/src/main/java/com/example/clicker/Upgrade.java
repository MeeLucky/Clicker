package com.example.clicker;

import androidx.annotation.NonNull;

public class Upgrade {
    //коэффицент увелечения цены
    private double Multiplier = 1.07;
    //Todo: обычные типы не подходят нужны биг десимал может и биг инт
    private String name;
    private int lvl;
    private double speed;
    private int basePrice;

    @NonNull
    @Override
    public String toString() {
        return "\n" +
                "name: " + this.name + "\n" +
                "speed: " + this.speed + "\n" +
                "price: " + this.basePrice + "\n" +
                "lvl: " + this.lvl;
    }

    Upgrade(String name, int price, double speed, int lvl) {
        this.name = name;
        this.speed = speed;
        this.basePrice = price;
        this.lvl = lvl;
    }

    public Upgrade (String name, int price, double speed, int lvl, double multiplier) {
        this.name = name;
        this.speed = speed;
        this.basePrice = price;
        this.lvl = lvl;
        this.Multiplier = multiplier;
    }

    public String getName() { return this.name; }
    int getLVL() { return this.lvl; }
//    public double getSpeed() { return this.speed; }
    int getBasePrice() { return this.basePrice; }

    double getTotalSpeed() {
        if(lvl == 0)
            return speed;
        else
            return MainActivity.roundAvoid(speed * lvl, 2);
    }

    int getTotalPrice() {
        if(lvl == 0)
            return basePrice;
        else
            return (int)Math.round(basePrice * Math.pow(Multiplier, lvl));
    }
}
