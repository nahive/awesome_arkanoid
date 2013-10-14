package pl.masslany.awesomearkanoid.Gui;

import pl.masslany.awesomearkanoid.R;
import pl.masslany.awesomearkanoid.Game.Game;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.widget.TextView;
// Backend of won game.
public class GameWon extends Activity implements OnClickListener {
    private View nextLvl, levelPick;
    private int level, points;
    private TextView pointsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slidein0rev, R.anim.slideout0rev);
        setContentView(R.layout.game_won);
        level = getIntent().getExtras().getInt("Level");
        points = getIntent().getExtras().getInt("Points");
        pointsField = (TextView) findViewById(R.id.points);
        pointsField.setText(points + " points");
        init();
        start();
    }

    private void init() {
        nextLvl = findViewById(R.id.nextlvl);
        nextLvl.setOnClickListener(this);
        levelPick = findViewById(R.id.levelpickwon);
        levelPick.setOnClickListener(this);
    }

    private void start() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextlvl:
                if (level < 10) {
                    Intent intent = new Intent(this, Game.class);
                    intent.putExtra("Level", level + 1);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, LevelPicker.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                }
                break;
            case R.id.levelpickwon:
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
