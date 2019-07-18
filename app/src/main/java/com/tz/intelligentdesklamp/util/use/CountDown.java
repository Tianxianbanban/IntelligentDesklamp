package com.tz.intelligentdesklamp.util.use;

import android.util.Log;

import java.util.concurrent.TimeUnit;

public class CountDown {
    private int limitSec;
    public CountDown(int limitSec) throws InterruptedException{
        this.limitSec = limitSec;
        System.out.println("Count from "+limitSec);
        while(limitSec > 0){
            Log.d("CountDown", "remians "+ --limitSec +" s");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println("Time is out");
    }
    public int getLimitSec() {
        return limitSec;
    }
}
