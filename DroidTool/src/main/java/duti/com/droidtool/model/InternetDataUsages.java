package duti.com.droidtool.model;


public class InternetDataUsages {

    long RecordId;
    long ServerRecordId;
    String TodayAppDataUsages;
    String TotalAppDataUsages;
    String DataCollectionDate;
    String DataCollectionTime;
    int UserId;
    int Status;

    public long getRecordId() {
        return RecordId;
    }

    public void setRecordId(long recordId) {
        RecordId = recordId;
    }

    public long getServerRecordId() {
        return ServerRecordId;
    }

    public void setServerRecordId(long serverRecordId) {
        ServerRecordId = serverRecordId;
    }

    public String getTodayAppDataUsages() {
        return TodayAppDataUsages;
    }

    public void setTodayAppDataUsages(String todayAppDataUsages) {
        TodayAppDataUsages = todayAppDataUsages;
    }

    public String getTotalAppDataUsages() {
        return TotalAppDataUsages;
    }

    public void setTotalAppDataUsages(String totalAppDataUsages) {
        TotalAppDataUsages = totalAppDataUsages;
    }

    public String getDataCollectionDate() {
        return DataCollectionDate;
    }

    public void setDataCollectionDate(String dataCollectionDate) {
        DataCollectionDate = dataCollectionDate;
    }

    public String getDataCollectionTime() {
        return DataCollectionTime;
    }

    public void setDataCollectionTime(String dataCollectionTime) {
        DataCollectionTime = dataCollectionTime;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "InternetDataUsages{" +
                "RecordId=" + RecordId +
                ", ServerRecordId=" + ServerRecordId +
                ", TodayAppDataUsages='" + TodayAppDataUsages + '\'' +
                ", TotalAppDataUsages='" + TotalAppDataUsages + '\'' +
                ", DataCollectionDate='" + DataCollectionDate + '\'' +
                ", DataCollectionTime='" + DataCollectionTime + '\'' +
                ", UserId=" + UserId +
                ", Status=" + Status +
                '}';
    }
}
