package duti.com.droidtool.dtlib;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.activities.CrashActivity;
import duti.com.droidtool.database.Repository;
import duti.com.droidtool.model.crash.CrashInfo;

import static duti.com.droidtool.config.Constants.mUserId;


public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    Activity mContext;
    String LINE_SEPARATOR = "\n";

    DroidTool dt;

    public ExceptionHandler(Activity context) {
        mContext = context;
        dt = new DroidTool(mContext);
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        prepareLogs(exception);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    public void prepareLogs(Throwable exception){
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************" + LINE_SEPARATOR);
        errorReport.append(stackTrace.toString());
        errorReport.append(LINE_SEPARATOR);
        errorReport.append(LINE_SEPARATOR + "************ BUILD INFO ************" + LINE_SEPARATOR);
        errorReport.append("SDK: "+Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: "+Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        reportLogs(errorReport.toString());
    }

    public void reportLogs(String errorLogs) {
        dt.tools.printErrorLog("custom error", "\n"+errorLogs.toString());

        // get app version details
        PackageManager manager = mContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // add data into repo
        Repository<CrashInfo> repository = new Repository<CrashInfo>(mContext, new CrashInfo());
        CrashInfo crashInfo = new CrashInfo();
        crashInfo.setServerRecordId(0);
        crashInfo.setUserId(dt.pref.getInt(mUserId));
        crashInfo.setCrashDetails(errorLogs.toString());
        crashInfo.setVersionCode(info.versionCode);
        crashInfo.setVersionName(info.versionName);
        crashInfo.setDeviceName(Build.BRAND);
        crashInfo.setDeviceModel(Build.MODEL);
        crashInfo.setCrashTime(dt.dateTime.getCurrentDateTime());
        repository.add(crashInfo);

        //Open Crash activity
        Intent intent = new Intent(mContext, CrashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
