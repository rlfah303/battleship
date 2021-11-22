package edu.byuh.cis.cs203.bw_collisions_2019.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

    private GameView gv;

    /**
     * Set up the Activity and load the GameView
     * @param savedInstanceState required by API, we just pass it to super-constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = new GameView(this);
        setContentView(gv);
    }

    /**
     * Destory when its done
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * pausing the game
     */
    @Override
    public void onPause() {
        super.onPause();
        gv.onPause();


    }

    /**
     * resuming the game
     */
    @Override
    public void onResume() {
        super.onResume();
        gv.onResume();
    }
}
