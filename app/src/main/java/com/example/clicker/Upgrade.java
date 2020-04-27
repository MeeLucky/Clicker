package com.example.clicker;

import androidx.annotation.NonNull;

public class Upgrade {
    //коэффицент увелечения цены
    private double Multiplier = 1.07;
    //Todo: обычные типы не подходят нужны биг десимал может и биг инт
    private String name;
    private int lvl = 0;
    private double speed;
    private int basePrice;

    @NonNull
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("\n");
        str.append("name: ").append(this.name).append("\n");
        str.append("speed: ").append(this.speed).append("\n");
        str.append("price: ").append(this.basePrice).append("\n");
        str.append("lvl: ").append(this.lvl);

        return str.toString();
    }

    public Upgrade (String name, int price, double speed, int lvl) {
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
    public int getLVL() { return this.lvl; }
//    public double getSpeed() { return this.speed; }
    public int getBasePrice() { return this.basePrice; }

    public double getTotalSpeed() {
        if(lvl == 0)
            return speed;
        else
            return MainActivity.roundAvoid(speed * lvl, 2);
    }

    public int getTotalPrice() {
        if(lvl == 0)
            return basePrice;
        else
            return (int)Math.round(basePrice * Math.pow(Multiplier, lvl));
    }
}
