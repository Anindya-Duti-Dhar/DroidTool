package duti.com.droidtool.api;

/**
 * Created by imrose on 4/28/2018.
 */

public class FetchHistory {
    long RecordId;
    int UserId;
    String TableName;
    int PageNo;
    int IsFetched;

    public long getRecordId() {
        return RecordId;
    }

    public void setRecordId(long recordId) {
        RecordId = recordId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public int getPageNo() {
        return PageNo;
    }

    public void setPageNo(int pageNo) {
        PageNo = pageNo;
    }

    public int isFetched() {
        return IsFetched;
    }

    public void setFetched(int fetched) {
        IsFetched = fetched;
    }

    @Override
    public String toString() {
        return "FetchHistory{" +
                "RecordId=" + RecordId +
                ", UserId=" + UserId +
                ", TableName='" + TableName + '\'' +
                ", PageNo=" + PageNo +
                ", IsFetched=" + IsFetched +
                '}';
    }
}
