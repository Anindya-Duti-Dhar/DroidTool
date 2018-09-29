package duti.com.droidtool.model.onboarding;

import android.graphics.drawable.Drawable;

import duti.com.droidtool.DroidTool;

import static duti.com.droidtool.config.Constants.mScreenHeight;


/**
 * Created by dev.pool2 on 6/7/2018.
 */

public class SyncItem {

    DroidTool dt;
    int ItemDataCount;
    Drawable ItemIcon;
    String ItemTitle;
    String ItemTag;
    int ItemSize;
    int ScreenSize;

    public Class[] getItemClass() {
        return ItemClass;
    }

    public SyncItem setItemClass(Class[] itemClass) {
        ItemClass = itemClass;
        return this;
    }

    Class[] ItemClass;

    public DroidTool getDt() {
        return dt;
    }

    public SyncItem setDt(DroidTool dt) {
        this.dt = dt;
        return this;
    }

    public int getItemDataCount() {
        return ItemDataCount;
    }

    public SyncItem setItemDataCount(int itemDataCount) {
        ItemDataCount = itemDataCount;
        return this;
    }

    public Drawable getItemIcon() {
        return ItemIcon;
    }

    public SyncItem setItemIcon(Drawable itemIcon) {
        ItemIcon = itemIcon;
        return this;
    }

    public String getItemTitle() {
        return ItemTitle;
    }

    public SyncItem setItemTitle(String itemTitle) {
        ItemTitle = itemTitle;
        return this;
    }

    public String getItemTag() {
        return ItemTag;
    }

    public SyncItem setItemTag(String itemTag) {
        ItemTag = itemTag;
        return this;
    }

    public int getItemSize() {
        return ItemSize;
    }

    public SyncItem setItemSize(int itemSize) {
        ItemSize = itemSize;
        return this;
    }

    public int getScreenSize() {
        return ScreenSize;
    }

    public SyncItem setScreenSize(int screenSize) {
        ScreenSize = screenSize;
        return this;
    }

    public SyncItem(DroidTool dt, Drawable itemIcon, int itemTitle, String itemTag, Class[] itemClass) {
        this.dt = dt;
        ItemDataCount = dt.tools.repoNotSyncCount(itemClass);
        ItemIcon = itemIcon;
        ItemTitle = dt.gStr(itemTitle);
        ItemTag = itemTag;
        ItemSize = (int) 4.047;
        ItemClass = itemClass;
        ScreenSize = dt.pref.getInt(mScreenHeight);
    }

}
