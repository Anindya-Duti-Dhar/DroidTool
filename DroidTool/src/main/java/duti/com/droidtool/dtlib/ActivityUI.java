package duti.com.droidtool.dtlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.R;
import duti.com.droidtool.handlers.SwitchBoardHandler;
import duti.com.droidtool.model.onboarding.HomeSubItem;


/**
 * Created by imrose on 5/26/2018.
 */

public class ActivityUI {

    DroidTool dt;
    Context c;
    String[] mDoNotClear;

    public ActivityUI(DroidTool droidTool) {
        dt = droidTool;
        c = dt.c;
    }

    public void clearUI(int resID, String[] doNotClear) {
        ViewGroup group = (ViewGroup) ((Activity) c).getWindow().getDecorView();
        mDoNotClear = doNotClear;
        clearActivityUI(group);
        if (resID != 0) {
            View view = (View) ((Activity) c).findViewById(resID);
            view.requestFocus();
        }
    }

    public void clearUI(String[] clearResIds) {
        ViewGroup group = (ViewGroup) ((Activity) c).getWindow().getDecorView();
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            String viewName = "";
            if (view.getId() != -1) viewName = c.getResources().getResourceEntryName(view.getId());
            if (Arrays.asList(clearResIds).contains(viewName)) {
                if ((view instanceof EditText) && (!view.getTag().equals("exclude"))) ((EditText) view).setText("");
                if ((view instanceof Spinner) && (!view.getTag().equals("exclude"))) ((Spinner) view).setSelection(0);
                if ((view instanceof CheckBox) && (!view.getTag().equals("exclude"))) ((CheckBox) view).setChecked(false);
                if ((view instanceof RadioButton) && (!view.getTag().equals("exclude"))) ((RadioButton) view).setChecked(false);
            }
        }
    }

    public void clearActivityUI(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);

            String editTextName = "";
            if (view.getId() != -1)
                editTextName = c.getResources().getResourceEntryName(view.getId());

            if (mDoNotClear == null) {
                if (view instanceof EditText) ((EditText) view).setText("");
                if (view instanceof Spinner) ((Spinner) view).setSelection(0);
                if (view instanceof CheckBox) ((CheckBox) view).setChecked(false);
                if (view instanceof RadioButton) ((RadioButton) view).setChecked(false);
            } else {
                if (!Arrays.asList(mDoNotClear).contains(editTextName)) {
                    if (view instanceof EditText) ((EditText) view).setText("");
                    if (view instanceof Spinner) ((Spinner) view).setSelection(0);
                    if (view instanceof CheckBox) ((CheckBox) view).setChecked(false);
                    if (view instanceof RadioButton) ((RadioButton) view).setChecked(false);
                }
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearActivityUI((ViewGroup) view);
        }

    }

    public void disableUI() {
        ViewGroup group = (ViewGroup) ((Activity) c).getWindow().getDecorView();
        disableUIControls(group);
    }

    public void disableUIControls(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);

            if (view instanceof EditText) ((EditText) view).setEnabled(false);
            if (view instanceof Spinner) ((Spinner) view).setEnabled(false);
            if (view instanceof CheckBox) ((CheckBox) view).setEnabled(false);
            if (view instanceof RadioButton) ((RadioButton) view).setEnabled(false);
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                disableUIControls((ViewGroup) view);
        }
    }

    public void disableUI(String[] views) {
        ViewGroup group = (ViewGroup) ((Activity) c).getWindow().getDecorView();
        disableUIControls(group, views);
    }

    public void disableUIControls(ViewGroup group, String[] views) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);

            if (view instanceof EditText) ((EditText) view).setEnabled(false);
            if (view instanceof Spinner) ((Spinner) view).setEnabled(false);
            if (view instanceof CheckBox) ((CheckBox) view).setEnabled(false);
            if (view instanceof RadioButton) ((RadioButton) view).setEnabled(false);
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                disableUIControls((ViewGroup) view);
        }
        if (views.length > 0) {
            for (String s : views) {
                int resID = dt.c.getResources().getIdentifier(s, "id", dt.c.getPackageName());
                View v = (View) ((Activity) dt.c).findViewById(resID);
                setEnable(v);
            }
        }
    }

    public void enableUI(String[] views) {
        ViewGroup group = (ViewGroup) ((Activity) c).getWindow().getDecorView();
        enableUIControls(group, views);
    }

    public void enableUIControls(ViewGroup group, String[] views) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) ((EditText) view).setEnabled(true);
            if (view instanceof Spinner) ((Spinner) view).setEnabled(true);
            if (view instanceof CheckBox) ((CheckBox) view).setEnabled(true);
            if (view instanceof RadioButton) ((RadioButton) view).setEnabled(true);
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                enableUIControls((ViewGroup) view, views);
        }
        if (views.length > 0) {
            for (String s : views) {
                int resID = dt.c.getResources().getIdentifier(s, "id", dt.c.getPackageName());
                View v = (View) ((Activity) dt.c).findViewById(resID);
                setDisable(v);
            }
        }
    }

    private void setDisable(View view) {
        if (view instanceof EditText) ((EditText) view).setEnabled(false);
        if (view instanceof Spinner) ((Spinner) view).setEnabled(false);
        if (view instanceof CheckBox) ((CheckBox) view).setEnabled(false);
        if (view instanceof RadioButton) ((RadioButton) view).setEnabled(false);
    }

    private void setEnable(View view) {
        if (view instanceof EditText) ((EditText) view).setEnabled(true);
        if (view instanceof Spinner) ((Spinner) view).setEnabled(true);
        if (view instanceof CheckBox) ((CheckBox) view).setEnabled(true);
        if (view instanceof RadioButton) ((RadioButton) view).setEnabled(true);
    }

    public void call(Class activityName, String extraValue) {
        Intent intent = new Intent(c, activityName);
        if (!TextUtils.isEmpty(extraValue)) intent.putExtra("key", extraValue);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        c.startActivity(intent);
        ((Activity) c).finish();
        ((Activity) c).overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    public void call(Class<?> cls, String key1, long extraValue1, String key2, String extraValue2, String key3, String extraValue3) {
        Activity activity = (Activity) c;
        Intent intent = new Intent(c, cls);
        if (!TextUtils.isEmpty(key1)) intent.putExtra(key1, extraValue1);
        if (!TextUtils.isEmpty(key2)) intent.putExtra(key2, extraValue2);
        if (!TextUtils.isEmpty(key3)) intent.putExtra(key3, extraValue3);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        c.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    public void callService(Class<?> cls, String key1, String extraValue1, String key2, String extraValue2) {
        Intent intent = new Intent(c, cls);
        if (!TextUtils.isEmpty(key1)) intent.putExtra(key1, extraValue1);
        if (!TextUtils.isEmpty(key2)) intent.putExtra(key2, extraValue2);
        c.startService(intent);
    }

    public void callService(Class<?> cls, String key1, String extraValue1, String key2, String extraValue2, String key3, Object extraValue3, String key4, boolean extraValue4, String key5, boolean extraValue5) {
        Intent intent = new Intent(c, cls);
        if (!TextUtils.isEmpty(key1)) intent.putExtra(key1, extraValue1);
        if (!TextUtils.isEmpty(key2)) intent.putExtra(key2, extraValue2);
        if (!TextUtils.isEmpty(key3)) intent.putExtra(key3, (Serializable) extraValue3);
        if (!TextUtils.isEmpty(key4)) intent.putExtra(key4, extraValue4);
        if (!TextUtils.isEmpty(key5)) intent.putExtra(key5, extraValue5);
        c.startService(intent);
    }

    public void inflateSubMenu(final ArrayList<HomeSubItem> itemList) {
        Ux.modal m = new Ux(dt, null, null).new modal(dt);
        m.inflateSubMenu(R.layout.bottom_sheet_menu_list, R.id.mRecylerView, R.layout.adapter_bottom_sheet_menu_list, R.id.deleteActionIcon,1, itemList, new Ux.onBottomSheetMenuClick() {
            @Override
            public void onClick(int position) {
                HomeSubItem data = itemList.get(position);
                int resId = data.getItemResId();
                new SwitchBoardHandler(new DroidTool(c)).onSubItemClick(resId);
            }
        });
    }
}
