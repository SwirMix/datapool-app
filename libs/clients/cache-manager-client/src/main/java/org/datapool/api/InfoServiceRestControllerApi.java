package org.datapool.api;

import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.model.UpdateCursorRequest;
import retrofit2.Call;
import retrofit2.http.*;
import org.datapool.model.InternalApiRequestObject;
import java.util.Map;

public interface InfoServiceRestControllerApi {
  /**
   * @return Call&lt;InternalApiRequestObject&gt;
   */
  @POST("api/v1/update/cursor")
  Call<InternalApiRequest> updateCursor(@Body UpdateCursorRequest request);

  @GET("api/v1/{project}/{cacheName}")
  Call<InternalApiRequest> getMetadata(
          @Path("project") String project, @Path("cacheName") String cacheName
  );

  /**
   * 
   * 
   * @return Call&lt;Map&lt;String, Object&gt;&gt;
   */
  @GET("api/v1/projects")
  Call<Map<String, Object>> getProject();
    

  /**
   * 
   * 
   * @param project  (required)
   * @return Call&lt;InternalApiRequestObject&gt;
   */
  @GET("api/v1/{project}")
  Call<InternalApiRequest> getProjectCache(
    @Path("project") String project
  );

}
