package duti.com.droidtool.services;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class DtJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case BackgroundJob.TAG:
                return new BackgroundJob();
            default:
                return null;
        }
    }
}
