package edu.byuh.cis.cs203.bw_collisions_2019.graphics;

import android.graphics.Bitmap;

import edu.byuh.cis.cs203.bw_collisions_2019.misc.Direction;
import edu.byuh.cis.cs203.bw_collisions_2019.misc.Size;

public class Airplane extends Enemy {

    private static float minY, maxY;

    /**
     * The default constructor. Loads the correct plane picture for its size
     */
    public Airplane() {
        super();
    }

    /**
     * Setter method intended to be called by the View subclass, to set the upper and lower range
     * of this sprite's Y coordinates. The caller may pass either end of the range as either the
     * first or second parameter, and the method will put the smaller value in minY and the larger
     * in maxY.
     * @param y1 one end of the range
     * @param y2 the other end of the range
     */
    public static void setSkyLimits(float y1, float y2) {
        minY = Math.min(y1, y2);
        maxY = Math.max(y1, y2);
    }

    @Override
    public float getRandomHeight() {
        return (float)(minY + (maxY-minY)*Math.random());
    }

    @Override
    protected void loadImage() {
        image = ImageCache.getAirplaneImage(size, dir);
        bounds.set(0,0,image.getWidth(), image.getHeight());
    }

    @Override
    public int getPointValue() {
        switch (size) {
            case SMALL:
                return 75;
            case MEDIUM:
                return 20;
            case LARGE:
            default:
                return 15;
        }
    }

    @Override
    public Bitmap explodingImage() {
        return ImageCache.getAirplaneExposion();
    }

}