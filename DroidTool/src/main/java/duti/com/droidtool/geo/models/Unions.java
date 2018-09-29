package duti.com.droidtool.geo.models;

/**
 * Created by imrose on 6/8/2018.
 */

public class Unions {
    private String DistrictCode;
    private String UpazilaCode;
    private String UnionCode;
    private String UnionName;
    private String UnionNameBangla;


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

    public String getUnionCode() {
        return UnionCode;
    }

    public void setUnionCode(String unionCode) {
        UnionCode = unionCode;
    }

    public String getUnionName() {
        return UnionName;
    }

    public void setUnionName(String unionName) {
        UnionName = unionName;
    }

    public String getUnionNameBangla() {
        return UnionNameBangla;
    }

    public void setUnionNameBangla(String unionNameBangla) {
        UnionNameBangla = unionNameBangla;
    }

    @Override
    public String toString() {
        return "Unions{" +
                "DistrictCode='" + DistrictCode + '\'' +
                ", UpazilaCode='" + UpazilaCode + '\'' +
                ", UnionCode='" + UnionCode + '\'' +
                ", UnionName='" + UnionName + '\'' +
                ", UnionNameBangla='" + UnionNameBangla + '\'' +
                '}';
    }
}
