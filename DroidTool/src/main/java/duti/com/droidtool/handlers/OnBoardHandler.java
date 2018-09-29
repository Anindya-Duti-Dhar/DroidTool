package duti.com.droidtool.handlers;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import java.io.IOException;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.activities.BaseOnBoardingActivity;


public class OnBoardHandler implements BaseOnBoardingActivity.onBoardListener {

    DroidTool dt = null;

    public OnBoardHandler(DroidTool dt) {
        this.dt = dt;
    }

    public interface onTabReady {
        void onReady(DroidTool droidTool, ActionBar actionBar, int position);
    }

    private onTabReady onTabReady = null;

    public void setTabReadyListener(onTabReady onTabReady) {
        this.onTabReady = onTabReady;
    }

    // do something when view pager appeared
    @Override
    public void onTabReady(ActionBar actionBar, int position) {
        if(onTabReady!=null)onTabReady.onReady(dt, actionBar, position);
    }

    public interface onTabSwitch {
        Fragment onSwitched(int position);
    }

    private onTabSwitch onTabSwitch = null;

    public void setTabSwitchListener(onTabSwitch onTabSwitch) {
        this.onTabSwitch = onTabSwitch;
    }

    @Override
    public Fragment onTabSwitched(int position) {
        Fragment fragment = null;
        if (onTabSwitch!=null) fragment = onTabSwitch.onSwitched(position);
        return fragment;
    }

    public interface onDrawerMenuSelect {
        void onSelected(DroidTool droidTool, int menuId);
    }

    private onDrawerMenuSelect onDrawerMenuSelect = null;

    public void setMenuSelectListener(onDrawerMenuSelect onDrawerMenuSelect) {
        this.onDrawerMenuSelect = onDrawerMenuSelect;
    }

    @Override
    public void onDrawerMenuSelect(int menuId) {
        if(onDrawerMenuSelect!=null)onDrawerMenuSelect.onSelected(dt, menuId);
    }
}
