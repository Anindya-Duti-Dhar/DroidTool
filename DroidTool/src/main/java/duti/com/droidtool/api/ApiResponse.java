package duti.com.droidtool.api;

/**
 * Created by imrose on 3/6/2018.
 */

public class ApiResponse {

    public boolean Success;
    public String Message;
    public long ServerRecordId;
    public long RecordId;
    public int TotalRecord;
    public int PageNo;
    public int PageSize;
    public ApiPacket ApiPacket = new ApiPacket();
    public int Status;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public long getServerRecordId() {
        return ServerRecordId;
    }

    public void setServerRecordId(long serverRecordId) {
        ServerRecordId = serverRecordId;
    }

    public long getRecordId() {
        return RecordId;
    }

    public void setRecordId(long recordId) {
        RecordId = recordId;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
