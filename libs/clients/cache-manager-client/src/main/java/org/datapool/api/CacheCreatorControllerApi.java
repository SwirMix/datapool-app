package org.datapool.api;

import org.datapool.dto.commons.InternalApiRequest;
import retrofit2.Call;
import retrofit2.http.*;
import org.datapool.model.CacheStatus;
import org.datapool.model.CreateCacheRequest;
import org.datapool.model.InternalApiRequestCacheMetadata;

public interface CacheCreatorControllerApi {
  /**
   * 
   * 
   * @param cacheStatus  (required)
   * @return Call&lt;String&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v1/manager/cache/status")
  Call<InternalApiRequest> changeCacheState(
    @Body CacheStatus cacheStatus
  );

  /**
   * 
   * 
   * @param createCacheRequest  (required)
   * @return Call&lt;InternalApiRequestCacheMetadata&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v1/manager/cache/runtime")
  Call<InternalApiRequest> createCache(
    @Body CreateCacheRequest createCacheRequest
  );

  /**
   * 
   * 
   * @param project  (required)
   * @param cacheName  (required)
   * @return Call&lt;InternalApiRequestObject&gt;
   */
  @DELETE("api/v1/manager/cache/{project}/{cacheName}")
  Call<InternalApiRequest> deleteCache(
          @Path("project") String project, @Path("cacheName") String cacheName
  );

}
