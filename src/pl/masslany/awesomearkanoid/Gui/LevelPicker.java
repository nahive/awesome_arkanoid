package pl.masslany.awesomearkanoid.Gui;

import pl.masslany.awesomearkanoid.MainActivity;
import pl.masslany.awesomearkanoid.R;
import pl.masslany.awesomearkanoid.Game.FileLoad;
import pl.masslany.awesomearkanoid.Game.Game;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
// This is main menu for levels
public class LevelPicker extends Activity {
    int levelsBeaten;
    static final int GAME_WON = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_pick);
        overridePendingTransition(R.anim.slidein0rev, R.anim.slideout0rev);
        FileLoad fl = new FileLoad("mem", this);
        fl.loadMem(this);
        levelsBeaten = fl.getLevelBeaten();
        Button x;
        for (int i = 1; i <= 10; i++) {

            int id = getResources().getIdentifier("lvl" + i, "id",
                    this.getPackageName());
            x = (Button) findViewById(id);
            x.setText("" + i);
            if (i > levelsBeaten + 1) {
                x.setBackgroundColor(getResources().getColor(
                        android.R.color.holo_red_light));
            }
        }
    }

    public void levelChoose(View v) {
        String name = v.getResources().getResourceEntryName(v.getId())
                .substring(3);
        int level = Integer.parseInt(name);
        if (level < levelsBeaten + 2)
            startGame(level);
    }

    private void startGame(int level) {
        Log.d("SomeArkanoid", "startedGame");
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("Level", level);
        startActivity(intent);
    }
    public void onResume(){
    	 overridePendingTransition(R.anim.slidein0rev, R.anim.slideout0rev);
    }
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	overridePendingTransition(R.anim.slidein0, R.anim.slideout0);
    }
}
