package duti.com.droidtool.dtlib;


import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.R;
import duti.com.droidtool.model.SpinnerValue;


public class ViewController {

    // context variable
    DroidTool dt;

    // Spinner Selected listener variable
    private onSpinnerSelectedListener mySpinnerSelectedListener = null;

    // Spinner Selected listener interface
    public interface onSpinnerSelectedListener {
        void onSpinnerSelected(String valueText, Spinner spinner);
    }

    // Spinner Selected response listener
    public void spinnerSelectedResponseListener(onSpinnerSelectedListener listener) {
        this.mySpinnerSelectedListener = listener;
    }

    // checkbox checked listener variable
    private onCheckboxCheckedListener myCheckboxCheckedListener = null;

    // checkbox checked listener interface
    public interface onCheckboxCheckedListener {
        void onCheckboxChecked(String valueText, CheckBox checkBox);
    }

    // checkbox checked response listener
    public void checkboxCheckedResponseListener(onCheckboxCheckedListener listener) {
        this.myCheckboxCheckedListener = listener;
    }

    // Edit text watcher listener variable
    private onEditTextWatcherListener myEditTextWatcherListener = null;

    // Edit text watcher listener interface
    public interface onEditTextWatcherListener {
        void onEditTextWatcher(Editable editable, String string, EditText editText);
    }

    // Edit text watcher response listener
    public void editTextWatcherResponseListener(onEditTextWatcherListener listener) {
        this.myEditTextWatcherListener = listener;
    }

    // empty constructor
    public ViewController(DroidTool droidTool) {
        dt = droidTool;
    }

    // init default spinner
    public Spinner initSpinner(int resId, SpinnerValue[] spinnerValue) {
        Spinner spinner = (Spinner) ((Activity) dt.c).findViewById(resId);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(dt.c, R.layout.spinner_drop, spinnerValue);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        return spinner;
    }

    public Spinner initSpinner(View view, int resId, SpinnerValue[] spinnerValue) {
        Spinner spinner = view.findViewById(resId);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(dt.c, R.layout.spinner_drop, spinnerValue);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        return spinner;
    }

    // init selected spinner
    public Spinner initSelectedSpinner(int resId, SpinnerValue[] spinnerValue, int position) {
        Spinner spinner = (Spinner) ((Activity) dt.c).findViewById(resId);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(dt.c, R.layout.spinner_drop, spinnerValue);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        // auto select
        spinner.setSelection(getSpinnerIndex(position, spinnerValue));
        return spinner;
    }

    public Spinner initSelectedSpinner(View view, int resId, SpinnerValue[] spinnerValue, int position) {
        Spinner spinner = view.findViewById(resId);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(dt.c, R.layout.spinner_drop, spinnerValue);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        // auto select
        spinner.setSelection(getSpinnerIndex(position, spinnerValue));
        return spinner;
    }

    // get spinner index from given array object
    public int getSpinnerIndex(int val, SpinnerValue[] spinnerValue) {
        for (int i = 0; i < spinnerValue.length; i++) {
            if (Integer.parseInt(spinnerValue[i].valueText) == val) {
                return i;
            }
        }
        return -1;
    }

    // get spinner value of index
    public String getSpinnerArrayIndexValue(int resId) {
        String arrayIndexValue = "";
        List<SpinnerValue> spinnerValues = new ArrayList<SpinnerValue>();
        Spinner spinner = (Spinner) ((Activity) dt.c).findViewById(resId);
        SpinnerValue value = (SpinnerValue) spinner.getSelectedItem();
        spinnerValues.add(value);
        for (int j = 0; j < spinnerValues.size(); j++) {
            arrayIndexValue = spinnerValues.get(j).valueText;
        }
        return arrayIndexValue;
    }

    public String getSpinnerArrayIndexValue(View view, int resId) {
        String arrayIndexValue = "";
        List<SpinnerValue> spinnerValues = new ArrayList<SpinnerValue>();
        Spinner spinner = view.findViewById(resId);
        SpinnerValue value = (SpinnerValue) spinner.getSelectedItem();
        spinnerValues.add(value);
        for (int j = 0; j < spinnerValues.size(); j++) {
            arrayIndexValue = spinnerValues.get(j).valueText;
        }
        return arrayIndexValue;
    }

    public Spinner onSpinnerSelected(final int resId) {
        final Spinner spinner = (Spinner) ((Activity) dt.c).findViewById(resId);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mySpinnerSelectedListener != null) {
                    mySpinnerSelectedListener.onSpinnerSelected(getSpinnerArrayIndexValue(resId), spinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return spinner;
    }

    public Spinner onSpinnerSelected(View view, final int resId) {
        final Spinner spinner = view.findViewById(resId);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mySpinnerSelectedListener != null) {
                    mySpinnerSelectedListener.onSpinnerSelected(getSpinnerArrayIndexValue(view, resId), spinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return spinner;
    }

    // call Edit text along with it's watcher
    public EditText editTextWithWatcher(View view, int resID) {

        // init edit text
        final EditText editText = (EditText) view.findViewById(resID);

        // init watcher of this edit text
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                // integrate with our custom listener to send result to the activity
                if (myEditTextWatcherListener != null) {
                    myEditTextWatcherListener.onEditTextWatcher(editable, editable.toString(), editText);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        return editText;
    }

    // call Edit text along with it's watcher
    public EditText editTextWithWatcher(int resID) {

        // init edit text
        final EditText editText = (EditText) ((Activity) dt.c).findViewById(resID);

        // init watcher of this edit text
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                // integrate with our custom listener to send result to the activity
                if (myEditTextWatcherListener != null) {
                    myEditTextWatcherListener.onEditTextWatcher(editable, editable.toString(), editText);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        return editText;
    }

    // call Checkbox along with it's listener
    public CheckBox checkboxWithListener(View view, int resID) {

        // init Checkbox
        final CheckBox checkBox = (CheckBox) view.findViewById(resID);

        // init listener of this check box
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String checkboxValue = "0";
                if(isChecked){
                    checkboxValue = "1";
                } else {
                    checkboxValue = "0";
                }
                // integrate with our custom listener to send result to the activity
                if (myCheckboxCheckedListener != null) {
                    myCheckboxCheckedListener.onCheckboxChecked(checkboxValue, checkBox);
                }
            }
        });
        return checkBox;

    }

    // call Checkbox along with it's listener
    public CheckBox checkboxWithListener(int resID, String state) {

        // init Checkbox
        final CheckBox checkBox = (CheckBox) ((Activity) dt.c).findViewById(resID);
        // bind default state
        if(state.equals("1")){
            checkBox.setChecked(true);
            checkBox.setText(dt.gStr(R.string.yes));
        } else {
            checkBox.setChecked(false);
            checkBox.setText(dt.gStr(R.string.no));
        }

        // init listener of this check box
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String checkboxValue = "0";
                if(isChecked){
                    checkboxValue = "1";
                    checkBox.setText(dt.gStr(R.string.yes));
                } else {
                    checkboxValue = "0";
                    checkBox.setText(dt.gStr(R.string.no));
                }
                // integrate with our custom listener to send result to the activity
                if (myCheckboxCheckedListener != null) {
                    myCheckboxCheckedListener.onCheckboxChecked(checkboxValue, checkBox);
                }
            }
        });
        return checkBox;
    }

}
