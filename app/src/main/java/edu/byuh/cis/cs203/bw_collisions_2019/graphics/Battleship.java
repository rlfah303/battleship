package edu.byuh.cis.cs203.bw_collisions_2019.graphics;

import android.graphics.PointF;

public class Battleship extends Sprite {

    private static Battleship battleship;

    /**
     * default cosntructor
     */
    private Battleship() {
        super();
        image = ImageCache.getBattleshipImage();
        bounds.set(0,0,image.getWidth(), image.getHeight());
    }

    public static Battleship getInstance(){
        if (battleship==null){
            battleship= new Battleship();
        }
        return battleship;
    }

//    @Override
//    protected float relativeWidth() {
//        return 0.4f;
//    }

    public PointF getRightCannonPosition() {
        return new PointF(bounds.left + bounds.width()*(167f/183f), bounds.top);
    }

    public PointF getLeftCannonPosition() {
        return new PointF(bounds.left + bounds.width()*(22f/183f), bounds.top);
    }

}
