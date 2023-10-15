package org.datapool.api;

import org.datapool.dto.DatapoolStrategy;
import org.datapool.dto.PutData;
import org.datapool.dto.commons.InternalApiRequest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DatapoolAppControllerApi {
    @GET("actuator/health")
    Call<Status> status();

    @GET("api/v1/datapool/{project}/{cacheName}")
    Call<InternalApiRequest> getDataFromCache(
            @retrofit2.http.Header("token") String token,
            @retrofit2.http.Path("project") String project,
            @retrofit2.http.Path("cacheName") String cacheName,
            @retrofit2.http.Query("consumer") String consumer,
            @retrofit2.http.Query("strategy") DatapoolStrategy strategy,
            @retrofit2.http.Query("key") Long key
    );

    @GET("/api/v1/datapool/data")
    Call<InternalApiRequest> getDataPage(
            @retrofit2.http.Header("token") String token,
            @retrofit2.http.Query("project") String project,
            @retrofit2.http.Query("cacheName") String cacheName,
            @retrofit2.http.Query("start") Long start,
            @retrofit2.http.Query("end") Long end
    );

    @GET("api/v1/datapool/{project}/{cacheName}/size")
    Call<InternalApiRequest> getCacheSize(
            @retrofit2.http.Header("token") String token,
            @retrofit2.http.Path("project") String project,
            @retrofit2.http.Path("cacheName") String cacheName
    );

    @POST("/api/v1/datapool/{project}/{cacheName}")
    Call<InternalApiRequest> postDataToCache(
            @retrofit2.http.Header("token") String token,
            @retrofit2.http.Path("project") String project,
            @retrofit2.http.Path("cacheName") String cacheName,
            @retrofit2.http.Body PutData postBody
    );

}
