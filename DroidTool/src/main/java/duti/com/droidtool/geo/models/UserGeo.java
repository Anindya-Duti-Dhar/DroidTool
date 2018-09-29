package duti.com.droidtool.geo.models;


import java.util.List;

public class UserGeo {

    List<District> District = null;
    List<Upazila> Upazila = null;
    List<Unions> Unions = null;
    List<Village> Village = null;
    int TotalRecord;

    public List<District> getDistrict() {
        return District;
    }

    public void setDistrict(List<District> district) {
        District = district;
    }

    public List<Upazila> getUpazila() {
        return Upazila;
    }

    public void setUpazila(List<Upazila> upazila) {
        Upazila = upazila;
    }

    public List<Unions> getUnions() {
        return Unions;
    }

    public void setUnions(List<Unions> unions) {
        Unions = unions;
    }

    public List<Village> getVillage() {
        return Village;
    }

    public void setVillage(List<Village> village) {
        Village = village;
    }

    public int getTotalRecord() {
        return TotalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        TotalRecord = totalRecord;
    }
}
