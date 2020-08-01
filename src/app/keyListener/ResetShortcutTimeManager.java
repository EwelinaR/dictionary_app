package app.keyListener;

import app.util.Observer;

import java.util.Timer;
import java.util.TimerTask;

class ResetShortcutTimeManager {

    private Observer observer;
    private Timer timer;
    private boolean toDelay;
    private boolean isSetTimer;

    ResetShortcutTimeManager(Observer observer){
        this.observer = observer;
        timer = new Timer();
        toDelay = false;
        isSetTimer = false;
    }

    void startTimer(){
        if(isSetTimer){
            toDelay = true;
        } else{
            isSetTimer = true;
            timer.schedule(new ResetKeysTimerTask(), 4000);
        }
    }

    private class ResetKeysTimerTask extends TimerTask {

        @Override
        public void run() {
            if(toDelay){
                toDelay = false;
                isSetTimer = false;
                startTimer();
            } else {
                isSetTimer = false;
                observer.update();
            }
        }
    }
}
