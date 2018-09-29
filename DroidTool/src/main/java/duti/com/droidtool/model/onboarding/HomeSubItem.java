package duti.com.droidtool.model.onboarding;

import android.graphics.drawable.Drawable;

import duti.com.droidtool.DroidTool;


public class HomeSubItem {

    DroidTool dt;
    int ItemDataCount;
    Drawable ItemIcon;
    String ItemTitle;

    public int getItemResId() {
        return ItemResId;
    }

    public HomeSubItem setItemResId(int itemResId) {
        ItemResId = itemResId;
        return this;
    }

    int ItemResId;

    public DroidTool getDt() {
        return dt;
    }

    public void setDt(DroidTool dt) {
        this.dt = dt;
    }

    public int getItemDataCount() {
        return ItemDataCount;
    }

    public void setItemDataCount(int itemDataCount) {
        ItemDataCount = itemDataCount;
    }

    public Drawable getItemIcon() {
        return ItemIcon;
    }

    public void setItemIcon(Drawable itemIcon) {
        ItemIcon = itemIcon;
    }

    public String getItemTitle() {
        return ItemTitle;
    }

    public void setItemTitle(String itemTitle) {
        ItemTitle = itemTitle;
    }

    public HomeSubItem(DroidTool dt, Drawable itemIcon, int itemTitle, Class[] dotClass) {
        this.dt = dt;
        ItemIcon = itemIcon;
        ItemTitle = dt.gStr(itemTitle);
        ItemResId = itemTitle;
        ItemDataCount = dt.tools.repoCount(dotClass);
    }
}
