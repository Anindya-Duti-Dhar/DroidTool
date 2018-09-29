package duti.com.droidtool.dtlib;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.model.SpinnerValue;


public class Mapper<T> {

    DroidTool dt;
    Object objModel;
    HashMap<String, SpinnerValue[]> spinnerValueMap = new HashMap<>();
    public SpeedMap speedMap;

    public Mapper(DroidTool droidTool, Object o, HashMap<String, SpinnerValue[]> spinnerValueMap) {
        dt = droidTool;
        this.spinnerValueMap = spinnerValueMap;
        objModel = o;
        speedMap = new SpeedMap();
    }

    //region ModelFromUI
    public Object ModelFromUI(Object model) {
        objModel = model;

        ViewGroup viewGroup = (ViewGroup) ((Activity) dt.c).getWindow().getDecorView();
        getAllChildren(viewGroup);
        return objModel;
    }

    public Object ModelFromUI(Object model, View v) {
        objModel = model;

        //ViewGroup viewGroup = (ViewGroup) ((ActivityUI) c).getWindow().getDecorView();
        getAllChildren(v);
        return objModel;
    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            boolean isSkipModelValue = false;

            View tv = (View) child;
            if (tv.getTag() != null)
                if (tv.getTag().toString().contains("exclude")) {
                    isSkipModelValue = true;
                }
            if (!isSkipModelValue) {
                if (child instanceof EditText) {
                    EditText editText = (EditText) child;
                    if (editText.getId() != -1) {
                        String editTextName = dt.c.getResources().getResourceEntryName(editText.getId());
                        String editTextValue = editText.getText().toString();
                        setModelValue(editTextName, editTextValue);
                    }
                } else if (child instanceof TextView) {
                    TextView textView = (TextView) child;
                    if (textView.getId() != -1) {
                        String textViewName = dt.c.getResources().getResourceEntryName(textView.getId());
                        String textViewValue = textView.getText().toString();
                        setModelValue(textViewName, textViewValue);
                    }
                } else if (child instanceof Spinner) {
                    Spinner spinner = (Spinner) child;
                    if (spinner.getId() != -1) {
                        String spinnerName = dt.c.getResources().getResourceEntryName(spinner.getId());
                        SpinnerValue spinnerValue = (SpinnerValue) spinner.getSelectedItem();
                        if(spinnerValue!=null)setModelValue(spinnerName, spinnerValue.valueText);
                    }
                }
            }
            result.addAll(viewArrayList);
        }
        return result;
    }

    private void setModelValue(String fieldName, String fieldValue) {
        String sFieldType = "";
        try {
            Field field = objModel.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            sFieldType = field.getType().toString();

            if (sFieldType.equalsIgnoreCase("int")) {
                int lValue = Integer.parseInt(fieldValue);
                field.set(objModel, lValue);
            }
            if (sFieldType.equalsIgnoreCase("class java.lang.Long")) {
                Long iValue = Long.parseLong(fieldValue);
                field.set(objModel, iValue);
            }
            if (sFieldType.equalsIgnoreCase("class java.lang.String")) {
                field.set(objModel, fieldValue);
            }
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    //endregion

    //region UIFromModel
    public void UIFromModel(Object model) {

        String sFieldName = "", sFieldValue = "";
        try {
            for (Field field : model.getClass().getDeclaredFields()) {
                field.setAccessible(true); // if you want to modify private fields

                sFieldName = field.getName().toString();
                sFieldValue = field.get(model) != null ? field.get(model).toString() : "";

                int resID = dt.c.getResources().getIdentifier(sFieldName, "id", dt.c.getPackageName());
                View v = (View) ((Activity) dt.c).findViewById(resID);
                setValues(v, sFieldName, sFieldValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int g = 0;
    }

    public void UIFromModel(Object model, View view) {

        String sFieldName = "", sFieldValue = "";
        try {
            for (Field field : model.getClass().getDeclaredFields()) {
                field.setAccessible(true); // if you want to modify private fields

                sFieldName = field.getName().toString();
                sFieldValue = field.get(model) != null ? field.get(model).toString() : "";

                int resID = view.getResources().getIdentifier(sFieldName, "id", dt.c.getPackageName());
                View v = view.findViewById(resID);
                setValues(v, sFieldName, sFieldValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int g = 0;
    }

    private void setValues(View v, String sFieldName, String sFieldValue) {
        if (v instanceof TextView) {
            TextView et = (TextView) v;
            et.setText(sFieldValue);
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            et.setText(sFieldValue);
        }
        if (v instanceof Spinner) {
            Spinner sp = (Spinner) v;
            sp.setSelection(getSpinnerValueIndex(Integer.parseInt(sFieldValue), spinnerValueMap.get(sFieldName)));
        }
    }

    private int getSpinnerValueIndex(int val, SpinnerValue[] spinnerValue) {
        for (int i = 0; i < spinnerValue.length; i++) {
            if (Integer.parseInt(spinnerValue[i].valueText) == val) {
                return i;
            }
        }
        return -1;
    }
    //endregion

    public ArrayList<T> JsonToModel(List<T> list, Class<?> cls) {
        ArrayList<T> modelList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            gsonBuilder.setDateFormat("dd-MMM-yyyy");
            Gson gson = gsonBuilder.create();
            LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) list.get(i);
            JsonObject jsonObject = gson.toJsonTree(map).getAsJsonObject();
            //"AncVisitDate": "2018-04-17T00:00:00"
            Object obj = gson.fromJson(jsonObject.toString().replace("T00:00:00+06:00", "").replace("T00:00:00", ""), cls);
            modelList.add((T) obj);
        }
        return modelList;
    }

    public T JsonToModel(Object list, Class<?> cls) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        JsonObject jsonObject = gson.toJsonTree(list).getAsJsonObject();
        Object obj = gson.fromJson(jsonObject.toString().replace("T00:00:00+06:00", "").replace("T00:00:00", ""), cls);

        return (T) obj;
    }


    public class SpeedMap {
        private void setFieldValue(Object model, String fieldName, String value) {
            Field field = null;
            try {
                field = model.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                String fieldType = field.getType().toString();

                if (fieldType.equalsIgnoreCase("int")) {
                    int lValue = Integer.parseInt(value);
                    field.set(model, lValue);
                }
                if (fieldType.equalsIgnoreCase("long")) {
                    Long iValue = Long.parseLong(value);
                    field.set(model, iValue);
                }
                if (fieldType.equalsIgnoreCase("class java.lang.String")) {
                    field.set(model, value);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        private String getControlValue(View view, CheckList[] cl, String fieldName) {
            String val = "";
            if (view != null) {
                if (view.getId() != -1) {
                    if (view instanceof EditText) {
                        val = ((EditText) view).getText().toString();
                    } else if (view instanceof TextView) {
                        val = ((TextView) view).getText().toString();
                    } else if (view instanceof Spinner) {
                        SpinnerValue spinnerValue = (SpinnerValue) ((Spinner) view).getSelectedItem();
                        val = spinnerValue.valueText;
                    } else if (view instanceof android.support.v7.widget.RecyclerView){
                        for (int i = 0; i < cl.length; i++) {
                            if (cl[i].getName().contains(fieldName)){
                                val = cl[i].getSelectedIndexString();
                            }
                        }
                    }
                }
            }
            return val;
        }

        public Object ModelFromUI(Object refModel, int currentQNo, int maxQNo, String[] grids, CheckList[] cl) {

            Object model = refModel;
            int id = dt.c.getResources().getIdentifier("Q" + currentQNo, "id", dt.c.getPackageName());
            View view = ((Activity) dt.c).findViewById(id);
            String ctlVal = getControlValue(view, cl, "Q" + currentQNo);

            setFieldValue(model, "QID", String.valueOf(currentQNo));
            setFieldValue(model, "QNO", String.valueOf(currentQNo));
            setFieldValue(model, "QVAL", ctlVal);
            setFieldValue(model, "QREM", "REM");

            if (currentQNo == maxQNo) {
                for (int i = 0; i < grids.length; i++) {
                    int qNo = Integer.parseInt(grids[i].split(",")[0]);
                    int rNo = Integer.parseInt(grids[i].split(",")[1]);
                    int cNo = Integer.parseInt(grids[i].split(",")[2]);
                    for (int j = qNo; j <= qNo + rNo; j++) {
                        String sSpitedValue = "";
                        for (int k = 1; k <= cNo; k++) {
                            String qStr = "Q" + j + "S" + k;

                            id = dt.c.getResources().getIdentifier(qStr, "id", dt.c.getPackageName());
                            view = ((Activity) dt.c).findViewById(id);
                            ctlVal = getControlValue(view, cl, "Q" + currentQNo);
                            sSpitedValue = sSpitedValue + "," + ctlVal;
                        }
                        setFieldValue(model, "QID", String.valueOf(j));
                        setFieldValue(model, "QNO", String.valueOf(qNo));
                        setFieldValue(model, "QVAL", sSpitedValue);
                        setFieldValue(model, "QREM", "REM");
                    }
                }
            }
            return model;
        }

        public void UIFromModel(Object model, String[] grid, CheckList[] cl) {

            String sFieldName = "", sFieldValue = "";
            try {
                for (Field field : model.getClass().getDeclaredFields()) {
                    field.setAccessible(true); // if you want to modify private fields

                    sFieldValue = field.get(model) != null ? field.get(model).toString() : "";
                    sFieldName = "Q" + sFieldValue; //field.getName().toString();

                    if (Arrays.asList(grid).contains(sFieldValue)) {
                        for (int i = 0; i < sFieldValue.split(",").length; i++) {
                            sFieldName = "Q" + sFieldValue + "S" + i;
                            int resID = dt.c.getResources().getIdentifier(sFieldName, "id", dt.c.getPackageName());
                            View v = (View) ((Activity) dt.c).findViewById(resID);
                            setValues(v, sFieldName, sFieldValue);
                        }

                    } else {
                        int resID = dt.c.getResources().getIdentifier(sFieldName, "id", dt.c.getPackageName());
                        View v = (View) ((Activity) dt.c).findViewById(resID);
                        setValues(v, sFieldName, sFieldValue);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void UIFromModel(Object model, View view) {

            String sFieldName = "", sFieldValue = "";
            try {
                for (Field field : model.getClass().getDeclaredFields()) {
                    field.setAccessible(true); // if you want to modify private fields

                    sFieldName = field.getName().toString();
                    sFieldValue = field.get(model) != null ? field.get(model).toString() : "";

                    int resID = view.getResources().getIdentifier(sFieldName, "id", dt.c.getPackageName());
                    View v = view.findViewById(resID);
                    setValues(v, sFieldName, sFieldValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            int g = 0;
        }

        private void setValues(View v, String sFieldName, String sFieldValue) {
            if (v instanceof TextView) {
                TextView et = (TextView) v;
                et.setText(sFieldValue);
            }
            if (v instanceof EditText) {
                EditText et = (EditText) v;
                et.setText(sFieldValue);
            }
            if (v instanceof Spinner) {
                Spinner sp = (Spinner) v;
                sp.setSelection(getSpinnerValueIndex(Integer.parseInt(sFieldValue), spinnerValueMap.get(sFieldName)));
            }
        }

        private int getSpinnerValueIndex(int val, SpinnerValue[] spinnerValue) {
            for (int i = 0; i < spinnerValue.length; i++) {
                if (Integer.parseInt(spinnerValue[i].valueText) == val) {
                    return i;
                }
            }
            return -1;
        }

    }
}
