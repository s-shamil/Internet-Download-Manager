/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ProjectRoot;

import static java.lang.Math.toIntExact;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Personal
 */
public class CurrSpeed implements Runnable {

    private long t1, t2;
    private int delT;
    private int size1, size2;
    private float avgSpeed;
    private float currSpeed;
    NewDownloader newDownloader;
    private boolean flag; //ei flag er kaj holo ekbar speed mepe avgSpeed k initialize kora hoise kina janano - na hoye thakle flase , hoye gele true
    private final float SMOOTHING_FACTOR; //description in constructor
    Thread t;
    private boolean shouldRun;

    CurrSpeed(NewDownloader nd) {
        newDownloader = nd;
        avgSpeed = 0;
        flag = false;
        shouldRun = true;
        SMOOTHING_FACTOR = 0.5f;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (shouldRun) {
            t1 = System.currentTimeMillis();
            size1 = newDownloader.getDownloadedVal();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CurrSpeed.class.getName()).log(Level.SEVERE, null, ex);
            }
            t2 = System.currentTimeMillis();
            size2 = newDownloader.getDownloadedVal();

            delT = toIntExact(t2 - t1);

            //bytes per sec e speed mapbo
            currSpeed = (float) (((size2 - size1) * 1000.0) / delT);

            if (!flag) {
                avgSpeed = currSpeed;
                flag = true;
            }
            //THIS LINE IS FOR GETTING SMOOTH SPEED MEASUREMENT
            avgSpeed = SMOOTHING_FACTOR * currSpeed + (1 - SMOOTHING_FACTOR) * avgSpeed;
            if (avgSpeed < 0.001) {
                avgSpeed = 0.000f;
            }
            /*SMOOTHING_FACTOR is a number between 0 and 1. The higher this number, the faster older samples are discarded. As you can see in the formula, when SMOOTHING_FACTOR is 1 you are simply using the value of your last observation. When SMOOTHING_FACTOR is 0 averageSpeed never changes. So, you want something in between, and usually a low value to get decent smoothing. I've found that 0.005 provides a pretty good smoothing value for an average download speed.*/

            //System.out.println("Curr : " + currSpeed + " Avg : " + avgSpeed);
        }

    }

    public String getDownloadSpeed() {
        float speed = avgSpeed;
        int idx = 0;
        while ((speed / 1000) >= 1.000 && idx < 1) {
            speed = speed / 1000;
            idx++;
        }
        String[] iV = {"bytes/sec", "KB/sec"};
        String sz = Float.toString(speed);
        //System.out.println(sz);
        if (sz.length() >= (sz.indexOf('.') + 4)) {
            //doshomik er por value ase, tahole 3 ghor prjnto dekhabo
            sz = sz.substring(0, sz.indexOf('.') + 4) + " " + iV[idx];

        } else {
            //doshomik er por value nai, purata dekhabo
            sz = sz + " " + iV[idx];
        }
        return sz;
    }

    public String getTimeRemaining() {
        String s;

        if (newDownloader.getStatus() == 2) {
            //complete
            s = "0 hours 0 minutess 0 seconds";
        } else {
            if (avgSpeed != 0) {
                s = TimeConverter.timeConversion((int) ((newDownloader.getSize() - newDownloader.getDownloadedVal()) / avgSpeed));
            } else {
                s = "Unknown";
            }
        }

        return s;
    }
    
    public void setShouldRunFalse(){
        shouldRun = false;
    }

}
