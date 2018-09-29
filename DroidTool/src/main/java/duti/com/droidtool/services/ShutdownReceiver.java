package duti.com.droidtool.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import duti.com.droidtool.DroidTool;


public class ShutdownReceiver extends BroadcastReceiver {

    DroidTool dt;

    @Override
    public void onReceive(Context context, Intent intent) {

        dt = new DroidTool(context);

        // save internet usages history
        //dt.tools.saveInternetUsagesHistory();

        dt.msg("Data is saving: " + dt.tools.getAppDataUsages() + " MB at " + dt.dateTime.getCurrentDateTime());
    }
}
