package duti.com.droidtool.api;

import android.content.Context;

import java.util.HashMap;
import java.util.List;


import duti.com.droidtool.DroidTool;
import duti.com.droidtool.database.Repository;
import duti.com.droidtool.dtlib.ApiCall;
import duti.com.droidtool.handlers.FetchPostHandler;
import retrofit2.Call;

import static duti.com.droidtool.config.Constants.mApplicationType;
import static duti.com.droidtool.config.Constants.mBaseUrl;
import static duti.com.droidtool.config.Constants.mPageNo;
import static duti.com.droidtool.config.Constants.mRecordId;
import static duti.com.droidtool.config.Constants.mServerPageSize;
import static duti.com.droidtool.config.Constants.mSyncQueryWhereClause;
import static duti.com.droidtool.config.Constants.mTableName;
import static duti.com.droidtool.config.Constants.mUserId;


public class ApiMaster<T> {

    public DroidTool dt;
    int mCurrentPage = 1;

    // get Api Master instance to do make api call
    public ApiMaster apiMaster(Context context) {
        dt = new DroidTool(context);
        return this;
    }

    // get Api Master instance to do make api call
    public ApiMaster apiMaster(DroidTool droidTool) {
        dt = droidTool;
        return this;
    }

    //******************** Sync Api Call **************************//

    HashMap<String, Integer> syncResponseCountMap = new HashMap<>();
    HashMap<String, Integer> syncSuccessCountMap = new HashMap<>();

    // make suitable object for sync and call api
    public void SyncApiCall(String baseUrl, String tableName, Object object) {

        ApiPacketRequest apiPacketRequest = new ApiPacketRequest();
        if (object != null) apiPacketRequest.getApiPacket().setPacket(object);
        apiPacketRequest.setTableName(tableName);

        dt.tools.printJson(tableName, apiPacketRequest);

        dt.api.post(ApiClient.getClient(baseUrl).create(ApiInterface.class).syncPacket(mApplicationType, apiPacketRequest));
    }

    // register sync response listener
    public void SyncApiCallResponse(final Repository[] repos, final int listLength) {

        // init hash map to track response count from server
        syncResponseCountMap.put(repos[0].mTableName, 0);
        // init hash map to track success sync count from server
        syncSuccessCountMap.put(repos[0].mTableName, 0);

        // response listener
        dt.api.setResponseListener(new ApiCall.onResponseListener() {
            @Override
            public void onHttpResponse(ApiResponse data) {

                dt.tools.printJson(repos[0].mTableName, data);  // print json object in log cat

                incrementResponseCountForSync(repos[0].mTableName);

                if (data.isSuccess()) {
                    // update sync status after successful response from server
                    updateSyncStatus(repos, data, listLength);
                } else {
                    // call method to stop sync process
                    stopSync(repos[0].mTableName, data, listLength);
                }

            }
        });
    }

    // register response listener
    public void SyncApiCallResponse(final String tableName) {
        dt.api.setResponseListener(new ApiCall.onResponseListener() {
            @Override
            public void onHttpResponse(ApiResponse data) {
                dt.tools.printJson(tableName, data);
                if (data.isSuccess()) {
                    data.setSuccess(true);
                    data.setMessage("Success");
                    notifyApiCallComplete(data);
                } else {
                    data.setSuccess(false);
                    data.setMessage("Failure");
                    notifyApiCallComplete(data);
                }
            }
        });
    }

    // update sync status after successful response from server
    public void updateSyncStatus(Repository[] repos, ApiResponse data, int listLength) {

        // update master table
        repos[0].updateMasterSyncStatus(data.RecordId, data.ServerRecordId);

        // update details table
        if (repos.length > 1) {
            for (int i = 1; i < repos.length; i++) {
                repos[i].updateDetailsSyncStatus(data.RecordId);
            }
        }

        incrementSuccessCountForSync(repos[0].mTableName);

        // call method to stop sync process
        stopSync(repos[0].mTableName, data, listLength);
    }

    // add response count into hash map to finish sync operation in proper time
    private void stopSync(String tableName, ApiResponse data, int listLength) {

        // check if count equals to list length
        if (syncResponseCountMap.get(tableName) >= listLength) {

            int successSyncCount = syncSuccessCountMap.get(tableName);

            if ((successSyncCount < listLength) && (successSyncCount > 0)) {
                data.setSuccess(true);
                data.setMessage("Partial");
            } else if (successSyncCount == 0) {
                data.setSuccess(false);
                data.setMessage("Failure");
            } else {
                data.setSuccess(true);
                data.setMessage("Success");
            }

            // reset count for sync table
            syncResponseCountMap.put(tableName, 0);

            syncSuccessCountMap.put(tableName, 0);

            // complete whole operation
            notifyApiCallComplete(data);
        }
    }

    private void incrementSuccessCountForSync(String tableName) {
        // second time init count
        int responseCount = 1;
        // increment value of count
        if (syncSuccessCountMap.get(tableName) != null)
            responseCount = syncSuccessCountMap.get(tableName) + 1;
        // add count into hash map
        syncSuccessCountMap.put(tableName, responseCount);
    }

    private void incrementResponseCountForSync(String tableName) {
        // second time init count
        int responseCount = 1;
        // increment value of count
        if (syncResponseCountMap.get(tableName) != null)
            responseCount = syncResponseCountMap.get(tableName) + 1;
        // add count into hash map
        syncResponseCountMap.put(tableName, responseCount);
    }

    // short cut method to make query for sync
    public String getId(long id) {
        return mRecordId + " = " + Long.toString(id);
    }

    public String getNotSyncedId(long id) {
        return mRecordId + " = " + Long.toString(id) + " AND " + mSyncQueryWhereClause;
    }


    //*********************** Generic Api Call using POST method ******************//

    // make generic post method call with target object
    public void post(String baseUrl, final String tableName, Object object, final boolean shouldStore) {

        ApiPacketRequest apiPacketRequest = new ApiPacketRequest();
        if (object != null) apiPacketRequest.getApiPacket().setPacket(object);
        apiPacketRequest.setTableName(tableName);
        apiPacketRequest.setUserId(dt.pref.getInt(mUserId));

        dt.tools.printJson(tableName, apiPacketRequest);

        dt.api.setResponseListener(new ApiCall.onResponseListener() {
            @Override
            public void onHttpResponse(ApiResponse data) {

                dt.tools.printJson(tableName, data);

                if (data.isSuccess()) {
                    // send data to handler to insert into database
                    if (shouldStore) {

                        T packet = null;
                        List<T> packetList = null;

                        if ((data.getApiPacket().getPacket() == null) && (data.getApiPacket().getPacketList() == null)) {
                            data.setSuccess(false);
                            data.setMessage("Failure");
                            notifyApiCallComplete(data);
                        } else if ((data.getApiPacket().getPacket() != null) && (data.getApiPacket().getPacketList() == null)) {
                            packet = (T) data.getApiPacket().getPacket();
                            storeData(data, tableName, packetList, packet);
                        } else if ((data.getApiPacket().getPacket() == null) && (data.getApiPacket().getPacketList() != null)) {
                            packetList = data.getApiPacket().getPacketList();
                            if (packetList.size() > 0) {

                                storeData(data, tableName, packetList, packet);
                            } else {
                                data.setMessage("Empty");
                                notifyApiCallComplete(data);
                            }
                        }
                    } else {
                        if (data.isSuccess()) data.setMessage("Success");
                        else data.setMessage("Failure");
                        notifyApiCallComplete(data);
                    }
                } else {
                    data.setMessage("Failure");
                    notifyApiCallComplete(data);
                }
            }
        });

        dt.api.post(ApiClient.getClient(baseUrl).create(ApiInterface.class).getPacketFromServer(mApplicationType, apiPacketRequest));
    }


    private void storeData(ApiResponse data, String tableName, List<T> packetList, T packet) {
        int isSaved = new FetchPostHandler().OnDataReceived(dt.c, tableName, packetList, packet);
        if (isSaved < 1) {
            data.setSuccess(false);
            data.setMessage("NotSaved");
        } else {
            data.setSuccess(true);
            data.setMessage("Saved");
        }
        notifyApiCallComplete(data);
    }


    //********************* Fetch Api Call ************************//

    HashMap<String, Integer> fetchResponseCountMap = new HashMap<>();
    HashMap<String, Integer> fetchDownloadedPageCountMap = new HashMap<>();
    int requiredPage = 0;

    // first call on fetch API
    public void fetchApiCall(String baseUrl, final String tableName, final Object object) {
        // first time init count
        fetchResponseCountMap.put(tableName, 0);
        fetchDownloadedPageCountMap.put(tableName, 0);
        // call api
        FetchApiCall(baseUrl, tableName, object);
    }

    boolean isPageCalculated = false;

    // make suitable object and call api
    public void FetchApiCall(final String baseUrl, final String tableName, final Object object) {

        ApiPacketRequest apiPacketRequest = new ApiPacketRequest();
        apiPacketRequest.setPageNo(mCurrentPage);
        apiPacketRequest.setPageSize(mServerPageSize);
        apiPacketRequest.setTableName(tableName);
        apiPacketRequest.setUserId(dt.pref.getInt(mUserId));
        if (object != null) apiPacketRequest.getApiPacket().setPacket(object);

        dt.tools.printJson(tableName, apiPacketRequest);

        String jsonString = apiPacketRequest.toString();
        dt.tools.printLog("API Json Request Size", jsonString.getBytes().length + " byte");

        // response listener to get packet from server
        dt.api.setResponseListener(new ApiCall.onResponseListener() {
            @Override
            public void onHttpResponse(ApiResponse data) {
                incrementResponseCountForFetch(tableName);
                dt.tools.printJson(tableName, data);
                if (data.isSuccess()) {
                    if (!isPageCalculated) {
                        int totalRecord = 0;
                        Repository<FetchHistory> repository = new Repository<FetchHistory>(dt.c, new FetchHistory());
                        int repoCount = ((int) repository.getRecordCount("TableName ='" + tableName + "' and IsFetched = 1", 0)) * 10;
                        if (repoCount < data.getTotalRecord())
                            totalRecord = data.getTotalRecord() - repoCount;
                        requiredPage = (totalRecord / mServerPageSize);
                        if ((totalRecord % mServerPageSize) != 0) requiredPage++;
                        isPageCalculated = true;
                    }
                    onFetchSuccess(baseUrl, data, tableName, object);
                } else {
                    stopFetch(baseUrl, data, tableName, object);
                }
            }
        });

        if (!IsAlreadyFetched(tableName, mCurrentPage))
            dt.api.post((Call) ApiClient.getClient(baseUrl).create(ApiInterface.class).getPacketFromServer(mApplicationType, apiPacketRequest));
        else {
            if (mCurrentPage <= new Repository<FetchHistory>(dt.c, new FetchHistory()).getRecordMaxValue("PageNo")) {
                mCurrentPage++;
                FetchApiCall(baseUrl, tableName, object);
            }
        }
    }

    private void onFetchSuccess(String baseUrl, ApiResponse data, String tableName, Object object) {
        List<T> packetList = null;
        int bSaved = 0;
        if (data.getApiPacket().getPacketList() != null) {
            packetList = data.getApiPacket().getPacketList();
            if (packetList.size() > 0) {
                bSaved = new FetchPostHandler().OnDataReceived(dt.c, tableName, packetList, null);
                if (bSaved == 1) incrementDownloadCountForFetch(tableName);
                AddUpdateFetchTable(tableName, data.getPageNo(), bSaved);
                stopFetch(baseUrl, data, tableName, object);
            } else {
                data.setMessage("Empty");
                stopFetch(baseUrl, data, tableName, object);
            }
        } else {
            stopFetch(baseUrl, data, tableName, object);
        }
    }

    // check if current page already fetched or not
    private boolean IsAlreadyFetched(String tableName, int pageNumber) {
        boolean isFetched = false;
        Repository<FetchHistory> repo = new Repository<FetchHistory>(dt.c, new FetchHistory());
        FetchHistory[] fetchHistories = (FetchHistory[]) repo.get(
                mTableName + "='" + tableName + "'" + " and " + mPageNo + "=" + pageNumber, "", FetchHistory[].class);
        if (fetchHistories.length > 0) {
            if (fetchHistories[0].isFetched() == 1) isFetched = true;
        }
        return isFetched;
    }

    // after successful dump page add this page int fetch history table
    private void AddUpdateFetchTable(String tableName, int pageNumber, int isFetched) {
        Repository<FetchHistory> repository = new Repository<FetchHistory>(dt.c, new FetchHistory());
        FetchHistory fetchHistory = new FetchHistory();
        fetchHistory.setTableName(tableName);
        fetchHistory.setPageNo(pageNumber);
        fetchHistory.setFetched(isFetched);
        fetchHistory.setUserId(dt.pref.getInt(mUserId));

        if (repository.isRecordExists(mTableName + "='" + tableName + "'" + " and " + mPageNo + "=" + pageNumber))
            repository.update(fetchHistory, mTableName + "= '?' and " + mPageNo + "= ?", new String[]{tableName, String.valueOf(pageNumber)});
        else repository.add(fetchHistory);
    }

    private void stopFetch(String baseUrl, ApiResponse data, String tableName, Object object) {
        // check if api response count equals to required page count
        if (fetchResponseCountMap.get(tableName) >= requiredPage) {
            // reset response count for fetch table
            fetchResponseCountMap.put(tableName, 0);
            // reset incremental requested page count
            mCurrentPage = 1;
            // number of successfully saved page
            int storedPageCount = fetchDownloadedPageCountMap.get(tableName);
            // if required page count is more than 1
            if (requiredPage > 1) {
                // if successfully saved page count is less than incremental requested page count and more than equals 1
                if ((storedPageCount < requiredPage) && (storedPageCount >= 1)) {
                    data.setSuccess(false);
                    data.setMessage("Partial");
                }
                // if successfully saved page count is less than 1
                else if (storedPageCount < 1) {
                    data.setSuccess(false);
                    data.setMessage("Failure");
                } else {
                    data.setSuccess(true);
                    data.setMessage("Success");
                }
            }
            // if required page count is equals to 1
            else if (requiredPage == 1) {
                // if successfully saved page count is less than 1
                if (storedPageCount < 1) {
                    // if data list from server is empty
                    if (data.getMessage() != null) {
                        if (data.getMessage().equals("Empty")) {
                            data.setSuccess(true);
                        } else {
                            data.setSuccess(false);
                            data.setMessage("Failure");
                        }
                    } else {
                        data.setSuccess(false);
                        data.setMessage("Failure");
                    }
                } else {
                    data.setSuccess(true);
                    data.setMessage("Success");
                }
            } else {
                // if data list from server is empty
                if (data.getMessage() != null) {
                    if (data.getMessage().equals("Empty")) {
                        data.setSuccess(true);
                    } else {
                        data.setSuccess(false);
                        data.setMessage("Failure");
                    }
                } else {
                    data.setSuccess(false);
                    data.setMessage("Failure");
                }
            }
            // reset successfully saved page count
            fetchDownloadedPageCountMap.put(tableName, 0);
            // reset boolean
            isPageCalculated = false;
            // complete whole operation
            notifyApiCallComplete(data);
        } else {
            // increment requested page count
            mCurrentPage++;
            // recursive call fetch api
            FetchApiCall(baseUrl, tableName, object);
        }
    }

    private void incrementResponseCountForFetch(String tableName) {
        // second time init count
        int responseCount = 1;
        // increment value of count
        if (fetchResponseCountMap.get(tableName) != null)
            responseCount = fetchResponseCountMap.get(tableName) + 1;
        // add count into hash map
        fetchResponseCountMap.put(tableName, responseCount);
    }

    private void incrementDownloadCountForFetch(String tableName) {
        // second time init count
        int responseCount = 1;
        // increment value of count
        if (fetchDownloadedPageCountMap.get(tableName) != null)
            responseCount = fetchDownloadedPageCountMap.get(tableName) + 1;
        // add count into hash map
        fetchDownloadedPageCountMap.put(tableName, responseCount);
    }

    //********************* Api Master common Listener ***************//

    // ApiCall finish listener variable
    public onApiCallFinishListener myApiCallFinishListener = null;

    // ApiCall finish listener interface
    public interface onApiCallFinishListener {
        void onApiCallFinish(ApiResponse data);
    }

    // ApiCall finish response listener
    public void setApiCallFinishListener(onApiCallFinishListener listener) {
        this.myApiCallFinishListener = listener;
    }

    // after final execution
    public void notifyApiCallComplete(ApiResponse data) {
        if (myApiCallFinishListener != null) {
            myApiCallFinishListener.onApiCallFinish(data);
        }
    }

}

//get table name from back index
//obj.getClass().getName().substring(obj.getClass().getName().lastIndexOf('.') + 1;// obj.getClass().getName().length());
