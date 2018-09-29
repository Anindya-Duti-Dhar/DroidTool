package duti.com.droidtool.geo.models;

/**
 * Created by imrose on 6/8/2018.
 */

public class District {
    String DistrictCode;
    String DivisionCode;
    String DistrictName;
    String DistrictNameBangla;

    public String getDivisionCode() {
        return DivisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        DivisionCode = divisionCode;
    }

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getDistrictNameBangla() {
        return DistrictNameBangla;
    }

    public void setDistrictNameBangla(String districtNameBangla) {
        DistrictNameBangla = districtNameBangla;
    }

    @Override
    public String toString() {
        return "District{" +
                "DistrictCode='" + DistrictCode + '\'' +
                ", DivisionCode='" + DivisionCode + '\'' +
                ", DistrictName='" + DistrictName + '\'' +
                ", DistrictNameBangla='" + DistrictNameBangla + '\'' +
                '}';
    }
}
