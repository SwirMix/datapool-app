package org.datapool.ant.jobs;

import org.datapool.ant.service.AmberWorker;
import org.datapool.api.DatapoolAppControllerApi;
import org.datapool.dto.commons.InternalApiRequest;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ImportRequest implements Callable<List<Map<String, Object>>> {

    private RequestCfg cfg;
    private DatapoolAppControllerApi api;
    private AmberWorker worker;

    public ImportRequest(RequestCfg cfg, DatapoolAppControllerApi api, AmberWorker amberWorker){
        this.api = api;
        this.worker = amberWorker;
        this.cfg = cfg;
    }
    @Override
    public List<Map<String, Object>> call() throws Exception {
        List<Map<String, Object>> page = new ArrayList<>();
        try {
            Response<InternalApiRequest> response = api.getDataPage(
                    cfg.getToken(),
                    cfg.getProject(),
                    cfg.getCacheName(),
                    cfg.getStart(),
                    cfg.getEnd()
            ).execute();
            if (response.code()==200){
                page = (List<Map<String, Object>>) response.body().getResult();
                //worker.writeData(page);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return page;
    }


    public static class RequestCfg {
        private long start;
        private long end;
        private String token;
        private String cacheName;
        private String project;

        public long getStart() {
            return start;
        }

        public RequestCfg setStart(long start) {
            this.start = start;
            return this;
        }

        public long getEnd() {
            return end;
        }

        public RequestCfg setEnd(long end) {
            this.end = end;
            return this;
        }

        public String getToken() {
            return token;
        }

        public RequestCfg setToken(String token) {
            this.token = token;
            return this;
        }

        public String getCacheName() {
            return cacheName;
        }

        public RequestCfg setCacheName(String cacheName) {
            this.cacheName = cacheName;
            return this;
        }

        public String getProject() {
            return project;
        }

        public RequestCfg setProject(String project) {
            this.project = project;
            return this;
        }
    }
}
