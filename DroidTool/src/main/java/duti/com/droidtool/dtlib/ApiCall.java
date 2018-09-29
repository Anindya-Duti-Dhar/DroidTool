package duti.com.droidtool.dtlib;


import android.os.Handler;
import android.text.TextUtils;

import duti.com.droidtool.DroidTool;
import duti.com.droidtool.R;
import duti.com.droidtool.api.ApiMaster;
import duti.com.droidtool.api.ApiResponse;
import duti.com.droidtool.api.FetchHistory;
import duti.com.droidtool.database.Repository;
import duti.com.droidtool.services.DataFetchService;
import duti.com.droidtool.services.DataSyncService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static duti.com.droidtool.config.Constants.mHasPaging;
import static duti.com.droidtool.config.Constants.mItemName;
import static duti.com.droidtool.config.Constants.mObject;
import static duti.com.droidtool.config.Constants.mShouldStore;
import static duti.com.droidtool.config.Constants.mTableName;

public class ApiCall {

    ApiMaster apiMaster;
    DroidTool dt;
    private onResponseListener responseListener = null;

    public ApiCall(DroidTool dt) {
        this.dt = dt;
        apiMaster = new ApiMaster().apiMaster(dt);
    }

    public interface onResponseListener {
        void onHttpResponse(ApiResponse data);
    }

    public void setResponseListener(onResponseListener listener) {
        this.responseListener = listener;
    }

    public void post(Call<ApiResponse> call) {
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dt.tools.printLog("API onResponse", response.toString());
                dt.tools.printLog("API Response Size", response.toString().getBytes().length + " byte");
                if (response.isSuccessful()) {
                    ApiResponse object = response.body();
                    dt.tools.printLog("API Json Response Size", object.toString().getBytes().length + " byte");
                    doneApiCall(object);
                } else {
                    doneApiCall(new ApiResponse());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dt.tools.printLog("API onFailure", t.getMessage());
                doneApiCall(new ApiResponse());
            }
        });
    }

    public void doneApiCall(ApiResponse object) {
        if (responseListener != null) {
            responseListener.onHttpResponse(object);
        }
    }

    public void syncRecord(final String tableName, final String itemName, final Class[] itemClass) {
        dt.alert.showWarning(dt.gStr(R.string.want_to_sync));
        dt.alert.setAlertListener(new SweetAlert.AlertListener() {
            @Override
            public void onAlertClick(boolean isCancel) {
                if (!isCancel) {
                    if (dt.tools.repoNotSyncCount(itemClass) < 1)
                        dt.msg(dt.gStr(R.string.no_data_message));
                    else syncDataToServer(tableName, itemName, itemClass);
                }
            }
        });
    }

    public void syncRecordWithOutLimit(final String tableName, final String itemName, final Class[] itemClass) {
        dt.alert.showWarning(dt.gStr(R.string.want_to_sync));
        dt.alert.setAlertListener(new SweetAlert.AlertListener() {
            @Override
            public void onAlertClick(boolean isCancel) {
                if (!isCancel) {
                    if (dt.tools.repoNotSyncCount(itemClass) < 1)
                        dt.msg(dt.gStr(R.string.no_data_message));
                    else syncDataWithoutLimitToServer(tableName, itemName, itemClass);
                }
            }
        });
    }

    public void syncDataToServer(final String tableName, final String itemName, final Class[] itemClass) {
        Handler handler = new Handler();
        if (dt.droidNet.hasConnection()) {
            if (!dt.tools.apiServiceRunning()) {
                dt.alert.showProgress(dt.gStr(R.string.sync_syncing_text));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dt.tools.repoNotSyncCount(itemClass) > 50)
                            dt.msg(dt.gStr(R.string.sync_50_by_50));
                        //Do after 2 seconds
                        dt.etc.keepDeviceScreenLightOn();
                        dt.activity.callService(DataSyncService.class, mTableName, tableName, mItemName, itemName);
                    }
                }, 2000);
            } else {
                dt.msg(dt.c.getString(R.string.another_service_running));
            }
        } else {
            // if there is no internet
            dt.alert.showError(dt.gStr(R.string.no_internet_title), dt.gStr(R.string.no_internet_message), dt.gStr(R.string.ok));
        }
    }

    public void syncDataWithoutLimitToServer(final String tableName, final String itemName, final Class[] itemClass) {
        Handler handler = new Handler();
        if (dt.droidNet.hasConnection()) {
            if (!dt.tools.apiServiceRunning()) {
                dt.alert.showProgress(dt.gStr(R.string.sync_syncing_text));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dt.etc.keepDeviceScreenLightOn();
                        dt.activity.callService(DataSyncService.class, mTableName, tableName, mItemName, itemName);
                    }
                }, 2000);
            } else {
                dt.msg(dt.c.getString(R.string.another_service_running));
            }
        } else {
            // if there is no internet
            dt.alert.showError(dt.gStr(R.string.no_internet_title), dt.gStr(R.string.no_internet_message), dt.gStr(R.string.ok));
        }
    }

    public boolean fetch(int pendingSyncCount, final String fetchTable, final int titleResID, final Object object, final Repository[] clearRepos) {

        if (pendingSyncCount > 0) {
            dt.alert.showError(dt.gStr(R.string.common_warning_title), dt.gStr(R.string.sync_request_text), dt.gStr(R.string.ok));
            return false;
        }

        dt.alert.showWarning(dt.gStr(R.string.fetch_warning_text));
        dt.alert.setAlertListener(new SweetAlert.AlertListener() {
            @Override
            public void onAlertClick(boolean isCancel) {
                if (!isCancel) {
                    dt.alert.showWarning(dt.gStr(R.string.fetch_delete_old_text));
                    dt.alert.setAlertListener(new SweetAlert.AlertListener() {
                        @Override
                        public void onAlertClick(boolean isCancel) {
                            if (isCancel)
                                fetchServerData(fetchTable, titleResID, object);
                            else {
                                for (int i = 0; i < clearRepos.length; i++)
                                    clearRepos[i].removeAll();

                                if (clearRepos.length > 0) {
                                    Repository<FetchHistory> repoHistory = new Repository<FetchHistory>(dt.c, new FetchHistory());
                                    if (repoHistory.getCountAgainstField(mTableName, fetchTable) > 0) {
                                        repoHistory.remove(mTableName + "='" + fetchTable + "'", null);
                                    }
                                    dt.pref.set(fetchTable, false);
                                }
                                fetchServerData(fetchTable, titleResID, object);
                            }
                        }
                    });
                }
            }
        });
        return true;
    }

    public void fetchServerData(final String tableName, final int titleResID, final Object object) {
        Handler handler = new Handler();
        if (dt.droidNet.hasConnection()) {
            if (!dt.tools.apiServiceRunning()) {
                dt.alert.showProgress(dt.gStr(R.string.fetch_fetching_text));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do after 2 seconds
                        dt.etc.keepDeviceScreenLightOn();
                        dt.activity.callService(DataFetchService.class, mTableName, tableName, mItemName, dt.gStr(titleResID), mObject, object, mShouldStore, true, mHasPaging, true);
                    }
                }, 2000);
            } else {
                dt.msg(dt.c.getString(R.string.another_service_running));
            }
        } else {
            // if there is no internet
            dt.alert.showError(dt.gStr(R.string.no_internet_title), dt.gStr(R.string.no_internet_message), dt.gStr(R.string.ok));
        }
    }

    public boolean genericFetch(String warningMsgRes, final String progressMessage, final String fetchTable, final int titleResID, final Object object, final Repository[] clearRepos, final boolean shouldStore) {
        if (!TextUtils.isEmpty(warningMsgRes)) {
            dt.alert.showWarning(warningMsgRes);
            dt.alert.setAlertListener(new SweetAlert.AlertListener() {
                @Override
                public void onAlertClick(boolean isCancel) {
                    if (!isCancel)
                        fetchGenericServerData(fetchTable, progressMessage, titleResID, object, clearRepos, shouldStore);
                }
            });
        } else {
            fetchGenericServerData(fetchTable, progressMessage, titleResID, object, clearRepos, shouldStore);
        }
        return true;
    }

    public void fetchGenericServerData(final String tableName, String progressMessage, final int titleResID, final Object object, final Repository[] clearRepos, final boolean shouldStore) {
        Handler handler = new Handler();
        if (dt.droidNet.hasConnection()) {
            if (!dt.tools.apiServiceRunning()) {
                dt.alert.showProgress(progressMessage);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do after 2 seconds
                        if (clearRepos.length > 0) {
                            for (int i = 0; i < clearRepos.length; i++) clearRepos[i].removeAll();
                        }
                        dt.pref.set(tableName, false);
                        dt.etc.keepDeviceScreenLightOn();
                        dt.activity.callService(DataFetchService.class, mTableName, tableName, mItemName, dt.gStr(titleResID), mObject, object, mShouldStore, shouldStore, mHasPaging, false);
                    }
                }, 2000);
            } else {
                dt.msg(dt.c.getString(R.string.another_service_running));
            }
        } else {
            // if there is no internet
            dt.alert.showError(dt.gStr(R.string.no_internet_title), dt.gStr(R.string.no_internet_message), dt.gStr(R.string.ok));
        }
    }

    public onPostCompleteListener postCompleteListener = null;

    // post ApiCall complete listener interface
    public interface onPostCompleteListener {
        void onPostComplete(ApiResponse data);
    }

    public void postApi(String progressText, String baseUrl, String tableName, Object object, boolean shouldStore, onPostCompleteListener listener) {
        this.postCompleteListener = listener;
        apiMaster.setApiCallFinishListener(new ApiMaster.onApiCallFinishListener() {
            @Override
            public void onApiCallFinish(ApiResponse data) {
                postCompleteListener.onPostComplete(data);
            }
        });
        if (dt.droidNet.hasConnection()) {
            dt.alert.showProgress(progressText);
            apiMaster.post(baseUrl, tableName, object, shouldStore);
        } else {
            // if there is no internet
            dt.alert.showError(dt.gStr(R.string.no_internet_title), dt.gStr(R.string.no_internet_message), dt.gStr(R.string.ok));
        }
    }

}
