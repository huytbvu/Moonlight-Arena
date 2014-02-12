package com.game.utility;
/**
 * 
 * @author Huy Vu
 * delay utility
 */

import android.os.Handler;

public class Delay {
    
    
    public static void delayTime(int millisecond){
        long time = System.currentTimeMillis();
        while(System.currentTimeMillis() < (time + millisecond));
    }
    
    public static void delayHandler(long millisecond, Handler dHandler, Runnable dRun){
        dHandler.postDelayed(dRun, millisecond);
    }

}
