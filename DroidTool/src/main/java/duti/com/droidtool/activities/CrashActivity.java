package duti.com.droidtool.activities;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import duti.com.droidtool.R;
import duti.com.droidtool.dtlib.SweetAlert;
import duti.com.droidtool.model.crash.CrashInfo;


public class CrashActivity extends BaseActivity<CrashInfo> {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // make a dialog without a titlebar
        setFinishOnTouchOutside(false); // prevent users from dismissing the dialog by tapping outside
        setContentView(R.layout.activity_crash);
        register(this, 0, 0);

        dt.alert.showError(dt.gStr(R.string.sorry), dt.gStr(R.string.crash_message), dt.gStr(R.string.ok));
        dt.alert.setAlertListener(new SweetAlert.AlertListener() {
            @Override
            public void onAlertClick(boolean isCancel) {
                finish();
            }
        });
    }

    @Override
    protected int onOptionsMenuInflate() {
        return 0;
    }

    @Override
    protected void onOptionsItemClick(MenuItem item) {

    }

}
