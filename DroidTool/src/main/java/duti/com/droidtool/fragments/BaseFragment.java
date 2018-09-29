package duti.com.droidtool.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.R;
import duti.com.droidtool.adapters.LinkAdapter;
import duti.com.droidtool.dtlib.ItemList;
import duti.com.droidtool.handlers.SwitchBoardHandler;
import duti.com.droidtool.model.onboarding.HomeItem;
import duti.com.droidtool.model.onboarding.SyncItem;

import static duti.com.droidtool.config.Constants.mIsSuccess;
import static duti.com.droidtool.config.Constants.mItemName;
import static duti.com.droidtool.config.Constants.mMessage;
import static duti.com.droidtool.config.Constants.mOldOnHome;
import static duti.com.droidtool.config.Constants.mOldOnSync;
import static duti.com.droidtool.config.Constants.mScreenHeight;
import static duti.com.droidtool.config.Constants.mSyncData;
import static duti.com.droidtool.config.Constants.mTableName;


/**
 * Created by Administrator on 6/4/2018.
 */

public abstract class BaseFragment<T> extends Fragment {

    public RelativeLayout mScreenParent;
    public int mParentHeight;
    public Handler handler = new Handler();
    public DroidTool dt;
    public BroadcastReceiver mBroadcastReceiver;

    public static BaseFragment newInstance(BaseFragment baseFragment) {
        Bundle args = new Bundle();
        baseFragment.setArguments(args);
        return baseFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Code here
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Code here
        }
    }

    public interface onFragmentListener {

        void onConfigMain(ArrayList<HomeItem> itemList);

        void onConfigSync(ArrayList<SyncItem> itemList);

        void onMainItemClick(int resId);

        void onSubItemClick(int resId);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getLayout(), container, false);
    }

    protected abstract int getLayout();

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dt = new DroidTool(getActivity());
    }

    public interface onSyncReadyListener {
        void onSyncReady();
    }

    public void getSyncReady(onSyncReadyListener listener){
        final onSyncReadyListener onSyncReadyListener = listener;
        if(!dt.pref.getBoolean(mOldOnSync)){
            dt.alert.showProgress(dt.gStr(R.string.getting_ready));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 2000 ms
                    dt.pref.set(mOldOnSync, true);
                    // hide progress
                    dt.alert.hideDialog(dt.alert.progress);
                    onSyncReadyListener.onSyncReady();
                }
            }, 2000);
        } else onSyncReadyListener.onSyncReady();
    }

    public interface onHomeReadyListener {
        void onHomeReady();
    }

    public void getHomeReady(final Class activity, onHomeReadyListener listener){
        final onHomeReadyListener onHomeReadyListener = listener;
        if(!dt.pref.getBoolean(mOldOnHome)){
            dt.alert.showProgress(dt.gStr(R.string.getting_ready));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 1500 ms
                    dt.pref.set(mOldOnHome, true);
                    onHomeReadyListener.onHomeReady();
                    // hide progress
                    dt.alert.hideDialog(dt.alert.progress);
                    dt.activity.call(activity, "");
                }
            }, 1500);
        } else onHomeReadyListener.onHomeReady();
    }

    // get parent view size
    public void getParentViewSize(View view, int resId) {
        // and calculate view height and width
        mScreenParent = (RelativeLayout) view.findViewById(resId);
        mScreenParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                //now we can retrieve the height
                mParentHeight = mScreenParent.getHeight();
                dt.pref.set(mScreenHeight, mParentHeight);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    mScreenParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    mScreenParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public LinkAdapter<?> initListMain(final String tag, int recyclerViewRes, int adapterLayoutRes, int deleteRes, final ArrayList<?> itemList, int columnCount) {
        dt.ui.listView.itemList.set(recyclerViewRes, itemList, adapterLayoutRes, deleteRes, columnCount, LinearLayout.VERTICAL, R.drawable.ic_action_anc);
        dt.ui.listView.itemList.setRecyclerViewItemClickListener(new ItemList.onRecyclerViewItemClick() {
            @Override
            public void onItemRowClick(Object o, int position) {
                if(tag.equals("Home")){
                    HomeItem data = (HomeItem) itemList.get(position);
                    int resId = data.getItemResId();
                    new SwitchBoardHandler(dt).onMainItemClick(resId);
                } else if(tag.equals("Sync")){
                    SyncItem data = (SyncItem) itemList.get(position);
                    String tableName = data.getItemTag();
                    String itemName = data.getItemTitle();
                    Class[] itemClass = data.getItemClass();
                    dt.api.syncRecord(tableName, itemName, itemClass);
                }
            }
        });
        return dt.ui.listView.itemList.adapter;
    }

    // init broad cast receiver
    public void initBroadCastReceiver(final onSyncDoneListener listener) {
        final onSyncDoneListener onSyncDoneListener = listener;
        // get broadcast messages for different action
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                // if service send broad cast
                if (intent.getAction().equals(mSyncData)) {

                    // get broadcast status intent
                    final String tableName = intent.getStringExtra(mTableName);
                    final String itemName = intent.getStringExtra(mItemName);
                    final boolean isSuccess = intent.getBooleanExtra(mIsSuccess, false);
                    final String message = intent.getStringExtra(mMessage);

                    // dismiss upload progress and show success
                    dt.alert.hideDialog(dt.alert.progress);

                    // make message based on broadcast status
                    String dialogMessage = "";

                    // make message based on broadcast message
                    if (isSuccess) {
                        dt.pref.set(tableName, true);
                        if (message.equals("Success"))
                            dialogMessage = itemName + " " + getString(R.string.sync_success_text);
                        else if (message.equals("Partial"))
                            dialogMessage = itemName + " " + getString(R.string.sync_partial_text);
                        else
                            dialogMessage = itemName + " " + getString(R.string.sync_finished_text);
                        dt.alert.showSuccess(dt.gStr(R.string.great), dialogMessage, dt.gStr(R.string.thanks));
                    } else {
                        if (message.equals("Failure"))
                            dialogMessage = itemName + " " + getString(R.string.sync_failed_text);
                        else
                            dialogMessage = itemName + " " + getString(R.string.sync_finished_text);
                        dt.alert.showWarningWithOneButton(dt.gStr(R.string.sorry), dialogMessage);
                    }

                    onSyncDoneListener.onSyncDone();
                    dt.etc.releaseDeviceScreenLight();
                }
            }
        };
    }

    public interface onSyncDoneListener {
        void onSyncDone();
    }

}
