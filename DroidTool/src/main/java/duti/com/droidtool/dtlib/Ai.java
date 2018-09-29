package duti.com.droidtool.dtlib;

import android.content.Context;

import duti.com.droidtool.DroidTool;


/**
 * Created by Duti on 5/28/2018.
 */

public class Ai {

    Context mContext;
    DroidTool dt;

    public Ai(Context context) {
        mContext = context;
        dt = new DroidTool(mContext);
    }

}
