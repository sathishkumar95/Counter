package www.shiftcntrlalt.com.counter;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;


public class MyService extends Service {

    private int value=0;
    private Date initialTime, currentTime;
    private boolean isbound = true;

    @Override
    public void onDestroy() {
        isbound = false;
        super.onDestroy();
    }

    private final IBinder myBinder = new MyLocalBinder();
    private static final String TAG="myTAG";

    public void start() {

            try {

                DateFormat df = new SimpleDateFormat("hh:mm");
                currentTime = Calendar.getInstance().getTime();
                long diff = currentTime.getTime() - initialTime.getTime();


                long timeInSeconds = diff / 1000;
                long hours;
                hours = timeInSeconds / 3600;
                //long diffMinutes = timeInSeconds/60;


                double d = (double)hours;
                d = java.lang.Math.floor(d);
                value = (int)d;


//                  Just for testing purpose
//                double d = (double)diffMinutes;
//                d = java.lang.Math.floor(d);
//                value = (int) d;

                if (hours < 1) {
                    value = 1;
                }
                else {
                    value = value+1;
                }

                Log.i(TAG,"Value updated");

            } catch (Exception e) {
                Log.e(TAG, "Something went wrong while fetching value", e);

            }
        }



    public int getValue(){

        return value;
    }


    public MyService() {
        initialTime = Calendar.getInstance().getTime();
        value = 1;
        Log.i(TAG,"Service Started and intialized");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyLocalBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }

    }
}
