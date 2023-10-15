package org.datapool.victoria;

import com.google.gson.internal.LinkedTreeMap;
import retrofit2.Call;
import retrofit2.http.GET;

public interface VictoriaMetricsClient {
    @GET("/prometheus/api/v1/query_range")
    public Call<LinkedTreeMap> query(
            @retrofit2.http.Query("query") String query,
            @retrofit2.http.Query("start") String start,
            @retrofit2.http.Query("end") String end,
            @retrofit2.http.Query("step") String step
    );

    @GET("/api/v1/status/tsdb")
    public Call<LinkedTreeMap> health(
    );

}
