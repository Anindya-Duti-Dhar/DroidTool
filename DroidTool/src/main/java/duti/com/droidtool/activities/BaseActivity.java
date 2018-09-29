package duti.com.droidtool.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.evernote.android.job.JobRequest;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.R;
import duti.com.droidtool.adapters.LinkAdapter;
import duti.com.droidtool.api.GeoPacket;
import duti.com.droidtool.dtlib.ExceptionHandler;
import duti.com.droidtool.geo.GeoManager;

import static duti.com.droidtool.config.Constants.mFetchData;
import static duti.com.droidtool.config.Constants.mFirstTimeInstallation;
import static duti.com.droidtool.config.Constants.mIsSuccess;
import static duti.com.droidtool.config.Constants.mItemName;
import static duti.com.droidtool.config.Constants.mLocalPageSize;
import static duti.com.droidtool.config.Constants.mMessage;
import static duti.com.droidtool.config.Constants.mRecordId;
import static duti.com.droidtool.config.Constants.mSyncData;
import static duti.com.droidtool.config.Constants.mTableName;
import static duti.com.droidtool.config.Constants.mUserGeoType;
import static duti.com.droidtool.config.Constants.mUserId;


public abstract class BaseActivity<T> extends AppCompatActivity {

    public interface RecordOperation {
        void AddRecord();

        void UpdateRecord(long recordId);
    }

    public interface detailsOperation {
        void addDetails();
        void updateDetails();
    }

    public Context mContext;
    public DroidTool dt;
    public FloatingSearchView mSearchView;
    protected BroadcastReceiver mBroadcastReceiver;

    public boolean isLoading = false;

    public String mGeo = "", mSplitGeoCode = "", mGeoWithRc = "", mGeoCode = "", mGeoType = "";
    protected int mDataStatus = 0;
    protected String mUID = "";
    protected long mServerRecordId = 0;
    String mLatitude, mLongitude;

    protected GeoManager geoManager = null;

    public JobRequest.Builder jobRequestBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        new DroidTool(this).languageSettings.configLanguage();
        super.onCreate(savedInstanceState);
        // init crash detector
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        mContext = this;
        dt = new DroidTool(mContext);

        if(!dt.pref.getBoolean(mFirstTimeInstallation)){
            // init job request builder
            //jobRequestBuilder = BackgroundJob.getJobRequestBuilder();
            //BackgroundJob.schedulePeriodic(TimeUnit.HOURS.toMillis(24), jobRequestBuilder);
            //dt.pref.set(mFirstTimeInstallation, true);
        }

    }

    public void register(Context context, int toolbarRes, int activityTitle) {
        mContext = context;
        dt = new DroidTool(mContext);
        if (activityTitle > 0) {
            mGeoType = dt.pref.getString(mUserGeoType);
            setupGeo();
            setupToolbar(toolbarRes, activityTitle);
        }
    }

    public void setupGeo() {
        geoManager = new GeoManager(mContext, new GeoManager.onGeoChangeListener() {
            @Override
            public void onGeoChange(String geoCode, String splitGeoCode, String geoWithRcCode, String geoText) {
                mGeo = geoCode;
                mGeoWithRc = geoWithRcCode;
                mGeoCode = geoText;
                mSplitGeoCode = splitGeoCode;
                if (geoCustomListener != null)
                    geoCustomListener.onGeoCodeChange(geoCode, splitGeoCode, geoWithRcCode, geoText);
            }
        });
    }

    public ListManager ListManager(LinkAdapter<T> recordListAdapter, RecyclerView rv) {
        return new ListManager(recordListAdapter, rv);
    }

    public interface onListManagerCalled {
        void onTextChanged(final String newQuery);

        void onScroll();

        void onSearchMenuClicked(MenuItem item);
    }

    public class ListManager {
        LinkAdapter<T> recordListAdapter;
        RecyclerView rv;
        public int pastVisibleItems, visibleItemCount, totalItemCount;
        public onListManagerCalled customListManagerListener = null;

        public ListManager(final LinkAdapter<T> recordListAdapter, RecyclerView rv) {
            this.recordListAdapter = recordListAdapter;
            this.rv = rv;
        }

        public void setListManagerListener(int searchView, int searchTitle, final int searchMenuRes, onListManagerCalled ListManagerListener) {

            this.customListManagerListener = ListManagerListener;

            // recycler view scroll listener
            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) //check for scroll down
                    {
                        LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            //Do pagination.. i.e. fetch new data
                            if (!isLoading) {
                                customListManagerListener.onScroll();
                            }
                        }
                    }
                }
            });

            // init search listener
            mSearchView = (FloatingSearchView) findViewById(searchView);
            mSearchView.setSearchHint(getString(searchTitle));
            if (searchMenuRes != 0) {
                mSearchView.inflateOverflowMenu(searchMenuRes);
            }

            mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
                @Override
                public void onSearchTextChanged(String oldQuery, final String newQuery) {
                    if (dt.tools.IsNumber(newQuery))
                        customListManagerListener.onTextChanged(newQuery);
                }
            });
            mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
                @Override
                public void onFocus() {
                }

                @Override
                public void onFocusCleared() {
                    mSearchView.clearQuery();
                    customListManagerListener.onTextChanged("");
                }
            });

            mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
                @Override
                public void onActionMenuItemSelected(MenuItem item) {
                    if (searchMenuRes != 0) {
                        customListManagerListener.onSearchMenuClicked(item);
                    }
                }
            });
        }
    }

    public String queryStatement(boolean loadMore, String searchKey, String searchValue, long recordId) {
        isLoading = true;
        String queryStatement;
        if (!TextUtils.isEmpty(searchKey)) {
            queryStatement = "where " + searchKey + " = '" + searchValue + "'";
        } else {
            if (loadMore) {
                queryStatement = "where " + mRecordId + " < " + recordId + " order by " + mRecordId + " desc limit " + mLocalPageSize;
            } else {
                queryStatement = "where " + mRecordId + " > " + recordId + " order by " + mRecordId + " desc limit " + mLocalPageSize;
            }
        }
        return queryStatement;
    }

    public void errorDialog(String message) {
        dt.alert.showError(dt.gStr(R.string.common_warning_title), message, dt.gStr(R.string.ok));
    }

    public boolean alreadyDownload(String fetchTable) {
        return dt.pref.getBoolean(fetchTable);
    }

    // bind toolbar
    public void setupToolbar(int toolbarRes, int titleResourceId) {
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(toolbarRes);
        setSupportActionBar(toolbar);
        setActionBarTitle(getString(titleResourceId), getString(R.string.default_bangla_font));

        // bind back arrow in toolbar
        ActionBar ctBr = getSupportActionBar();
        ctBr.setDisplayHomeAsUpEnabled(true);
        ctBr.setDisplayShowHomeEnabled(true);
        ctBr.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
    }

    // Set up the toolbar title
    public void setActionBarTitle(String title, String font) {
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText(title);
        tv.setTextSize(22);
        tv.setTextColor(Color.parseColor("#616161"));
        Typeface tf = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(tf);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
    }

    public interface onGeoChangeListener {
        void onGeoCodeChange(String geoCode, String splitGeoCode, String geoWithRcCode, String geoText);
    }

    onGeoChangeListener geoCustomListener = null;

    public void onGeoCodeChange(onGeoChangeListener listener) {
        geoCustomListener = listener;
    }

    public void inflateGeo(boolean isHidden) {
        geoManager.setLabel(mGeoType);
        geoManager.inflateGeo(isHidden);
    }

    public void setGeoValue(String sDistrictCode, String sUpazilaCode, String sUnionCode, String sVillageCode, String sRcNSchoolCode) {
        geoManager.setGeo(sDistrictCode, sUpazilaCode, sUnionCode, sVillageCode, sRcNSchoolCode);
    }

    public void disableGeo(boolean enable) {
        geoManager.disableGeo(enable);
    }

    public void saveGeoInPref() {
        GeoPacket geoPref = dt.etc.getFetchGeoPacket(geoManager.getSplitGeoCode());
        dt.pref.set("DistrictCode", geoPref.getDistrictCode());
        dt.pref.set("UpazilaCode", geoPref.getUpazilaCode());
        dt.pref.set("UnionCode", geoPref.getUnionCode());
        dt.pref.set("VillageCode", geoPref.getVillageCode());
        dt.pref.set("RcNSchoolCode", geoPref.getRcNSchoolCode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(onOptionsMenuInflate()>0) getMenuInflater().inflate(onOptionsMenuInflate(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected abstract void onOptionsItemClick(MenuItem item);

    protected abstract int onOptionsMenuInflate();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        onOptionsItemClick(item);
        return true;
    }

    // back arrow action
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //by imrose
    public void RecordsAddUpdate(String primaryKey, RecordOperation recordOperation) {
        // check gps and get location
        if (dt.tools.hasGPS()) {
            mLatitude = String.valueOf(dt.tools.gps.getLatitude());
            mLongitude = String.valueOf(dt.tools.gps.getLongitude());
            if (primaryKey.isEmpty()) recordOperation.AddRecord();
            else recordOperation.UpdateRecord(Long.parseLong(primaryKey));
        }
    }

    //by duti
    public void detailsAddUpdate(boolean isForUpdate, detailsOperation detailsOperation) {
        // check gps and get location
        if (dt.tools.hasGPS()) {
            mLatitude = String.valueOf(dt.tools.gps.getLatitude());
            mLongitude = String.valueOf(dt.tools.gps.getLongitude());
            if (!isForUpdate) detailsOperation.addDetails();
            else detailsOperation.updateDetails();
        }
    }

    protected void setCommonModelValues(Object o) {
        dt.dynamic.executeMethod(o, "setServerRecordId", mServerRecordId);
        dt.dynamic.executeMethod(o, "setUID", mUID);
        dt.dynamic.executeMethod(o, "setRcNSchoolCode", geoManager.getRcNSchoolCode());
        dt.dynamic.executeMethod(o, "setDistrictCode", geoManager.getDistrictCode());
        dt.dynamic.executeMethod(o, "setUpazilaCode", geoManager.getUpazilaCode());
        dt.dynamic.executeMethod(o, "setUnionCode", geoManager.getUnionCode());
        dt.dynamic.executeMethod(o, "setVillageCode", geoManager.getVillageCode());
        dt.dynamic.executeMethod(o, "setDataCollectionDate", dt.dateTime.getSqlCurrentDate());
        dt.dynamic.executeMethod(o, "setDataCollectionTime", dt.dateTime.getCurrentTime());
        dt.dynamic.executeMethod(o, "setUserId", dt.pref.getInt(mUserId));
        dt.dynamic.executeMethod(o, "setGeoType", dt.pref.getString(mUserGeoType));
        dt.dynamic.executeMethod(o, "setLatitude", mLatitude);
        dt.dynamic.executeMethod(o, "setLongitude", mLongitude);
    }

    public interface OnBroadcastReceivedListener {
        void notifyAfterReceived(String broadcastType, String tableName, String itemName, boolean isSuccess, String message);
    }

    public OnBroadcastReceivedListener onBroadcastReceivedListener = null;

    public void initOnlyBroadCast(final boolean hasPref, final boolean hasDialog, OnBroadcastReceivedListener listener) {

        onBroadcastReceivedListener = listener;

        // init broadcast receiver
        // get broadcast messages for different action
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // get broadcast status intent
                final String tableName = intent.getStringExtra(mTableName);
                final String itemName = intent.getStringExtra(mItemName);
                final boolean isSuccess = intent.getBooleanExtra(mIsSuccess, false);
                final String message = intent.getStringExtra(mMessage);

                // dismiss upload progress and show success
                dt.alert.hideDialog(dt.alert.progress);

                // make message based on broadcast status
                String dialogMessage = "";

                // checking for type intent filter
                // if service send broad cast
                if (intent.getAction().equals(mFetchData)) {

                    // notify after full fetch
                    onBroadcastReceivedListener.notifyAfterReceived(mFetchData, tableName, itemName, isSuccess, message);

                    // make message based on broadcast message
                    if (isSuccess) {
                        if (hasPref) {
                            dt.pref.set(tableName, true);
                        }
                        if (message.equals("Success"))
                            dialogMessage = itemName + " " + getString(R.string.fetch_sucess_text);
                        else if (message.equals("Saved"))
                            dialogMessage = itemName + " " + getString(R.string.fetch_saved_text);
                        else if (message.equals("Empty"))
                            dialogMessage = itemName + " " + getString(R.string.fetch_empty_text);
                        else
                            dialogMessage = itemName + " " + getString(R.string.fetch_finished_text);
                        if(hasDialog) dt.alert.showSuccess(dt.gStr(R.string.great), dialogMessage, dt.gStr(R.string.thanks));
                    } else {
                        if (message.equals("Failure"))
                            dialogMessage = itemName + " " + getString(R.string.fetch_failed_text);
                        else if (message.equals("NotSaved"))
                            dialogMessage = itemName + " " + getString(R.string.fetch_not_saved_text);
                        else if (message.equals("Partial"))
                            dialogMessage = itemName + " " + getString(R.string.fetch_partial_text);
                        else
                            dialogMessage = itemName + " " + getString(R.string.fetch_finished_text);
                        if(hasDialog) dt.alert.showWarningWithOneButton(dt.gStr(R.string.sorry), dialogMessage);
                    }

                } else if (intent.getAction().equals(mSyncData)) {

                    // notify after success full sync
                    onBroadcastReceivedListener.notifyAfterReceived(mSyncData, tableName, itemName, isSuccess, message);

                    // make message based on broadcast message
                    if (isSuccess) {
                        dt.pref.set(tableName, true);
                        if (message.equals("Success"))
                            dialogMessage = itemName + " " + getString(R.string.sync_success_text);
                        else if (message.equals("Partial"))
                            dialogMessage = itemName + " " + getString(R.string.sync_partial_text);
                        else
                            dialogMessage = itemName + " " + getString(R.string.sync_finished_text);
                        if(hasDialog) dt.alert.showSuccess(dt.gStr(R.string.great), dialogMessage, dt.gStr(R.string.thanks));
                    } else {
                        if (message.equals("Failure"))
                            dialogMessage = itemName + " " + getString(R.string.sync_failed_text);
                        else
                            dialogMessage = itemName + " " + getString(R.string.sync_finished_text);
                        if(hasDialog) dt.alert.showWarningWithOneButton(dt.gStr(R.string.sorry), dialogMessage);
                    }

                }

                dt.etc.releaseDeviceScreenLight();

            }
        };
    }

}