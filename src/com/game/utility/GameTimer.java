package com.game.utility;
/**
 * 
 * @author Huy Vu
 * game timer utility
 */

import android.os.CountDownTimer;
import android.widget.TextView;

public class GameTimer extends CountDownTimer{

    TextView display;
    
    public GameTimer(long millisecs, TextView display) {
        super(millisecs, 1000);
        this.display = display;
    }

    @Override
    public void onFinish() {
        display.setText("0");
    }

    @Override
    public void onTick(long millisUntilFinished) {
        display.setText("" + millisUntilFinished);
    }
    

}
