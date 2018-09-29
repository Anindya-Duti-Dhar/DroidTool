package duti.com.droidtool.dtlib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


import duti.com.droidtool.DroidTool;
import duti.com.droidtool.api.GeoPacket;

import static android.content.Context.POWER_SERVICE;
import static duti.com.droidtool.config.Constants.mUserId;

/**
 * Created by imrose on 5/26/2018.
 */

public class Etc {

    DroidTool dt;

    PowerManager.WakeLock screenLock;

    public Etc(DroidTool dt) {
        this.dt = dt;
        getDeviceScreenLight();
    }

    // Set up the toolbar title
    public void setActionBarTitle(ActionBar actionBar, String title, String font) {
        TextView tv = new TextView(dt.c.getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText(title);
        tv.setTextSize(22);
        tv.setTextColor(Color.parseColor("#616161"));
        Typeface tf = Typeface.createFromAsset(dt.c.getAssets(), font);
        tv.setTypeface(tf);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);
    }

    public GeoPacket getFetchGeoPacket(String geoCodeForIntent) {
        GeoPacket geoPacket = new GeoPacket();
        geoPacket = null;
        if (!TextUtils.isEmpty(geoCodeForIntent)) {
            List<String> geoCodeList = Arrays.asList(geoCodeForIntent.split(","));
            geoPacket = new GeoPacket(geoCodeList.get(0), geoCodeList.get(1), geoCodeList.get(2)
                    , geoCodeList.get(3), geoCodeList.get(4));
        }
        return geoPacket;
    }

    public long getTransactionId(){
        String compositeString = String.valueOf(System.currentTimeMillis()) + String.valueOf(dt.pref.getInt(mUserId));
        long transactionId = Long.parseLong(compositeString);
        return transactionId;
    }

    public PowerManager.WakeLock getDeviceScreenLight(){
        screenLock = ((PowerManager)dt.c.getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "duti");
        return screenLock;
    }

    public void keepDeviceScreenLightOn(){
        screenLock.acquire();
    }

    public void releaseDeviceScreenLight(){
        screenLock.release();
    }

    public boolean isDeviceScreenLightOn(){
        return ((PowerManager) dt.c.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }

    public void activityLaunchDialog(String message, final Class activityName){
        dt.alert.showWarning(message);
        dt.alert.setAlertListener(new SweetAlert.AlertListener() {
            @Override
            public void onAlertClick(boolean isCancel) {
                if(!isCancel)dt.activity.call(activityName, "");
            }
        });
    }

}
