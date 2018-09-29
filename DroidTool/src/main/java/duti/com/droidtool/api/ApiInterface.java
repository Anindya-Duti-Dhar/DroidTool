package duti.com.droidtool.api;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

import static duti.com.droidtool.config.Constants.mContentType;

public interface ApiInterface<T> {

    @POST("Sync/SendPacket")
    Call<ApiResponse> syncPacket(
            @Header(mContentType) String contentType,
            @Body Object syncPacketRequest);

    @POST("Fetch/GetPacket")
    Call<ApiResponse> getPacketFromServer(
            @Header(mContentType) String contentType,
            @Body ApiPacketRequest apiPacketRequest);

}
