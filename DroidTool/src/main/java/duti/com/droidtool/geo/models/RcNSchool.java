package duti.com.droidtool.geo.models;



public class RcNSchool {

    String RcNSchoolCode;
    String RcNSchoolName;
    String RcNSchoolNameBangla;

    public String getRcNSchoolCode() {
        return RcNSchoolCode;
    }

    public void setRcNSchoolCode(String rcNSchoolCode) {
        RcNSchoolCode = rcNSchoolCode;
    }

    public String getRcNSchoolName() {
        return RcNSchoolName;
    }

    public void setRcNSchoolName(String rcNSchoolName) {
        RcNSchoolName = rcNSchoolName;
    }

    public String getRcNSchoolNameBangla() {
        return RcNSchoolNameBangla;
    }

    public void setRcNSchoolNameBangla(String rcNSchoolNameBangla) {
        RcNSchoolNameBangla = rcNSchoolNameBangla;
    }

    @Override
    public String toString() {
        return "RcNSchool{" +
                "RcNSchoolCode='" + RcNSchoolCode + '\'' +
                ", RcNSchoolName='" + RcNSchoolName + '\'' +
                ", RcNSchoolNameBangla='" + RcNSchoolNameBangla + '\'' +
                '}';
    }
}
