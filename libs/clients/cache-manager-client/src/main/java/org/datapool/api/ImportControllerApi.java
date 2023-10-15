package org.datapool.api;

import org.datapool.dto.commons.InternalApiRequest;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import org.datapool.model.CreateDefaultJdbcCache;
import org.datapool.model.CreateJdbcCacheRequest;
import org.datapool.model.InternalApiRequestObject;

public interface ImportControllerApi {
  /**
   * 
   * 
   * @param createJdbcCacheRequest  (required)
   * @return Call&lt;InternalApiRequestObject&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v1/import/jdbc/cache")
  Call<InternalApiRequest> createJdbcCache(
    @Body CreateJdbcCacheRequest createJdbcCacheRequest
  );

  @Headers({
          "Content-Type:application/json"
  })
  @POST("api/v1/import/jdbc/reload")
  Call<InternalApiRequest> reloadJdbcCache(
          @Body CreateJdbcCacheRequest createJdbcCacheRequest
  );

  /**
   * 
   * 
   * @param createDefaultJdbcCache  (required)
   * @return Call&lt;InternalApiRequestObject&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v1/import/default/cache")
  Call<InternalApiRequest> createJdbcCache(
    @Body CreateDefaultJdbcCache createDefaultJdbcCache
  );

}
