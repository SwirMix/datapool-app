package org.datapool.api;

import org.datapool.dto.commons.InternalApiRequest;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import org.datapool.model.SaveToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserTokenControllerApi {
  /**
   * 
   * 
   * @param token  (required)
   * @return Call&lt;String&gt;
   */
  @DELETE("api/v1/tokens/{token}")
  Call<InternalApiRequest> deleteToken(
    @Path("token") String token
  );

  /**
   * 
   * 
   * @param saveToken  (required)
   * @return Call&lt;String&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v1/tokens/")
  Call<InternalApiRequest> saveRemoteToken(
    @Body SaveToken saveToken
  );

  /**
   * 
   * 
   * @param token  (required)
   * @return Call&lt;String&gt;
   */
  @GET("api/v1/tokens/{token}")
  Call<InternalApiRequest> getToken(
    @Path("token") String token
  );

}
