package duti.com.droidtool.handlers;

import android.content.Context;

import java.util.List;

import duti.com.droidtool.DroidTool;


public class FetchPostHandler<T> {

    public interface onDataReceived<T> {
        int onReceived(DroidTool droidTool, String tableName, List<T> list, T object);
    }

    private onDataReceived onDataReceived = null;

    public void setDataReceiveListener(onDataReceived onDataReceived) {
        this.onDataReceived = onDataReceived;
    }

    public int OnDataReceived(Context mContext, String tableName, List<T> list, T object) {
        DroidTool dt = new DroidTool(mContext);
        int isSaved = 1;
        if(onDataReceived!=null) isSaved = onDataReceived.onReceived(dt, tableName, list, object);
        return isSaved;
    }

}