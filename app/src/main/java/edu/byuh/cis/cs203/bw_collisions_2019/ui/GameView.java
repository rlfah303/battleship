package edu.byuh.cis.cs203.bw_collisions_2019.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import edu.byuh.cis.cs203.bw_collisions_2019.R;
import edu.byuh.cis.cs203.bw_collisions_2019.graphics.DepthCharge;
import edu.byuh.cis.cs203.bw_collisions_2019.graphics.Enemy;
import edu.byuh.cis.cs203.bw_collisions_2019.graphics.ImageCache;
import edu.byuh.cis.cs203.bw_collisions_2019.graphics.Missile;
import edu.byuh.cis.cs203.bw_collisions_2019.graphics.Submarine;
import edu.byuh.cis.cs203.bw_collisions_2019.graphics.Airplane;
import edu.byuh.cis.cs203.bw_collisions_2019.graphics.Battleship;
import edu.byuh.cis.cs203.bw_collisions_2019.misc.Direction;
import edu.byuh.cis.cs203.bw_collisions_2019.misc.Size;
import edu.byuh.cis.cs203.bw_collisions_2019.misc.TickListener;
import edu.byuh.cis.cs203.bw_collisions_2019.misc.Timer;

public class GameView extends View implements TickListener {

    private Bitmap water;
    private Battleship battleship;
    private List<Airplane> planes;
    private List<Submarine> subs;
    private boolean init;
    private List<DepthCharge> bombs;
    private List<Missile> missiles;
    private Timer horloge;
    private boolean leftPop, rightPop;
    private int w,h,score;
    private Paint scorePaint;
    private int timeLeft;
    private long timeNow;
    private MediaPlayer bombsound, leftgunsound, rightgunsound, planeexplode, subexplode;

    /**
     * Constructor for our View subclass. Loads all the images.
     * @param context a reference to our main Activity class
     */
    public GameView(Context context) {
        super(context);
        bombs = new ArrayList<>();
        missiles = new ArrayList<>();
        planes = new ArrayList<>();
        subs = new ArrayList<>();
        scorePaint = new Paint();
        horloge = new Timer();
        scorePaint.setColor(Color.BLACK);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        leftPop = false;
        rightPop = false;
        init = false;
        bombsound = MediaPlayer.create(getContext(), R.raw.depth_charge);
        leftgunsound = MediaPlayer.create(getContext(), R.raw.left_gun);
        rightgunsound = MediaPlayer.create(getContext(), R.raw.right_gun);
        planeexplode = MediaPlayer.create(getContext(), R.raw.plane_explode);
        subexplode = MediaPlayer.create(getContext(), R.raw.sub_explode);

    }

    /**
     * Scales, positions, and renders the scene
     * @param c the Canvas object, provided by system
     */
    @Override
    public void onDraw(Canvas c) {
        c.drawColor(Color.WHITE);
        w = getWidth();
        h = getHeight();
        if (init == false) {
            init = true;
            Enemy.setPreference(Prefs.getSpeedPref(getContext()), Prefs.getDirectionPref(getContext()));
            resetGame();

        }

        //now draw everything
        float waterX = 0;
        while (waterX < w) {
            c.drawBitmap(water, waterX, h/2, null);
            waterX += water.getWidth();
        }
        for (Airplane a : planes) {
            a.draw(c);
        }
        for (Submarine s : subs) {
            s.draw(c);
        }

        for (DepthCharge d : bombs) {
            d.draw(c);
        }
        for (Missile d : missiles) {
            d.draw(c);
        }
        battleship.draw(c);

        //extra credit: draw the "pop" at the mouth of the cannon
        Bitmap pop = ImageCache.getCannonFire();
        if (leftPop) {
            final PointF popLocation = battleship.getLeftCannonPosition();
            c.drawBitmap(pop, popLocation.x-pop.getWidth(), popLocation.y-pop.getHeight(), null);
            leftPop = false;
        }
        if (rightPop) {
            final PointF popLocation = battleship.getRightCannonPosition();
            c.drawBitmap(pop, popLocation.x, popLocation.y-pop.getHeight(), null);
            rightPop = false;
        }

        c.drawText(getResources().getString(R.string.score_screen) + score, 5, h*0.6f, scorePaint);
        String timestr = Integer.toString(timeLeft/60)+':';
        if (timeLeft%60>=10){
            timestr = timestr+Integer.toString(timeLeft%60);

        }else {
            timestr = timestr+'0'+Integer.toString(timeLeft%60);
        }
        c.drawText(getResources().getString(R.string.time_screen) + timestr, w*0.8f,h*0.6f,scorePaint);

        if(timeLeft==0){
            int currentscore = score;
            loadScore();
            if(currentscore>score){
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setTitle(getResources().getString(R.string.congrats))
                        .setMessage(getResources().getString(R.string.highest_score))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), (d, i) -> {
                            score = currentscore;
                            saveScore();
                            planes.clear();
                            subs.clear();
                            bombs.clear();
                            missiles.clear();
                            horloge.setStop(true);
                            resetGame();
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int i) {
                                Activity parent = (Activity)getContext();
                                parent.finish();
                            }
                        });
                AlertDialog box = ab.create();
                box.show();


            }else{
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setTitle(getResources().getString(R.string.game_over))
                        .setMessage(getResources().getString(R.string.start_over))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), (d, i) -> {
                            planes.clear();
                            subs.clear();
                            bombs.clear();
                            missiles.clear();
                            horloge.setStop(true);
                            resetGame();
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int i) {
                                Activity parent = (Activity)getContext();
                                parent.finish();
                            }
                        });
                AlertDialog box = ab.create();
                box.show();

            }

        }
        cleanupOffscreenObjects();
    }

    /**
     * Deal with touch events. Launch missiles and depth charges
     * @param m the MotionEvent object, provided by the OS
     * @return always true. It just works better that way.
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            //did the user tap the bottom half of the screen? Depth Charge!
            if (y > getHeight()/2) {
                if(!Prefs.getRapidFireDepthChargesPref(getContext())) {
                    if(bombs.size() < 1) {
                        launchNewDepthCharge();
                    }
                } else {
                    launchNewDepthCharge();
                }
                if(Prefs.getMusicPref(getContext())){
                    bombsound.start();
                }

            } else {
                //did the user tap the top half of the screen? missile!
                if (x<getWidth()/2) {
                    if(!Prefs.getRapidFireMissilesPref(getContext())){
                        if(missiles.size() < 1) {
                            launchNewMissile(Direction.RIGHT_TO_LEFT);
                        }
                    } else {
                        launchNewMissile(Direction.RIGHT_TO_LEFT);
                    }
                    if(Prefs.getMusicPref(getContext())){
                        rightgunsound.start();
                    }
                } else {
                    if(!Prefs.getRapidFireMissilesPref(getContext())){
                        if(missiles.size() < 1) {
                            launchNewMissile(Direction.LEFT_TO_RIGHT);
                        }
                    } else {
                        launchNewMissile(Direction.LEFT_TO_RIGHT);
                    }
                    if(Prefs.getMusicPref(getContext())) {
                        leftgunsound.start();
                    }
                }
            }
        }
        return true;
    }

    private void launchNewDepthCharge() {
        DepthCharge dc = new DepthCharge();
        dc.setCentroid(getWidth()/2, getHeight()/2);
        bombs.add(dc);
        horloge.subscribe(dc);
    }

    private void launchNewMissile(Direction d) {
        Missile miss = new Missile(d);
        if (d == Direction.RIGHT_TO_LEFT) {
            miss.setBottomRight(battleship.getLeftCannonPosition());
            leftPop = true;
        } else {
            miss.setBottomLeft(battleship.getRightCannonPosition());
            rightPop = true;
        }
        missiles.add(miss);
        horloge.subscribe(miss);
    }

    private void cleanupOffscreenObjects() {

        //clean up missiles that go off-screen
        List<Missile>doomeded =missiles.stream().filter(t-> t.getBottom()<0).collect(Collectors.toList());
        doomeded.forEach(m -> horloge.unsubscribe(m));
        missiles.removeIf(m -> m.getBottom()<0);

        //clean up depth charges that go off-screen
        List<DepthCharge>doomededd =bombs.stream().filter(t-> t.getTop()>h).collect(Collectors.toList());
        doomededd.forEach(b -> horloge.unsubscribe(b));
        bombs.removeIf(b -> b.getTop()>h);




//        //clean up depth charges that go off-screen
//        List<Sprite> doomed = new ArrayList<>();
//        for (DepthCharge dc : bombs) {
//            if (dc.getTop() > getHeight()) {
//                doomed.add(dc);
//            }
//        }
//        for (Sprite d : doomed) {
//            bombs.remove(d);
//            horloge.unsubscribe(d);
//        }
//        doomed.clear();
//
//        //clean up missiles that go off-screen
//        for (Missile dc : missiles) {
//            if (dc.getBottom() < 0) {
//                doomed.add(dc);
//            }
//        }
//        for (Sprite d : doomed) {
//            missiles.remove(d);
//            horloge.unsubscribe(d);
//        }
    }

    private void detectCollisions() {
        for (Submarine s : subs) {
            for (DepthCharge d : bombs) {
                if (d.overlaps(s)) {
                    s.explode();
                    if(Prefs.getMusicPref(getContext())){
                        subexplode.start();
                    }

                    score += s.getPointValue();
                    //hide the depth charge off-screen; it will get cleaned
                    //up at the next touch event.
                    d.setLocation(0,ImageCache.screenHeight());
                }
            }
        }

        for (Airplane p : planes) {
            for (Missile m : missiles) {
                if (p.overlaps(m)) {
                    p.explode();
                    if(Prefs.getMusicPref(getContext())){
                        planeexplode.start();
                    }
                    score += p.getPointValue();
                    //hide the missile charge off-screen; it will get cleaned
                    //up at the next touch event.
                    m.setLocation(0,-ImageCache.screenHeight());
                }
            }
        }
    }

    @Override
    public void tick() {
        invalidate();
        detectCollisions();
        if (System.currentTimeMillis() - timeNow>=1000){
            timeLeft--;
            timeNow = System.currentTimeMillis();
        }
        if(timeLeft==0){
            horloge.setStop(true);
        }
    }

    /**
     * resetting game when its done
     */
    public void resetGame(){
        ImageCache.init(getResources(), w, h);

        scorePaint.setTextSize(h/20);

        water = ImageCache.getWaterImage();
        battleship = Battleship.getInstance();

        float battleshipX = w/2-battleship.getWidth()/2; //center the ship
        float battleshipY = h/2-battleship.getHeight()+water.getHeight(); //put the ship above the water line
        battleship.setLocation(battleshipX, battleshipY);

        //inform Airplane class of acceptable upper/lower limits of flight
        Airplane.setSkyLimits(0, battleship.getTop()-ImageCache.getAirplaneImage(Size.LARGE, Direction.RIGHT_TO_LEFT).getHeight()*2);

        //inform Submarine class of acceptable upper/lower limits of depth
        Submarine.setWaterDepth(h/2 + water.getHeight()*2, h-water.getHeight()*2);

        //instantiate enemy vessels, currently hardcoded to just 3 each
        for (int i=0; i<Prefs.getNumberPref(getContext()); i++) {
            planes.add(new Airplane());
            subs.add(new Submarine());
        }

        //Once everything is in place, start the animation loop!
        for (Airplane p : planes) {
            horloge.subscribe(p);
        }
        for (Submarine s : subs) {
            horloge.subscribe(s);
        }
        horloge.subscribe(this);
        score = 0;
        timeNow = System.currentTimeMillis();
        timeLeft =180;


    }

    /**
     * saving score
     */
    public void saveScore() {
        try(final FileReader reader = new FileReader("score.txt")){
            FileOutputStream fos = getContext().openFileOutput("Score.txt", Context.MODE_PRIVATE);
            String ivan = ""+score;
            fos.write(ivan.getBytes());
            //fos.close();
        } catch (IOException e) {
            Log.d("FWE","file writing exception");
        }
    }

    /**
     * loading score
     */
    private void loadScore() {
        try {
            FileInputStream fis = getContext().openFileInput("Score.txt");
            Scanner s = new Scanner(fis);
            String lana = s.nextLine();
            score = Integer.parseInt(lana);
            s.close();
        } catch (FileNotFoundException e) {
            Log.d("FNFE","file not found");
            score = 0;
        }
    }

    /**
     * linking with timer and Mainativity
     */
    public  void  onPause(){
        horloge.onPause();

    }
    /**
     * linking with timer and Mainativity
     */
    public void onResume(){
        horloge.onResume();

    }
}
