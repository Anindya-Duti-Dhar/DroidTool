package duti.com.droidtool.activities;

import android.app.Application;

import com.evernote.android.job.JobManager;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.services.DtJobCreator;


public class MainApp extends Application {

    private static MainApp mContext;
    private static DroidTool dt;

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new DtJobCreator());
        //JobManager.instance().geallconfigsetAllowSmallerIntervalsForMarshmallow(true); // Don't use this in production
        mContext = this;
        dt = new DroidTool(this);
    }

    public static MainApp getContext() {
        return mContext;
    }

    public static DroidTool getDt(){
        return dt;
    }

}

