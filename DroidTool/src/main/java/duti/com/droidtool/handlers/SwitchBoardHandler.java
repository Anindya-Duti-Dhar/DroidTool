package duti.com.droidtool.handlers;


import java.util.ArrayList;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.fragments.BaseFragment;
import duti.com.droidtool.model.onboarding.HomeItem;
import duti.com.droidtool.model.onboarding.SyncItem;

public class SwitchBoardHandler implements BaseFragment.onFragmentListener {

    DroidTool dt = null;

    public SwitchBoardHandler(DroidTool dt) {
        this.dt = dt;
    }

    public interface onConfigMain {
        void onMainConfigured(DroidTool droidTool, ArrayList<HomeItem> itemList);
    }

    private onConfigMain onConfigMain = null;

    public void setConfigMainListener(onConfigMain onConfigMain) {
        this.onConfigMain = onConfigMain;
    }

    @Override
    public void onConfigMain(ArrayList<HomeItem> itemList) {
        if(onConfigMain!=null)onConfigMain.onMainConfigured(dt, itemList);
    }

    public interface onConfigSync {
        void onSyncConfigured(DroidTool droidTool, ArrayList<SyncItem> itemList);
    }

    private onConfigSync onConfigSync = null;

    public void setConfigSyncListener(onConfigSync onConfigSync) {
        this.onConfigSync = onConfigSync;
    }

    @Override
    public void onConfigSync(ArrayList<SyncItem> itemList) {
        if(onConfigSync!=null)onConfigSync.onSyncConfigured(dt, itemList);
    }

    public interface onMainItemClick {
        void onMainClicked(DroidTool droidTool, int resId);
    }

    private onMainItemClick onMainItemClick = null;

    public void setMainItemClickListener(onMainItemClick onMainItemClick) {
        this.onMainItemClick = onMainItemClick;
    }

    @Override
    public void onMainItemClick(int resId) {
        if(onMainItemClick!=null)onMainItemClick.onMainClicked(dt, resId);
    }

    public interface onSubItemClick {
        void onSubClicked(DroidTool droidTool, int resId);
    }

    private onSubItemClick onSubItemClick = null;

    public void setSubItemClickListener(onSubItemClick onSubItemClick) {
        this.onSubItemClick = onSubItemClick;
    }

    @Override
    public void onSubItemClick(int resId) {
        if(onSubItemClick!=null)onSubItemClick.onSubClicked(dt, resId);
    }

}