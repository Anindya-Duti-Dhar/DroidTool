package duti.com.droidtool.services;


import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

import duti.com.droidtool.DroidTool;


public class BackgroundJob extends Job {

    static final String TAG = "BackgroundJob";

    DroidTool dt;

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        dt = new DroidTool(getContext());

        // save internet usages history
        dt.tools.saveInternetUsagesHistory();
        dt.msg("Data is saving: " + dt.tools.getAppDataUsages() + " MB at " + dt.dateTime.getCurrentDateTime());

        return Result.SUCCESS;
    }

    public static int schedulePeriodicDataUsagesJob(int timeInterval) {
        JobRequest.Builder jobRequestBuilder = new JobRequest.Builder(BackgroundJob.TAG);
        return jobRequestBuilder.setPeriodic(TimeUnit.MINUTES.toMillis(timeInterval), TimeUnit.MINUTES.toMillis(5))
                .setRequiredNetworkType(JobRequest.NetworkType.ANY)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    public static void cancelJob(int jobId) {
        if (JobManager.instance() != null) {
            JobManager.instance().cancel(jobId);
        }
    }

}
