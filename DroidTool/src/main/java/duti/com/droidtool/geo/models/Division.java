package duti.com.droidtool.geo.models;

/**
 * Created by imrose on 6/8/2018.
 */

public class Division {
    String DivisionCode;
    String DivisionName;

    public String getDivisionCode() {
        return DivisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        DivisionCode = divisionCode;
    }

    public String getDivisionName() {
        return DivisionName;
    }

    public void setDivisionName(String divisionName) {
        DivisionName = divisionName;
    }

    @Override
    public String toString() {
        return "Division{" +
                "DivisionCode='" + DivisionCode + '\'' +
                ", DivisionName='" + DivisionName + '\'' +
                '}';
    }
}
