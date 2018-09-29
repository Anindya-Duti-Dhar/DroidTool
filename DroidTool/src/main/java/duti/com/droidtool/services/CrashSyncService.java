package duti.com.droidtool.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import duti.com.droidtool.R;
import duti.com.droidtool.api.ApiMaster;
import duti.com.droidtool.api.ApiResponse;
import duti.com.droidtool.handlers.SyncDataHandler;

import static duti.com.droidtool.config.Constants.mItemName;
import static duti.com.droidtool.config.Constants.mTableName;


public class CrashSyncService extends Service {

    private static final String TAG = CrashSyncService.class.getSimpleName();
    private final static int FOREGROUND_ID = 999;
    private Handler handler = new Handler();

    private String tableName;
    private String itemName;

    ApiMaster apiMaster;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get intent data from activity
        if (intent != null) {
            tableName = intent.getStringExtra(mTableName);
            itemName = intent.getStringExtra(mItemName);
            makeToast(this.getString(R.string.sync_syncing_text));
        }

        PendingIntent pendingIntent = createPendingIntent();
        Notification notification = createNotification(itemName, pendingIntent);

        startForeground(FOREGROUND_ID, notification);

        // bind api master with service
        apiMaster = new ApiMaster().apiMaster(this);
        // get response and send to activity
        apiMaster.setApiCallFinishListener(new ApiMaster.onApiCallFinishListener() {
            @Override
            public void onApiCallFinish(ApiResponse data) {
                stopService();
            }
        });
        // sync data to server
        new SyncDataHandler().syncRecord(apiMaster, tableName);

        return START_STICKY;
    }

    // call to stop service
    public void stopService() {
        makeToast(this.getString(R.string.crash_sending_stop));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do after 2 seconds
                stopForeground(true);
                stopSelf();
            }
        }, 2000);
    }

    private PendingIntent createPendingIntent() {
        return PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification createNotification(String itemName, PendingIntent intent) {
        return new Notification.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(itemName + " " + this.getString(R.string.sync_syncing_text))
                .setSmallIcon(R.drawable.ic_action_file_upload)
                .setContentIntent(intent)
                .setLights(Color.GREEN, 3000, 3000)
                .build();
    }

    // method to make toast
    public void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }
}