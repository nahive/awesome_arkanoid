package pl.masslany.awesomearkanoid;

import pl.masslany.awesomearkanoid.Gui.MainScreen;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    // This starts main acitivity that just starts main menu intent and finishes.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent menuIntent = new Intent(this, MainScreen.class);
        startActivity(menuIntent);
        finish();
    }
    

}
