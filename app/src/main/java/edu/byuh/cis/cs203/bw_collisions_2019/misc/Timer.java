package edu.byuh.cis.cs203.bw_collisions_2019.misc;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class Timer extends Handler {

    private List<TickListener> observers;
    private boolean stop;
    private boolean pause_message;

    /**
     * the public constructor. Gets the timer started.
     */
    public Timer() {
        stop =false;
        observers = new ArrayList<>();
        sendMessageDelayed(obtainMessage(), 0);
    }

    /**
     * lets you register new observers to the timer
     * @param t an object that wants to be notified of timer events
     */
    public void subscribe(TickListener t) {
        observers.add(t);
    }

    /**
     * lets you remove observers from the timer
     * @param t an object who no longer wants to be notified of timer events
     */
    public void unsubscribe(TickListener t) {
        observers.remove(t);
    }

    /**
     * We override this method to notify the observers that the timer went off
     * @param msg we're not using this parameter
     */
    @Override
    public void handleMessage(Message msg) {
        notifyObservers();
        if (stop==true){
            observers.clear();
        }
        if(pause_message==false){
            sendMessageDelayed(obtainMessage(), 50);

        }
    }

    public  void  onPause(){
        pause_message = true;

    }

    public void onResume(){
        pause_message = false;
        sendMessageDelayed(obtainMessage(), 0);

    }
    private void notifyObservers() {
        for (TickListener t : observers) {
            t.tick();
        }
    }

    /**
     * stopping all the moves
     * @param sw
     */
    public void setStop(boolean sw){
        stop = sw;
    }
}
