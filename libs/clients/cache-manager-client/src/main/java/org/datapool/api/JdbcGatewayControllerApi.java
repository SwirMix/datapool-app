package org.datapool.api;

import org.datapool.dto.commons.InternalApiRequest;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import org.datapool.model.PostgresJdbcProps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JdbcGatewayControllerApi {
  /**
   * 
   * 
   * @param postgresJdbcProps  (required)
   * @param tableName  (optional)
   * @return Call&lt;InternalApiRequestObject&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v1/default/tables/")
  Call<InternalApiRequest> getJdbcTables(
          @Body PostgresJdbcProps postgresJdbcProps, @Query("tableName") String tableName
  );

  @Headers({
          "Content-Type:application/json"
  })
  @POST("api/v1/default/tables/")
  Call<InternalApiRequest> getJdbcTables(
          @Body PostgresJdbcProps postgresJdbcProps
  );

}
