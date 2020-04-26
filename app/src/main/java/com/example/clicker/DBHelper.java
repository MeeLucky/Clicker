package com.example.clicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 17;
    public static final String DB_NAME = "main";
    public static final String TABLE_UPGRADES = "upgrades";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_SPEED = "speed";
    public static final String KEY_LVL = "lvl";

    Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_UPGRADES
                        + "("
                        + KEY_ID + " integer primary key autoincrement, "
                        + KEY_NAME + " text, "
                        + KEY_SPEED + " double, "
                        + KEY_PRICE + " " + " integer, "
                        + KEY_LVL + " integer"
                        + ");"
        );

        setInitialUpgrades(db);

//        logTableUpgrade();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_UPGRADES);
        onCreate(db);
    }

    public void logTableUpgrade() {

        SQLiteDatabase DB = getWritableDatabase();

        final String LOG_TAG = "DB";
        Log.d(LOG_TAG, "--- Rows in Upgrades: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = DB.query(TABLE_UPGRADES, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(KEY_ID);
            int nameColIndex = c.getColumnIndex(KEY_NAME);
            int speedColIndex = c.getColumnIndex(KEY_SPEED);
            int priceColIndex = c.getColumnIndex(KEY_PRICE);
            int lvlColIndex = c.getColumnIndex(KEY_LVL);

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", speed = " + c.getString(speedColIndex) +
                                ", price = " + c.getString(priceColIndex) +
                                ", lvl = " + c.getString(lvlColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }

    public void setInitialUpgrades(SQLiteDatabase DB) {
        String[] upgrades = context.getResources().getStringArray(R.array.upgrades);

        int len = upgrades.length;
        for(String item : upgrades) {
            String[] subitem = item.split(" \\| ");
            // 1 name | 2 price | 3 speed
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_NAME, subitem[0].trim());
            contentValues.put(KEY_PRICE, Integer.parseInt(subitem[1]));
            contentValues.put(KEY_SPEED, Double.parseDouble(subitem[2]));
            contentValues.put(KEY_LVL, 0);

            DB.insert(TABLE_UPGRADES, null, contentValues);
        }
    }

    public Upgrade[] getAllUpgrades() {

        SQLiteDatabase DB = getWritableDatabase();
        Cursor c = DB.query(TABLE_UPGRADES, null, null, null, null, null, null);
        int len = c.getCount();
        Upgrade[] upgrades = new Upgrade[len];

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex(KEY_ID);
            int nameColIndex = c.getColumnIndex(KEY_NAME);
            int speedColIndex = c.getColumnIndex(KEY_SPEED);
            int priceColIndex = c.getColumnIndex(KEY_PRICE);
            int lvlColIndex = c.getColumnIndex(KEY_LVL);

            for (int i = 0; i < len; i++) {
                upgrades[i] = new Upgrade(
                        c.getString(nameColIndex),
                        c.getInt(priceColIndex),
                        c.getDouble(speedColIndex),
                        c.getInt(lvlColIndex)
                );

                c.moveToNext();
            }
        }

        c.close();
        return  upgrades;
    }

    public Upgrade getOneUpgrade(int id) {

        SQLiteDatabase DB = getWritableDatabase();
        Cursor c = DB.query(
                TABLE_UPGRADES,
                null,
                "id = ?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null
        );

        //да я хуй знает крч
        if(c.getCount() > 0) {
            c.moveToFirst();
            Upgrade upgrade = new Upgrade(
                    c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getInt(c.getColumnIndex(KEY_PRICE)),
                    c.getDouble( c.getColumnIndex(KEY_SPEED)),
                    c.getInt(c.getColumnIndex(KEY_LVL))
            );

            c.close();
            return upgrade;
        }
        //else
        c.close();
        return null;
    }
}
