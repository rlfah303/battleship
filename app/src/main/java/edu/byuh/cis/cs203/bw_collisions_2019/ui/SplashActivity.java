package edu.byuh.cis.cs203.bw_collisions_2019.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import edu.byuh.cis.cs203.bw_collisions_2019.R;

public class SplashActivity extends Activity {
    private ImageView iv;

    /**
     * splash screen
     * @param b
     */
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.splash_screen);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        float w = iv.getWidth();
        float h = iv.getHeight();
        RectF startButton = new RectF(w/2-(w*0.1f),h/2-(h*0.07f), w/2-(w*0.1f)+(w*0.2f),h/2-(h*0.07f)+(h*0.14f));
        RectF prefsButton = new RectF(5,5,5+(w*0.1f),5+(h*0.1f));
        RectF aboutBoxButton = new RectF(5.0f,h-5.0f-(h*0.1f),5+(w*0.1f),h-5.0f);

        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
//            Intent marco = new Intent(this, MainActivity.class);
            if (startButton.contains(x,y)) {
                Log.d("CS203", "tapped inside start button");
                Intent lana = new Intent(this, MainActivity.class);
                startActivity(lana);

            }
            if (aboutBoxButton.contains(x,y)) {
                Log.d("CS203", "tapped inside about button");
                AlertDialog.Builder ab = new AlertDialog.Builder(iv.getContext());
                ab.setTitle(R.string.game_title)
                        .setMessage(R.string.game_message)
                        .setCancelable(true);
                AlertDialog box = ab.create();
                box.show();
            }
            if(prefsButton.contains(x,y)){
                Log.d("CS203", "tapped inside about button");
                Intent prefs = new Intent(this,Prefs.class);
                startActivity(prefs);

            }
        }
        return true;
    }
}
