package duti.com.droidtool.model.onboarding;


import android.graphics.drawable.Drawable;

import duti.com.droidtool.DroidTool;

import static duti.com.droidtool.config.Constants.mScreenHeight;

public class HomeItem {

    DroidTool dt;
    int ItemDataCount;
    Drawable ItemIcon;
    String ItemTitle;
    int ItemSize;
    int ScreenSize;
    int ItemResId;

    public DroidTool getDt() {
        return dt;
    }

    public HomeItem setDt(DroidTool dt) {
        this.dt = dt;
        return this;
    }

    public int getItemDataCount() {
        return ItemDataCount;
    }

    public HomeItem setItemDataCount(int itemDataCount) {
        ItemDataCount = itemDataCount;
        return this;
    }

    public Drawable getItemIcon() {
        return ItemIcon;
    }

    public HomeItem setItemIcon(Drawable itemIcon) {
        ItemIcon = itemIcon;
        return this;
    }

    public String getItemTitle() {
        return ItemTitle;
    }

    public HomeItem setItemTitle(String itemTitle) {
        ItemTitle = itemTitle;
        return this;
    }

    public int getItemSize() {
        return ItemSize;
    }

    public HomeItem setItemSize(int itemSize) {
        ItemSize = itemSize;
        return this;
    }

    public int getScreenSize() {
        return ScreenSize;
    }

    public HomeItem setScreenSize(int screenSize) {
        ScreenSize = screenSize;
        return this;
    }

    public int getItemResId() {
        return ItemResId;
    }

    public HomeItem setItemResId(int itemResId) {
        ItemResId = itemResId;
        return this;
    }

    public HomeItem(DroidTool dt, Drawable itemIcon, int itemTitle, Class[] itemClass) {
        this.dt = dt;
        ItemDataCount = dt.tools.repoCount(itemClass);
        ItemIcon = itemIcon;
        ItemTitle = dt.gStr(itemTitle);
        ItemResId = itemTitle;
        ItemSize = (int) 3.076;
        ScreenSize = dt.pref.getInt(mScreenHeight);
    }
}
