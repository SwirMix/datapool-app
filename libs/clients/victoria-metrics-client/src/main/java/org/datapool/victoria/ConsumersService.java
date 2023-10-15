package org.datapool.victoria;

import com.google.gson.internal.LinkedTreeMap;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.victoria.dto.ConsumerCountData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConsumersService {
    private VictoriaMetricsClient client;

    public boolean getHealth() throws IOException {
        boolean result = false;
        try {
            String status = (String) client.health().execute().body().get("status");
            if (status.equals("success")) return true;
        } catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public ConsumersService(String server){
        this.client = ClientFactory.getVictoriaMetricsClient(server);
    }
    public InternalApiRequest<List<ConsumerCountData>> getConsumersDataByRange(
            String startTime, String endTime, String project, String cache
    ){

        String query = String.format("sum_over_time(delta(cache_consumer_total{project='%s', cache='%s'}))", project, cache);
        InternalApiRequest<List<ConsumerCountData>> result = new InternalApiRequest<>();
        result.setResult(new ArrayList<ConsumerCountData>());

        String st = startTime.substring(0, startTime.length()-3) + "." + startTime.substring(startTime.length()-3, startTime.length());
        String end = endTime.substring(0, endTime.length()-3) + "." +endTime.substring(endTime.length()-3, endTime.length());
        try {
            LinkedTreeMap response = client.query(
                    query,
                    startTime + ".000",
                    endTime + ".000",
                    "60s"
            ).execute().body();
            ArrayList<LinkedTreeMap> data = (ArrayList) ((LinkedTreeMap)response.get("data")).get("result");
            for (LinkedTreeMap item : data){
                ConsumerCountData consumer = new ConsumerCountData();
                consumer.setProject((String) ((LinkedTreeMap) item.get("metric")).get("project"));
                consumer.setConsumer((String) ((LinkedTreeMap) item.get("metric")).get("consumer"));
                consumer.setCache((String) ((LinkedTreeMap) item.get("metric")).get("cache"));
                consumer.setStrategy((String) ((LinkedTreeMap) item.get("metric")).get("strategy"));

                List<ArrayList> series = (ArrayList) item.get("values");
                series.forEach(datanote -> {
                    consumer.incrementCount(Double.parseDouble((String) datanote.get(1)));
                });
                result.getResult().add(consumer);
            }
            double sum = 0.0;
            for (ConsumerCountData consumer : result.getResult()){
                sum += consumer.getCount();
            }
            for (ConsumerCountData consumer : result.getResult()){
                consumer.setPercentage((consumer.getCount()/sum)*100);
            }
            result.setCode(200);
            result.setMessage("OK");
            result.setSuccess(true);
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal Error");
        }
        return result;
    }

    public InternalApiRequest<List<ConsumerCountData>> getConsumersDataByProjectAndRange(
            String startTime, String endTime, String project
    ){
        String query = String.format("sum_over_time(delta(cache_consumer_total{project='%s'}))", project);
        InternalApiRequest<List<ConsumerCountData>> result = new InternalApiRequest<>();
        result.setResult(new ArrayList<ConsumerCountData>());

        String st = startTime.substring(0, startTime.length()-3) + "." + startTime.substring(startTime.length()-3, startTime.length());
        String end = endTime.substring(0, endTime.length()-3) + "." +endTime.substring(endTime.length()-3, endTime.length());
        try {
            LinkedTreeMap response = client.query(
                    query,
                    startTime + ".000",
                    endTime + ".000",
                    "60s"
            ).execute().body();
            ArrayList<LinkedTreeMap> data = (ArrayList) ((LinkedTreeMap)response.get("data")).get("result");
            for (LinkedTreeMap item : data){
                ConsumerCountData consumer = new ConsumerCountData();
                consumer.setProject((String) ((LinkedTreeMap) item.get("metric")).get("project"));
                consumer.setConsumer((String) ((LinkedTreeMap) item.get("metric")).get("consumer"));
                consumer.setCache((String) ((LinkedTreeMap) item.get("metric")).get("cache"));
                consumer.setStrategy((String) ((LinkedTreeMap) item.get("metric")).get("strategy"));
                List<ArrayList> series = (ArrayList) item.get("values");
                series.forEach(datanote -> {
                    consumer.incrementCount(Double.parseDouble((String) datanote.get(1)));
                });
                result.getResult().add(consumer);
            }
            double sum = 0.0;
            for (ConsumerCountData consumer : result.getResult()){
                sum += consumer.getCount();
            }
            for (ConsumerCountData consumer : result.getResult()){
                consumer.setPercentage((consumer.getCount()/sum)*100);
            }
            result.setCode(200);
            result.setMessage("OK");
            result.setSuccess(true);
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal Error");
        }
        return result;
    }

    public InternalApiRequest<List<DataPoolItem>> getDatapoolItem(String startTime, String endTime, String project, String cache, String consumer){
        String query = String.format(
                "(count by (consumer, project, cacheId, hash, key) (count_over_time(consumer_size{project='%s', cacheId='%s', consumer='%s'})))",
                project,
                cache,
                consumer
        );
        InternalApiRequest<List<DataPoolItem>> result = new InternalApiRequest<>();
        result.setResult(new ArrayList<DataPoolItem>());
        Double sum = 0.0;
        try {
            LinkedTreeMap response = client.query(
                    query,
                    startTime + ".000",
                    endTime + ".000",
                    "60s"
            ).execute().body();
            ArrayList<LinkedTreeMap> data = (ArrayList) ((LinkedTreeMap)response.get("data")).get("result");
            for (LinkedTreeMap item : data){
                DataPoolItem dataPoolItem = new DataPoolItem();
                LinkedTreeMap row = (LinkedTreeMap) item.get("metric");
                dataPoolItem.setKey(Long.parseLong(((String)row.get("key"))));
                result.getResult().add(dataPoolItem);
            }
            result.setCode(200);
            result.setMessage("OK");
            result.setSuccess(true);
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal Error");
        }
        return result;
    }
}
