package duti.com.droidtool.api;

/**
 * Created by imrose on 4/27/2018.
 */

public class ApiPacketRequest {
    public int UserId;
    public String TableName;
    public int TotalRecord;
    public int PageNo;
    public int PageSize;
    public ApiPacket ApiPacket = new ApiPacket();

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

    public int getTotalRecord() {
        return TotalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        TotalRecord = totalRecord;
    }

    public int getPageNo() {
        return PageNo;
    }

    public void setPageNo(int pageNo) {
        PageNo = pageNo;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public ApiPacket getApiPacket() {
        return ApiPacket;
    }

    public void setApiPacket(ApiPacket apiPacket) {
        ApiPacket = apiPacket;
    }
}
