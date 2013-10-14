package pl.masslany.awesomearkanoid.Gui;

import pl.masslany.awesomearkanoid.R;
import pl.masslany.awesomearkanoid.Game.FileLoad;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
// This is main menu class that includes all buttons to navigate through app.
public class MainScreen extends Activity implements OnClickListener{

    private View buttonStart, buttonSettings, buttonExit, buttonAbout;
    private Animation slideIn0, slideIn1, slideIn2, slideIn3, fadeIn;
    private static String name = "";
    private FileLoad fileLoad;
    private int levelsBeaten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awesome_arkanoid);
        init();
        start();
    }

    private void init() {
        fileLoad = new FileLoad("mem", this);
        fileLoad.loadMem(this);
        levelsBeaten = fileLoad.getLevelBeaten();
        System.out.println(levelsBeaten);
        slideIn0 = AnimationUtils.loadAnimation(this, R.anim.slidein0menu);
        slideIn1 = AnimationUtils.loadAnimation(this, R.anim.slidein1menu);
        slideIn2 = AnimationUtils.loadAnimation(this, R.anim.slidein2menu);
        slideIn3 = AnimationUtils.loadAnimation(this, R.anim.slidein3menu);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(this);
        buttonSettings = findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(this);
        buttonExit = findViewById(R.id.button_exit);
        buttonExit.setOnClickListener(this);
        buttonAbout = findViewById(R.id.button_about);
        buttonAbout.setOnClickListener(this);
    }

    private void start() {
        buttonStart.startAnimation(slideIn0);
        buttonSettings.startAnimation(slideIn1);
        buttonAbout.startAnimation(slideIn2);
        buttonExit.startAnimation(slideIn3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                Intent i = new Intent(this, LevelPicker.class);
                startActivity(i);
                break;
            case R.id.button_settings:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.button_about:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.button_exit:
                finish();
                break;
        }
    }

    public static String getName() {
        return name;
    }

    public static void setName(String nick) {
        name = nick;
    }
   
    
}
