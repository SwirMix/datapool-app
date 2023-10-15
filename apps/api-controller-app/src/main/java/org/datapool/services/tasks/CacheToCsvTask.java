package org.datapool.services.tasks;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.datapool.ExcludeCache;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.DataPoolHashKey;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.dto.storage.CacheToCsvRequest;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CacheToCsvTask implements Callable<InternalApiRequest> {
    private IgniteClient client;
    private CacheToCsvRequest request;
    private File targetFile;
    private Long cursor = 0l;
    private long batch = 100l;

    public CacheToCsvTask(IgniteClient client, CacheToCsvRequest request, File file){
        this.client = client;
        this.targetFile = file;
        this.request = request;
    }
    @Override
    public InternalApiRequest call() throws Exception {
        InternalApiRequest result = new InternalApiRequest();
        ClientCache<CacheMetadataKey, CacheMetadata> metadataClientCache = this.client.cache(ExcludeCache.METADATA_CACHE.name());
        CacheMetadata metadata = metadataClientCache.get(request.getCache());
        ClientCache<DataPoolHashKey, DataPoolHashData> dataCache = this.client.cache(metadata.getHashKey());
        ClientCache<Long, DataPoolItem> datapoolCache = this.client.cache(metadata.getCache().getPublicCacheName());
        CSVWriter writer = new CSVWriter(new FileWriter(targetFile));
        this.cursor = request.getStartId();
        long finalCursor = 0;
        List<String[]> lines = new ArrayList<>();
        try {
            DataPoolItem item = null;
            int hash = 0;
            writer.writeNext(extractData(metadata.getColumns().keySet()));
            do {
                lines.clear();
                finalCursor = cursor + batch;
                if (finalCursor > request.getEndId()){
                    finalCursor = request.getEndId();
                }
                Map<Long, DataPoolItem> items = datapoolCache.getAll(buildKeysRequest(cursor,finalCursor));
                List<DataPoolHashData> dataItems = getDataFromHashCache(items, dataCache);
                for (DataPoolHashData data : dataItems){
                    lines.add(extractData(data.getData().values()));
                }
                writer.writeAll(lines);
                cursor = finalCursor;
                writer.flush();
            } while (this.cursor < request.getEndId());
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        client.close();
        return result;
    }

    private Set<Long> buildKeysRequest(Long start, Long end){
        HashSet<Long> keys = new HashSet<>();
        for (long index = start; index < end; index++){
            keys.add(index);
        }
        return keys;
    }

    private List<DataPoolHashData> getDataFromHashCache(Map<Long, DataPoolItem> items, ClientCache<DataPoolHashKey, DataPoolHashData> dataCache){
        ArrayList<DataPoolHashData> results = new ArrayList<>();
        for (DataPoolItem item : items.values()){
            results.add(dataCache.get(new DataPoolHashKey(item.getHash())));
        }
        return results;
    }

    private String[] extractData(Collection data){
        ArrayList values = new ArrayList();
        for (Object value : data){
            String finalStr = "";
            if (value instanceof String){
                finalStr = ((String) value).replace("\"\"", "\"");
            } else if (value instanceof Long){
                finalStr = Long.toString((Long) value);
            } else if (value instanceof Double){
                finalStr = Double.toString((double) value);
            } else if (value instanceof Float){
                finalStr = Float.toString((Float) value);
            }
            values.add(finalStr);
        }
        return (String[]) values.toArray(new String[0]);
    }
}
