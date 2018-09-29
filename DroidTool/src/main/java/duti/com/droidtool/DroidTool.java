package duti.com.droidtool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import duti.com.droidtool.dtlib.ActivityUI;
import duti.com.droidtool.dtlib.ApiCall;
import duti.com.droidtool.dtlib.CheckPermission;
import duti.com.droidtool.dtlib.DataBackupManager;
import duti.com.droidtool.dtlib.DateTimeManager;
import duti.com.droidtool.dtlib.Db;
import duti.com.droidtool.dtlib.Etc;
import duti.com.droidtool.dtlib.LanguageSettings;
import duti.com.droidtool.dtlib.Mapper;
import duti.com.droidtool.dtlib.NetworkChecking;
import duti.com.droidtool.dtlib.Pref;
import duti.com.droidtool.dtlib.SweetAlert;
import duti.com.droidtool.dtlib.Threading;
import duti.com.droidtool.dtlib.Tools;
import duti.com.droidtool.dtlib.Ux;
import duti.com.droidtool.dtlib.Validation;
import duti.com.droidtool.dtlib.ViewController;
import duti.com.droidtool.model.ListCheckBox;
import duti.com.droidtool.model.SpinnerValue;


/**
 * DroidTool is a helper class for Android business type application
 * this tool manipulate cv and Model with no time
 * Created by Imrose & Anindya on 9/20/2017.
 */


public class DroidTool {

    private Object objModel = null;
    public Context c;
    public static Context context;
    private View modalView = null;

    private Map<String, String[]> validationMap = new HashMap<String, String[]>();
    private HashMap<String, SpinnerValue[]> spinnerValueMap = new HashMap<>();
    private ArrayList<ListCheckBox> checkBoxArrayList = new ArrayList<ListCheckBox>();

    public SweetAlert alert;
    public NetworkChecking droidNet;
    public Pref pref;
    public Ux ui;
    public Mapper mapper;
    public ActivityUI activity;
    public Validation validation;
    public ApiCall api;
    public Etc etc;
    public DateTimeManager dateTime;
    public Tools tools;
    public DataBackupManager dbBackup;
    public Threading threading;
    public Db db;
    public dynamic dynamic;
    public ViewController oldUi;
    public CheckPermission permission;
    public LanguageSettings languageSettings;

    public DroidTool(Context context) {
        c = context;
        this.context = context;
        droidNet = new NetworkChecking(this);
        alert = new SweetAlert(this);
        pref = new Pref(this);
        ui = new Ux(this, spinnerValueMap, checkBoxArrayList);
        mapper = new Mapper(this, objModel, spinnerValueMap);
        activity = new ActivityUI(this);
        validation = new Validation(this, validationMap);
        etc = new Etc(this);
        api = new ApiCall(this);
        tools = new Tools(this);
        dbBackup = new DataBackupManager(this);
        dateTime = new DateTimeManager(this);
        threading = new Threading();
        db = new Db(c);
        dynamic = new dynamic();
        oldUi = new ViewController(this);
        permission = new CheckPermission(this);
        languageSettings = new LanguageSettings(this);
    }

    public void setModalView(View view) {
        modalView = view;
        ui.setModalView(modalView);
    }

    public View getModalView() {
        return modalView;
    }

    public void msg(String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public String gStr(int resId) {
        return c.getString(resId);
    }

    public static String getStr(int resId) {
        return context.getString(resId);
    }

    public String extra() {
        String extraVal = "";
        Bundle extras = ((Activity) c).getIntent().getExtras();
        if (extras != null) {
            extraVal = extras.getString("key");
        }
        return extraVal;
    }

    public class dynamic {
        public void executeMethod(Object o, String methodName, Object val) {
            Method method = null;
            try {
                if (val.getClass() == Integer.class) {
                    method = o.getClass().getMethod(methodName, Integer.TYPE);
                    method.invoke(o, val);
                } else if (val.getClass() == Long.class){
                    method = o.getClass().getMethod(methodName, Long.TYPE);
                    method.invoke(o, val);
                } else {
                    method = o.getClass().getMethod(methodName, new Class[]{String.class});
                    method.invoke(o, new Object[]{val});
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public String getFieldValue(Object o, String fieldName) {
            return "";
        }

        public void setFieldValue(Object o, String fieldName, String value) {

        }
    }
}