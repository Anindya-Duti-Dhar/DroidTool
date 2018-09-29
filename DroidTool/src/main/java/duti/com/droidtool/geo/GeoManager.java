package duti.com.droidtool.geo;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.R;
import duti.com.droidtool.database.Repository;
import duti.com.droidtool.dtlib.Ux;
import duti.com.droidtool.geo.models.District;
import duti.com.droidtool.geo.models.RcNSchool;
import duti.com.droidtool.geo.models.Unions;
import duti.com.droidtool.geo.models.Upazila;
import duti.com.droidtool.geo.models.Village;
import duti.com.droidtool.model.SpinnerValue;

import static duti.com.droidtool.config.Constants.mLanguage;


public class GeoManager {

    private DroidTool dt;
    private View mView;
    private Dialog mBottomSheetDialog;
    private boolean isInitFromDb = true;
    private onGeoChangeListener customListener = null;
    private String language;

    protected String districtCode = "0", upazilaCode = "0", unionCode = "0", villageCode = "0", rcNSchoolCode = "0";
    protected String districtText = "", upazilaText = "", unionText = "", villageText = "", rcNSchoolText = "";

    public interface onGeoChangeListener {
        void onGeoChange(String geoCode, String splitGeoCode, String geoWithRcCode, String geoText);
    }

    public GeoManager(Context context, onGeoChangeListener listener) {

        customListener = listener;

        dt = new DroidTool(context);
        language = dt.pref.getString(mLanguage);

        mView = View.inflate(context, R.layout.geo_setup, null);

        mBottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(mView);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        dt.setModalView(mView);

        initGeo();
    }

    public void inflateGeo(boolean isHidden) {
        isInitFromDb = false;
        if (!isHidden) mBottomSheetDialog.show();
    }

    boolean isGeoSetCalled = false;

    public void setGeo(String sDistrictCode, String sUpazilaCode, String sUnionCode, String sVillageCode, String sRcNSchoolCode) {
        isGeoSetCalled = true;
        districtCode = sDistrictCode;
        upazilaCode = sUpazilaCode;
        unionCode = sUnionCode;
        villageCode = sVillageCode;
        rcNSchoolCode = sRcNSchoolCode;
        districtText = dt.ui.spinner.getText(R.id.District);
        upazilaText = dt.ui.spinner.getText(upazilaCode, getUpazila(districtCode));
        unionText = dt.ui.spinner.getText(unionCode, getUnion(districtCode, upazilaCode));
        villageText = dt.ui.spinner.getText(villageCode, getVillage(districtCode, upazilaCode, unionCode));
        rcNSchoolText = dt.ui.spinner.getText(R.id.RcNSchool);
        initGeo();
    }

    // bind spinner value from database
    private void initGeo() {
        dt.ui.spinner.bind(R.id.District, getDistrict());
        dt.ui.spinner.bind(R.id.RcNSchool, getRcNSchool());
        dt.ui.spinner.bind(R.id.Upazila, getUpazila(districtCode));
        dt.ui.spinner.bind(R.id.Union, getUnion(districtCode, upazilaCode));
        dt.ui.spinner.bind(R.id.Village, getVillage(districtCode, upazilaCode, this.unionCode));

        // set Upazilla
        dt.ui.spinner.onChange(R.id.District, new Ux.onSpinnerChangeListener() {
            @Override
            public void onChange(AdapterView<?> parent, View view, int position, long id) {
                dt.ui.spinner.cascadeBind(position, R.id.Upazila, upazilaCode, getUpazila(readDistrictCode()));
                if (getUpazila(readDistrictCode()).length <= 0) {
                    dt.ui.spinner.cascadeBind(position, R.id.Union, unionCode, getUnion("", ""));
                    dt.ui.spinner.cascadeBind(position, R.id.Village, villageCode, getVillage("", "", ""));
                }
            }
        });

        // set Union
        dt.ui.spinner.onChange(R.id.Upazila, new Ux.onSpinnerChangeListener() {

            @Override
            public void onChange(AdapterView<?> parent, View view, int position, long id) {
                dt.ui.spinner.cascadeBind(position, R.id.Union, unionCode, getUnion(readDistrictCode(), readUpazilaCode()));

                if (getUnion(readDistrictCode(), readUpazilaCode()).length <= 0) {
                    dt.ui.spinner.cascadeBind(position, R.id.Village, villageCode, getVillage("", "", ""));
                }
            }
        });

        // set Village
        dt.ui.spinner.onChange(R.id.Union, new Ux.onSpinnerChangeListener() {
            @Override
            public void onChange(AdapterView<?> parent, View view, int position, long id) {
                dt.ui.spinner.cascadeBind(position, R.id.Village, villageCode, getVillage(readDistrictCode(), readUpazilaCode(), readUnionCode()));
            }
        });

        registerUpdateGeoListener();

    }

    // load data in view (Spinner)
    private void registerUpdateGeoListener() {

        dt.ui.spinner.set(R.id.District, districtCode, getDistrict());
        dt.ui.spinner.set(R.id.RcNSchool, rcNSchoolCode, getRcNSchool());

        AppCompatButton mSetGeo = mView.findViewById(R.id.mSaveAllGeoBtn);
        mSetGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                districtCode = readDistrictCode();
                upazilaCode = readUpazilaCode();
                unionCode = readUnionCode();
                villageCode = readVillageCode();
                rcNSchoolCode = readRcNSchoolCode();
                districtText = dt.ui.spinner.getText(R.id.District);
                upazilaText = dt.ui.spinner.getText(upazilaCode, getUpazila(districtCode));
                unionText = dt.ui.spinner.getText(unionCode, getUnion(districtCode, upazilaCode));
                villageText = dt.ui.spinner.getText(villageCode, getVillage(districtCode, upazilaCode, unionCode));
                rcNSchoolText = dt.ui.spinner.getText(R.id.RcNSchool);
                setGeo(districtCode, upazilaCode, unionCode, villageCode, rcNSchoolCode);
                mBottomSheetDialog.dismiss();
            }
        });

        customListener.onGeoChange(getGeoCode(), getSplitGeoCode(), getGeoWithRc(), getGeoText());

    }

    public void setLabel(String geoType) {
        if (geoType.equals("3")) {
            dt.ui.textView.set(R.id.UpazilaLabel, dt.gStr(R.string.settings_city));
            dt.ui.textView.set(R.id.UnionLabel, dt.gStr(R.string.settings_city_ward));
            dt.ui.textView.set(R.id.VillageLabel, dt.gStr(R.string.settings_city_para));
            dt.ui.textView.set(R.id.RcSchoolLabel, dt.gStr(R.string.settings_resource_center));
        } else if (geoType.equals("2")) {
            dt.ui.textView.set(R.id.UpazilaLabel, dt.gStr(R.string.settings_pourosava));
            dt.ui.textView.set(R.id.UnionLabel, dt.gStr(R.string.settings_city_ward));
            dt.ui.textView.set(R.id.VillageLabel, dt.gStr(R.string.settings_city_para));
            dt.ui.textView.set(R.id.RcSchoolLabel, dt.gStr(R.string.settings_resource_center));
        } else {
            dt.ui.textView.set(R.id.UpazilaLabel, dt.gStr(R.string.settings_upazila));
            dt.ui.textView.set(R.id.UnionLabel, dt.gStr(R.string.settings_union));
            dt.ui.textView.set(R.id.VillageLabel, dt.gStr(R.string.settings_village));
            dt.ui.textView.set(R.id.RcSchoolLabel, dt.gStr(R.string.settings_primary_school));
        }
    }

    public void disableGeo(boolean enable) {
        dt.ui.spinner.enable(R.id.District, enable);
        dt.ui.spinner.enable(R.id.Upazila, enable);
        dt.ui.spinner.enable(R.id.Union, enable);
        dt.ui.spinner.enable(R.id.Village, enable);
        dt.ui.spinner.enable(R.id.RcNSchool, enable);
    }

    public String getGeoCode() {
        return getDistrictCode() + getUpazilaCode() + getUnionCode() + getVillageCode();
    }

    public String getSplitGeoCode() {
        return getDistrictCode() + "," + getUpazilaCode() + "," + getUnionCode() + "," + getVillageCode() + "," + readRcNSchoolCode();
    }

    public String getGeoWithRc() {
        return getDistrictCode() + getUpazilaCode() + getUnionCode() + getVillageCode() + readRcNSchoolCode();
    }

    public String getGeoText() {
        return districtText + " >> " + upazilaText + " >> " + unionText + " >> " + villageText + " >> " + rcNSchoolText;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public String getUpazilaCode() {
        return upazilaCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public String getRcNSchoolCode() {
        return rcNSchoolCode;
    }

    private String readDistrictCode() {
        return dt.ui.spinner.getValue(R.id.District).isEmpty() ? districtCode : dt.ui.spinner.getValue(R.id.District);
    }

    private String readUpazilaCode() {
        return dt.ui.spinner.getValue(R.id.Upazila).isEmpty() ? upazilaCode : dt.ui.spinner.getValue(R.id.Upazila);
    }

    private String readUnionCode() {
        return dt.ui.spinner.getValue(R.id.Union).isEmpty() ? unionCode : dt.ui.spinner.getValue(R.id.Union);
    }

    private String readVillageCode() {
        return dt.ui.spinner.getValue(R.id.Village).isEmpty() ? villageCode : dt.ui.spinner.getValue(R.id.Village);
    }

    private String readRcNSchoolCode() {
        return dt.ui.spinner.getValue(R.id.RcNSchool).isEmpty() ? rcNSchoolCode : dt.ui.spinner.getValue(R.id.RcNSchool);
    }

    private SpinnerValue[] getDistrict() {
        Repository<District> repo = new Repository<District>(dt.c, new District());
        District[] list = (District[]) repo.getAll(" order by CAST(DistrictCode AS INTEGER) DESC", District[].class);
        //District[] list = (District[]) repo.getAll("", District[].class);
        SpinnerValue[] spinnerValues = new SpinnerValue[list.length];
        for (int i = 0; i < list.length; i++) {
            if (language.equals("bn"))
                spinnerValues[i] = new SpinnerValue(list[i].getDistrictNameBangla(), list[i].getDistrictCode());
            else
                spinnerValues[i] = new SpinnerValue(list[i].getDistrictName(), list[i].getDistrictCode());
        }
        int len = spinnerValues.length;
        //if (len > 0 && isInitFromDb) districtCode = spinnerValues[0].valueText;
        //if (len > 0 && isInitFromDb) districtText = spinnerValues[0].displayText;
        if (isGeoSetCalled) districtText = dt.ui.spinner.getText(districtCode, spinnerValues);
        if (len <= 0) dt.msg(dt.gStr(R.string.geo_missing));

        if ((len > 0) && (districtCode.equals("0"))) districtCode = spinnerValues[0].valueText;
        if ((len > 0) && (TextUtils.isEmpty(districtText)))
            districtText = spinnerValues[0].displayText;

        return spinnerValues;
    }

    private SpinnerValue[] getUpazila(String dsCode) {
        Repository<Upazila> repo = new Repository<Upazila>(dt.c, new Upazila());
        Upazila[] list = (Upazila[]) repo.get("DistrictCode = '" + dsCode + "'", " order by UpazilaCode DESC", Upazila[].class);
        //Upazila[] list = (Upazila[]) repo.get("DistrictCode = '" + dsCode + "'", "", Upazila[].class);
        SpinnerValue[] spinnerValues = new SpinnerValue[list.length];
        for (int i = 0; i < list.length; i++) {
            if (language.equals("bn"))
                spinnerValues[i] = new SpinnerValue(list[i].getUpazilaNameBangla(), list[i].getUpazilaCode());
            else
                spinnerValues[i] = new SpinnerValue(list[i].getUpazilaName(), list[i].getUpazilaCode());
        }
        int len = spinnerValues.length;
        //if (len > 0 && isInitFromDb) upazilaCode = spinnerValues[0].valueText;
        //if (len > 0 && isInitFromDb) upazilaText = spinnerValues[0].displayText;

        if ((len > 0) && (upazilaCode.equals("0"))) upazilaCode = spinnerValues[0].valueText;
        if ((len > 0) && (TextUtils.isEmpty(upazilaText)))
            upazilaText = spinnerValues[0].displayText;

        return spinnerValues;
    }

    private SpinnerValue[] getUnion(String dsCode, String upCode) {
        Repository<Unions> repo = new Repository<Unions>(dt.c, new Unions());
        Unions[] list = (Unions[]) repo.get("DistrictCode = '" + dsCode + "' AND UpazilaCode = '" + upCode + "'", " order by UnionCode DESC", Unions[].class);
        //Unions[] list = (Unions[]) repo.get("DistrictCode = '" + dsCode + "' AND UpazilaCode = '" + upCode + "'", "", Unions[].class);
        SpinnerValue[] spinnerValues = new SpinnerValue[list.length];
        for (int i = 0; i < list.length; i++) {
            if (language.equals("bn"))
                spinnerValues[i] = new SpinnerValue(list[i].getUnionNameBangla(), list[i].getUnionCode());
            else
                spinnerValues[i] = new SpinnerValue(list[i].getUnionName(), list[i].getUnionCode());
        }
        int len = spinnerValues.length;
        //if (len > 0 && isInitFromDb) unionCode = spinnerValues[0].valueText;
        //if (len > 0 && isInitFromDb) unionText = spinnerValues[0].displayText;

        if ((len > 0) && (unionCode.equals("0"))) unionCode = spinnerValues[0].valueText;
        if ((len > 0) && (TextUtils.isEmpty(unionText))) unionText = spinnerValues[0].displayText;

        return spinnerValues;
    }

    private SpinnerValue[] getVillage(String dsCode, String upCode, String unCode) {
        Repository<Village> repo = new Repository<Village>(dt.c, new Village());
        Village[] list = (Village[]) repo.get("DistrictCode = '" + dsCode + "' AND UpazilaCode = '" + upCode + "' AND UnionCode = '" + unCode + "'", "", Village[].class);
        SpinnerValue[] spinnerValues = new SpinnerValue[list.length];
        for (int i = 0; i < list.length; i++) {
            if (language.equals("bn"))
                spinnerValues[i] = new SpinnerValue(list[i].getVillageNameBangla(), list[i].getVillageCode());
            else
                spinnerValues[i] = new SpinnerValue(list[i].getVillageName(), list[i].getVillageCode());
        }
        int len = spinnerValues.length;
        //if (len > 0 && isInitFromDb) villageCode = spinnerValues[0].valueText;
        //if (len > 0 && isInitFromDb) villageText = spinnerValues[0].displayText;

        if ((len > 0) && (villageCode.equals("0"))) villageCode = spinnerValues[0].valueText;
        if ((len > 0) && (TextUtils.isEmpty(villageText)))
            villageText = spinnerValues[0].displayText;

        return spinnerValues;
    }

    private SpinnerValue[] getRcNSchool() {
        Repository<RcNSchool> repo = new Repository<RcNSchool>(dt.c, new RcNSchool());
        RcNSchool[] list = (RcNSchool[]) repo.getAll("", RcNSchool[].class);
        SpinnerValue[] spinnerValues = new SpinnerValue[list.length];
        for (int i = 0; i < list.length; i++) {
            if (language.equals("bn"))
                spinnerValues[i] = new SpinnerValue(list[i].getRcNSchoolNameBangla(), list[i].getRcNSchoolCode());
            else
                spinnerValues[i] = new SpinnerValue(list[i].getRcNSchoolName(), list[i].getRcNSchoolCode());
        }
        int len = spinnerValues.length;
        //if (len > 0 && isInitFromDb) rcNSchoolCode = spinnerValues[0].valueText;
        //if (len > 0 && isInitFromDb) rcNSchoolText = spinnerValues[0].displayText;
        if (isGeoSetCalled) rcNSchoolText = dt.ui.spinner.getText(rcNSchoolCode, spinnerValues);

        if ((len > 0) && (rcNSchoolCode.equals("0"))) rcNSchoolCode = spinnerValues[0].valueText;
        if ((len > 0) && (TextUtils.isEmpty(rcNSchoolText)))
            rcNSchoolText = spinnerValues[0].displayText;

        return spinnerValues;
    }
}