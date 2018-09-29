package duti.com.droidtool.api;


import java.io.Serializable;

public class GeoPacket implements Serializable {

    private String RcNSchoolCode;
    private String DistrictCode;
    private String UpazilaCode;
    private String UnionCode;
    private String VillageCode;

    public GeoPacket(String districtCode, String upazilaCode, String unionCode, String villageCode, String rcNSchoolCode) {
        RcNSchoolCode = rcNSchoolCode;
        DistrictCode = districtCode;
        UpazilaCode = upazilaCode;
        UnionCode = unionCode;
        VillageCode = villageCode;
    }

    public GeoPacket() {

    }

    public String getRcNSchoolCode() {
        return RcNSchoolCode;
    }

    public void setRcNSchoolCode(String rcNSchoolCode) {
        RcNSchoolCode = rcNSchoolCode;
    }

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

    public String getVillageCode() {
        return VillageCode;
    }

    public void setVillageCode(String villageCode) {
        VillageCode = villageCode;
    }

}
