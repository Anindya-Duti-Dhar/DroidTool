package duti.com.droidtool.geo.models;

/**
 * Created by imrose on 6/8/2018.
 */

public class Upazila {
    private String DistrictCode;
    private String UpazilaCode;
    private String UpazilaName;
    private String UpazilaNameBangla;


    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getUpazilaCode() {
        return UpazilaCode;
    }

    public void setUpazilaCode(String upazilaCode) {
        UpazilaCode = upazilaCode;
    }

    public String getUpazilaName() {
        return UpazilaName;
    }

    public void setUpazilaName(String upazilaName) {
        UpazilaName = upazilaName;
    }

    public String getUpazilaNameBangla() {
        return UpazilaNameBangla;
    }

    public void setUpazilaNameBangla(String upazilaNameBangla) {
        UpazilaNameBangla = upazilaNameBangla;
    }

    @Override
    public String toString() {
        return "Upazila{" +
                "DistrictCode='" + DistrictCode + '\'' +
                ", UpazilaCode='" + UpazilaCode + '\'' +
                ", UpazilaName='" + UpazilaName + '\'' +
                ", UpazilaNameBangla='" + UpazilaNameBangla + '\'' +
                '}';
    }
}
