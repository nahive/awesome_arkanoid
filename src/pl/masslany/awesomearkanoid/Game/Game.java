package pl.masslany.awesomearkanoid.Game;


import pl.masslany.awesomearkanoid.R;
import pl.masslany.awesomearkanoid.Gui.GameLost;
import pl.masslany.awesomearkanoid.Gui.GameWon;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
// This is just a starter activity class that functions as a bridge between menu and gameengine.
public class Game extends AndroidApplication {
    int levelPicked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slidein0, R.anim.slideout0);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        levelPicked = getIntent().getExtras().getInt("Level");
        initialize(new GameEngine(this, levelPicked), cfg);
    }

    public void gameWon(int levelPoints) {
        Intent i = new Intent(this, GameWon.class);
        i.putExtra("Level", levelPicked);
        i.putExtra("Points", levelPoints);
        startActivity(i);
    }

    public void gameLost() {
        Intent i = new Intent(this, GameLost.class);
        i.putExtra("Level", levelPicked);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slidein0rev, R.anim.slideout0rev);
    }
}
