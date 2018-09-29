package duti.com.droidtool.services;


import android.support.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

import duti.com.droidtool.DroidTool;


public class DailyDataUsagesCheck extends DailyJob {

    static final String TAG = "DailyDataUsagesCheck";

    DroidTool dt;

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {

        dt = new DroidTool(getContext());

        // save internet usages history
        dt.tools.saveInternetUsagesHistory();

        return DailyJobResult.SUCCESS;
    }

    public static int scheduleDailyDataUsagesJob(int start, int end) {
        //JobRequest.Builder jobRequestBuilder = new JobRequest.Builder(TAG);
        //jobRequestBuilder.setRequiredNetworkType(JobRequest.NetworkType.ANY)
        //        .setUpdateCurrent(true)
       //         .build();
        // schedule between start and end in 24 hours format
        return DailyJob.schedule(new JobRequest.Builder(TAG), TimeUnit.HOURS.toMillis(start), TimeUnit.HOURS.toMillis(end));
    }

    public static void cancelJob(int jobId) {
        if(JobManager.instance()!=null){
            JobManager.instance().cancel(jobId);
        }
    }

}
