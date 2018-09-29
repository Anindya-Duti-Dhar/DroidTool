package duti.com.droidtool.model.crash;

public class CrashInfo {

    long RecordId;
    long ServerRecordId;
    int UserId;
    String CrashDetails;
    int VersionCode;
    String VersionName;
    String DeviceName;
    String DeviceModel;
    String CrashTime;
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

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getCrashDetails() {
        return CrashDetails;
    }

    public void setCrashDetails(String crashDetails) {
        CrashDetails = crashDetails;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getDeviceModel() {
        return DeviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        DeviceModel = deviceModel;
    }

    public String getCrashTime() {
        return CrashTime;
    }

    public void setCrashTime(String crashTime) {
        CrashTime = crashTime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "CrashInfo{" +
                "RecordId=" + RecordId +
                ", ServerRecordId=" + ServerRecordId +
                ", UserId=" + UserId +
                ", CrashDetails='" + CrashDetails + '\'' +
                ", VersionCode='" + VersionCode + '\'' +
                ", VersionName='" + VersionName + '\'' +
                ", DeviceName='" + DeviceName + '\'' +
                ", DeviceModel='" + DeviceModel + '\'' +
                ", CrashTime='" + CrashTime + '\'' +
                ", Status=" + Status +
                '}';
    }
}
