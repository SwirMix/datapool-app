package org.datapool.services;

import org.datapool.api.DatapoolAppControllerApi;
import org.datapool.api.Status;
import org.datapool.dto.DatapoolStrategy;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.Message;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;

import static org.datapool.Utils.cacheManagerResponseConverter;

public class DatapoolService {
    private Retrofit retrofit;
    private DatapoolAppControllerApi api;
    private String masterToken;

    public DatapoolService(String masterToken, Retrofit retrofit){
        this.retrofit = retrofit;
        this.masterToken = masterToken;
    }

    private DatapoolAppControllerApi getApi(){
        if (api == null){
            api = retrofit.create(DatapoolAppControllerApi.class);
        }
        return api;
    }

    public Status getHealth() throws IOException {
        return getApi().status().execute().body();
    }

    public InternalApiRequest getDataPoolItem(String project, String cache, Long key){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Call<InternalApiRequest> call = getApi().getDataFromCache(
                    masterToken,
                    project,
                    cache,
                    "export-task",
                    DatapoolStrategy.KEY,
                    key
            );
            InternalApiRequest object = call.execute().body();
            if (object!=null){
                if (object.isSuccess()){
                    result.setMessage(object.getMessage());
                    result.setSuccess(object.isSuccess());
                    result.setResult(object.getResult());
                    return cacheManagerResponseConverter(object);
                }
            } else {
                result.setMessage("Internal error.");
                result.setSuccess(false);
                result.setResult(new Message("Slave service error"));
                return result;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setCode(500);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    public InternalApiRequest getDataPage(String project, String cacheName, Long start, Long end){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Call<InternalApiRequest> call = getApi().getDataPage(masterToken, project, cacheName, start, end);
            InternalApiRequest object = call.execute().body();
            if (object!=null){
                if (object.isSuccess()){
                    result.setMessage(object.getMessage());
                    result.setSuccess(object.isSuccess());
                    result.setResult(object.getResult());
                    return cacheManagerResponseConverter(object);
                }
            } else {
                result.setMessage("Internal error.");
                result.setSuccess(false);
                result.setResult(new Message("Slave service error"));
                return result;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setCode(500);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    public InternalApiRequest getData(String project, String cacheName, DatapoolStrategy strategy, Long key, String consumer){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Call<InternalApiRequest> call = getApi().getDataFromCache(masterToken, project, cacheName, consumer, strategy, key);
            InternalApiRequest object = call.execute().body();
            if (object!=null){
                if (object.isSuccess()){
                    result.setMessage(object.getMessage());
                    result.setSuccess(object.isSuccess());
                    result.setResult(object.getResult());
                    return cacheManagerResponseConverter(object);
                }
            } else {
                result.setMessage("Internal error.");
                result.setSuccess(false);
                result.setResult(new Message("Slave service error"));
                return result;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setCode(500);
        result.setResult(new Message("Slave service error"));
        return result;
    }
}
