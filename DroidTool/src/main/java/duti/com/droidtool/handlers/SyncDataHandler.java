package duti.com.droidtool.handlers;

import java.util.Arrays;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.api.ApiMaster;
import duti.com.droidtool.database.Repository;
import duti.com.droidtool.model.crash.CrashInfo;

import static duti.com.droidtool.config.Constants.mBaseUrl;
import static duti.com.droidtool.config.Constants.mSyncQueryOrderBy;
import static duti.com.droidtool.config.Constants.mSyncQueryWhereClause;

public class SyncDataHandler {

    public interface onSyncRecord {
        boolean onSynced(DroidTool droidTool, ApiMaster api, String tableName);
    }

    private onSyncRecord onSyncRecord = null;

    public void setSyncRecordListener(onSyncRecord onSyncRecord) {
        this.onSyncRecord = onSyncRecord;
    }

    public boolean syncRecord(ApiMaster api, String tableName) {
        ApiMaster apiMaster = api;
        DroidTool dt = apiMaster.dt;
        boolean isSend = false;
        if(tableName.equals("CrashInfo")){
            Repository<CrashInfo> repo = new Repository<CrashInfo>(dt.c, new CrashInfo());
            final CrashInfo[] master = (CrashInfo[]) repo.get(mSyncQueryWhereClause, mSyncQueryOrderBy, CrashInfo[].class);
            apiMaster.SyncApiCallResponse(new Repository[]{repo}, master.length);
            for (int i = 0; i < master.length; i++) {
                apiMaster.SyncApiCall(dt.pref.getString(mBaseUrl), tableName, master[i]);
            }
            isSend = true;
        } else {
            if(onSyncRecord!=null) isSend = onSyncRecord.onSynced(dt, api, tableName);
        }
        return isSend;
    }

}