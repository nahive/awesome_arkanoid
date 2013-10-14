package pl.masslany.awesomearkanoid.Gui;


import pl.masslany.awesomearkanoid.R;
import pl.masslany.awesomearkanoid.Game.FileLoad;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
// This is backend of settings
public class Settings extends Activity implements OnClickListener {
    private Button red, green, blue, purple, orange;
    private FileLoad fileLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slidein0, R.anim.slideout0);
        setContentView(R.layout.settings);
        init();
        start();
    }

    private void init() {
        fileLoad = new FileLoad("mem", this);
        red = (Button) findViewById(R.id.red);
        red.setOnClickListener(this);
        green = (Button) findViewById(R.id.green);
        green.setOnClickListener(this);
        blue = (Button) findViewById(R.id.blue);
        blue.setOnClickListener(this);
        purple = (Button) findViewById(R.id.purple);
        purple.setOnClickListener(this);
        orange = (Button) findViewById(R.id.orange);
        orange.setOnClickListener(this);
    }

    private void start() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.red:
                fileLoad.setPickedColor(getResources().getColor(
                        android.R.color.holo_red_light));
                break;
            case R.id.green:
                fileLoad.setPickedColor(getResources().getColor(
                        android.R.color.holo_green_light));
                break;
            case R.id.blue:
                fileLoad.setPickedColor(getResources().getColor(
                        android.R.color.holo_blue_light));
                break;
            case R.id.purple:
                fileLoad.setPickedColor(getResources().getColor(
                        android.R.color.holo_purple));
                break;
            case R.id.orange:
                fileLoad.setPickedColor(getResources().getColor(
                        android.R.color.holo_orange_light));
                break;
        }

        fileLoad.writeMem(this);
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slidein0rev, R.anim.slideout0rev);
    }
}
