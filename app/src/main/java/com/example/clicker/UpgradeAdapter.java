package com.example.clicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class UpgradeAdapter extends ArrayAdapter<Upgrade> {

    private LayoutInflater inflater;
    private int layout;
    private Upgrade[] upgrades;

    public UpgradeAdapter(@NonNull Context context, int resource, Upgrade[] upgrades) {
        super(context, resource, upgrades);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.upgrades = upgrades;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);

        TextView Name = view.findViewById(R.id.upgradeName);
        TextView lvl = view.findViewById(R.id.upgradeLVL);
        TextView price = view.findViewById(R.id.upgradePrice);
        TextView speed = view.findViewById(R.id.upgradeSpeed);

        Upgrade upgrade = upgrades[position];

        Name.setText(upgrade.getName());
        lvl.setText(String.valueOf(upgrade.getLVL()));
        price.setText(String.valueOf(upgrade.getTotalPrice()));
        speed.setText(String.valueOf(upgrade.getTotalSpeed()));

        return view;
    }
}
