package rest;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.OkHttpClient;
import org.datapool.api.CacheCreatorControllerApi;
import org.datapool.api.InfoServiceRestControllerApi;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.model.CacheMetadata;
import org.datapool.model.CacheMetadataKey;
import org.datapool.model.CreateCacheRequest;
import org.datapool.model.InternalApiRequestObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.HashMap;

public class CacheCreatorControllerApiTest {
    private String project = "711bff9d-a873-4497-873f-fbfceb220071";
    private String url = "http://localhost:8083";
    private Retrofit retrofit;

    @BeforeTest
    public void initClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    @Test
    public void createRuntimeCache() throws IOException {
        String runtimeCacheName = "runtimeCache";
        CacheCreatorControllerApi cacheCreatorControllerApi = retrofit.create(CacheCreatorControllerApi.class);
        CreateCacheRequest request = new CreateCacheRequest();
        CacheMetadataKey cacheKey = new CacheMetadataKey();
        HashMap<String,String> columns = new HashMap<String,String>();
        columns.put("id", "text");
        cacheKey.setCacheName(runtimeCacheName);
        cacheKey.setProject(project);
        request.setCache(cacheKey);
        request.setCacheType(CreateCacheRequest.CacheTypeEnum.PERSISTENCE);
        request.setColumns(columns);
        Assert.assertEquals(cacheCreatorControllerApi.createCache(request).execute().code(), 200);
        InfoServiceRestControllerApi infoServiceRestControllerApi = retrofit.create(InfoServiceRestControllerApi.class);
        InternalApiRequest result = infoServiceRestControllerApi.getMetadata(project, runtimeCacheName).execute().body();
        LinkedTreeMap data = (LinkedTreeMap) result.getResult();
        LinkedTreeMap metadata = (LinkedTreeMap) data.get("cacheMetadata");
        String id = (String) metadata.get("cacheId");
        Assert.assertEquals(id, project + "_" + runtimeCacheName);
    }

    @Test
    public void createAndDeleteRuntimeCache() throws IOException {
        String runtimeCacheName = "runtimeCache";
        CacheCreatorControllerApi cacheCreatorControllerApi = retrofit.create(CacheCreatorControllerApi.class);
        CreateCacheRequest request = new CreateCacheRequest();
        CacheMetadataKey cacheKey = new CacheMetadataKey();
        HashMap<String,String> columns = new HashMap<String,String>();
        columns.put("id", "text");
        cacheKey.setCacheName(runtimeCacheName);
        cacheKey.setProject(project);
        request.setCache(cacheKey);
        request.setCacheType(CreateCacheRequest.CacheTypeEnum.PERSISTENCE);
        request.setColumns(columns);
        Assert.assertEquals(cacheCreatorControllerApi.createCache(request).execute().code(), 200);
        InfoServiceRestControllerApi infoServiceRestControllerApi = retrofit.create(InfoServiceRestControllerApi.class);
        InternalApiRequest result = infoServiceRestControllerApi.getMetadata(project, runtimeCacheName).execute().body();
        LinkedTreeMap data = (LinkedTreeMap) result.getResult();
        LinkedTreeMap metadata = (LinkedTreeMap) data.get("cacheMetadata");
        String id = (String) metadata.get("cacheId");
        Assert.assertEquals(id, project + "_" + runtimeCacheName);
        cacheCreatorControllerApi.deleteCache(project, runtimeCacheName).execute().body();
    }
}
