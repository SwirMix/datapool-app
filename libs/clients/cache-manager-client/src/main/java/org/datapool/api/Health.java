package org.datapool.api;

import org.datapool.model.Status;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Health {
    @GET("actuator/health")
    Call<Status> status();
}
