package pl.masslany.awesomearkanoid.Gui;

import pl.masslany.awesomearkanoid.R;
import pl.masslany.awesomearkanoid.Game.Game;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
// Backend of lost game
public class GameLost extends Activity implements OnClickListener {
    private View buttonRestart, levelPick;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slidein0rev, R.anim.slideout0rev);
        setContentView(R.layout.game_lost);
        level = getIntent().getExtras().getInt("Level");
        init();
        start();
    }

    private void init() {
        buttonRestart = findViewById(R.id.restart);
        buttonRestart.setOnClickListener(this);
        levelPick = findViewById(R.id.levelpicklost);
        levelPick.setOnClickListener(this);
    }

    private void start() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restart:
                Intent intent = new Intent(this, Game.class);
                intent.putExtra("Level", level);
                startActivity(intent);
                break;
            case R.id.levelpicklost:
                Intent i = new Intent(this, LevelPicker.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slidein0, R.anim.slideout0);
  
    }
}
