package rest;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.datapool.api.DatapoolAppControllerApi;
import org.datapool.dto.DatapoolStrategy;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Map;

public class DatapoolRestTest {
    private String url = "http://192.168.0.8:8084/";
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
    public void test() throws IOException, InterruptedException {
       DatapoolAppControllerApi api =  retrofit.create(DatapoolAppControllerApi.class);
       String project = "711bff9d-a873-4497-873f-fbfceb220071";
       String[] cache = new String[]{"boardingPasses", "ticketFlights"};
       String strategy = "RANDOM";
       String token = "002115f0-3b5c-42a4-8c39-586e84d8ef59";
       String[] consumers = new String[]{"bob1"};
       for (int i = 0; i < 100_000; i++){
           Response response = api.getDataFromCache(
                   token,
                   project,
                   cache[(int) ((Math.random() * (cache.length - 0)) + 0)],
                   consumers[(int) ((Math.random() * (consumers.length - 0)) + 0)],
                   DatapoolStrategy.RANDOM,
                   100l
           ).execute().raw();
           //Thread.sleep(10);
       }

    }
}
