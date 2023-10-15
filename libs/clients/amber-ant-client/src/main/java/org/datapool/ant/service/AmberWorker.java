package org.datapool.ant.service;

import com.opencsv.CSVWriter;
import org.datapool.ant.Utils;
import org.datapool.ant.jobs.ImportRequest;
import org.datapool.api.ClientFactory;
import org.datapool.api.DatapoolAppControllerApi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class AmberWorker implements Runnable{
    private AmberWorkerConfig config;
    private DatapoolAppControllerApi api;
    private ExecutorService executorService;

    public AmberWorker(AmberWorkerConfig cfg) throws IOException {
        this.config = cfg;
        this.api = ClientFactory.getDatapoolClient(config.getUrl());
        this.executorService = Executors.newFixedThreadPool(cfg.getThreads());
    }

    public void importDataToCSV() throws IOException, InterruptedException, ExecutionException {
        long startId = config.getTask().getStartId();
        long endId = config.getTask().getEndId();
        long cursor = 0;
        List<Future> futures = new ArrayList<>();
        CSVWriter writer = new CSVWriter(new FileWriter(config.getTask().getOutput()));
        do {
            CountDownLatch countDownLatch = new CountDownLatch(config.getThreads());
            while (countDownLatch.getCount() > 0){
                cursor = startId + config.getTask().getBatch();
                if (cursor < endId){
                    startId = importRequest(startId+1, countDownLatch, config.getTask().getBatch(), futures);
                } else {
                    startId = importRequest(startId+1, countDownLatch, endId - startId, futures);
                }
            }
            for (Future<List<Map<String, Object>>> future : futures){
                writeData(future.get(), writer);
                writer.flush();
            }
            futures.clear();
        } while (startId < endId);
        writer.flush();
        writer.close();
        executorService.shutdown();
    }

    private long importRequest(long startId, CountDownLatch countDownLatch, long batchSize, List<Future> futures){
        long endId = startId + batchSize;
        if (endId > config.getTask().getEndId()){
            endId = config.getTask().getEndId();
        }
        ImportRequest.RequestCfg config = new ImportRequest.RequestCfg();
        config.setEnd(endId);
        config.setStart(startId);
        config.setProject(this.config.getProject());
        config.setToken(this.config.getToken());
        config.setCacheName(this.config.getTask().getCacheName());
        ImportRequest request = new ImportRequest(config, api, this);
        futures.add(executorService.submit(request));
        countDownLatch.countDown();
        return endId;
    }

    public void writeData(List<Map<String, Object>> batch, CSVWriter writer){
        List<String[]> data = new ArrayList<>();
        for (Map<String, Object> item : batch){
            if (!item.isEmpty()){
                data.add(Utils.extractData(item.values()));
                //writer.writeNext(Utils.extractData(item.values()));
            }
        }
        writer.writeAll(data);
    }

    @Override
    public void run() {
        try {
            importDataToCSV();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
