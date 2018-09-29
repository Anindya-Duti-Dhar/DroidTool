package duti.com.droidtool.model;

/**
 * Created by dev.pool2 on 1/30/2018.
 */

public class ListCheckBox {

    private String CheckboxText;
    private boolean checkState;

    public String getCheckboxText() {
        return CheckboxText;
    }

    public void setCheckboxText(String checkboxText) {
        CheckboxText = checkboxText;
    }

    public boolean isCheckState() {
        return checkState;
    }

    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }


    public ListCheckBox(String checkboxText, boolean checkState){
        this.CheckboxText = checkboxText;
        this.checkState = checkState;

    }

}
