package com.example.clicker;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
//Todo: были мысли о том что вместо фнукций в мейне сделать отдельный класс для меню и "улучшений" в целом
public class MainActivity extends AppCompatActivity {

    double PlayerClickPower = 1;
    double PlayerScore = 0;
    double PlayerSpeed = 0.1;
    int time = 0;
    public int ScreenWidth;
    public int ScreenHeight;
    public DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        dbhelper = new DBHelper(this);

        updatePlayerSpeed();

        //screen size
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        ScreenWidth = metricsB.widthPixels;
        ScreenHeight = metricsB.heightPixels;

        LinearLayout rightMenu = findViewById(R.id.rightMenu);
        rightMenu.setMinimumWidth(ScreenWidth);

        updateRightMenu();

        //Right Menu Item Click Listener
        ListView upgradesList = findViewById(R.id.upgradeList);
        upgradesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int bdid = position+1;
                Upgrade up = dbhelper.getOneUpgrade(bdid);
                buy(String.valueOf(bdid), up);
            }
        });


        // TIMER of score up and score show
        final TextView scoreView = (TextView) findViewById(R.id.scoreView);
        final TextView speedView = (TextView) findViewById(R.id.speedView);
        Timer timer = new Timer();
        long delay = 0;
        long period = 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time++;
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        //inc per second
                        PlayerScore += PlayerSpeed;
                        //display it
                        scoreView.setText(String.valueOf((int) PlayerScore));
                        speedView.setText(String.valueOf((roundAvoid(PlayerSpeed, 2))) + "/sec");
                    }
                });
            }
        },delay,period);
    }

    //Todo: почему метот бай() кроме покупки ещё и проверяет возможность покупки, а itemListener пустует
    //Todo: работу с бд можно новой функцией в БДХелпере. Тогда в связи с перыдущим todo буй исчезнет.
    private void buy(String id, Upgrade up) {
        if(PlayerScore > up.getTotalPrice()) {
            PlayerScore = PlayerScore - up.getTotalPrice();

            Log.d("KEY", up.toString() + " PlayserScore:" + PlayerScore +
                    ", TotalPrice: " + up.getTotalPrice() + ", BasePrice: " + up.getBasePrice());

            ContentValues cv = new ContentValues();
            cv.put(dbhelper.KEY_LVL, up.getLVL() + 1);

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            db.update(dbhelper.TABLE_UPGRADES, cv, "id = ?", new String[]{id});

            db.close();
            updateRightMenu();
            updatePlayerSpeed();
        } else {
            toast("Нехвататет денег...");
        }
    }

    private void updatePlayerSpeed() {
        Upgrade[] ups = dbhelper.getAllUpgrades();
        double speed = 0;

        for(Upgrade item : ups) {
            if(item.getLVL() > 0) {
                speed += item.getTotalSpeed();
                Log.d("key", "speed: " + item.getTotalSpeed());
            }
        }

        PlayerSpeed = speed + 0.1;
        //0.1 is base speed
    }

    private void updateRightMenu() {
        ListView upgradesList = findViewById(R.id.upgradeList);
        Upgrade[] upgrades = dbhelper.getAllUpgrades();
        UpgradeAdapter adapter = new UpgradeAdapter(this, R.layout.upgrades_list_item, upgrades);
        upgradesList.setAdapter(adapter);
    }

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public void incScore(View view) {
        PlayerScore += PlayerClickPower;
    }

    public void openRightMenu(View view) {
        dbhelper.logTableUpgrade();

        final LinearLayout menu = findViewById(R.id.rightMenu);
        final int animDuration = 500;
        final TranslateAnimation animation = new TranslateAnimation(
                0,
                menu.getTranslationX() == 0 ? -ScreenWidth : ScreenWidth,
                0,
                0
        );
        animation.setDuration(animDuration);
        menu.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                menu.setTranslationX(menu.getTranslationX() == 0 ? -ScreenWidth : 0);
                animation.cancel();
            }
        }, animDuration);
    }

    public void nothing(View v) {
        Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show();
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
