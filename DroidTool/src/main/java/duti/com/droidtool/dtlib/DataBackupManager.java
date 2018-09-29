package duti.com.droidtool.dtlib;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.R;

import static duti.com.droidtool.config.Constants.mDatabaseName;
import static duti.com.droidtool.config.Constants.mUserDesignation;
import static duti.com.droidtool.config.Constants.mUserFullName;
import static duti.com.droidtool.config.Constants.mUserId;


public class DataBackupManager {

    DroidTool dt;

    public DataBackupManager(DroidTool dt) {
        this.dt = dt;
    }

    public void setBackUp() throws IOException {

        String databaseName = dt.pref.getString(mDatabaseName);

        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File dataDirectory = Environment.getDataDirectory();

        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = "//data//"+ dt.c.getPackageName() +"//databases//"+databaseName;
        String backupDBPath = databaseName;
        File currentDB = new File(dataDirectory, currentDBPath);
        File backupDB = new File(externalStorageDirectory, backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            dt.msg(dt.gStr(R.string.db_exported));
        } catch (IOException e) {
            e.printStackTrace();
            dt.msg(dt.gStr(R.string.sorry));
        } finally {
            try {
                if (source != null) source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (destination != null) destination.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setEmail(dt.c, databaseName);
    }

    public void setEmail(Context context, String databaseName) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        final File file = new File(Environment.getExternalStorageDirectory(), databaseName);
        Uri uri = Uri.fromFile(file);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }


        // get app version name and code
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appVersionName = info.versionName;
        int appVersionCode = info.versionCode;

        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"anindyadutidhar@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SCiBd Sponsorship backup database");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "User Full Name: " + dt.pref.getString(mUserFullName)+"\n"+"User Designation: " + dt.pref.getString(mUserDesignation)+"\n"+"User ID: " + dt.pref.getInt(mUserId)+"\n"+"Version Name: " + appVersionName+"\n"+"Version Code: " + appVersionCode);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }


}
