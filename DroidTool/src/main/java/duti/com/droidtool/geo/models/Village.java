package duti.com.droidtool.geo.models;

/**
 * Created by imrose on 6/8/2018.
 */

public class Village {
    private String DistrictCode;
    private String UpazilaCode;
    private String UnionCode;
    private String VillageCode;
    private String VillageName;
    private String VillageNameBangla;

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

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getVillageNameBangla() {
        return VillageNameBangla;
    }

    public void setVillageNameBangla(String villageNameBangla) {
        VillageNameBangla = villageNameBangla;
    }

    @Override
    public String toString() {
        return "Village{" +
                "DistrictCode='" + DistrictCode + '\'' +
                ", UpazilaCode='" + UpazilaCode + '\'' +
                ", UnionCode='" + UnionCode + '\'' +
                ", VillageCode='" + VillageCode + '\'' +
                ", VillageName='" + VillageName + '\'' +
                ", VillageNameBangla='" + VillageNameBangla + '\'' +
                '}';
    }
}
