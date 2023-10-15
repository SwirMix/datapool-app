package org.datapool;

import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.DatapoolStrategy;
import org.datapool.dto.PutData;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.dto.metadata.Message;
import org.datapool.metrics.CacheConsumer;
import org.datapool.metrics.ConsumersMetrics;
import org.datapool.service.MainDataPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/datapool")
public class DatapoolRestController {
    private Logger logger = LoggerFactory.getLogger(DatapoolRestController.class);
    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private MainDataPoolService datapoolService;
    @Autowired
    private ConsumersMetrics consumersMetrics;

    @GetMapping("/{project}/{cacheName}/size")
    public ResponseEntity getSize(
            @PathVariable String project,
            @PathVariable String cacheName,
            @RequestHeader(required = false) String token
    ){
        tokenProcessing(token, project);
        InternalApiRequest result = new InternalApiRequest();
        try {
            CacheMetadataKey cache = new CacheMetadataKey(cacheName, project);
            result = datapoolService.getCacheSize(cache);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setResult(new Message("Internal error"));
            result.setMessage("Internal error");
        }
        if (!result.isSuccess()){
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/{project}/{cacheName}/metadata")
    public ResponseEntity getCacheMetadata(
            @PathVariable String project,
            @PathVariable String cacheName,
            @RequestHeader(required = false) String token
    ){
        if(tokenProcessing(token, project)){
            InternalApiRequest result = new InternalApiRequest();
            try {
                CacheMetadataKey cache = new CacheMetadataKey(cacheName, project);
                result = datapoolService.getCacheMetadata(new CacheMetadataKey(cacheName, project));
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setResult(new Message("Internal error"));
                result.setMessage("Internal error");
            }
            if (!result.isSuccess()){
                return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            return new ResponseEntity(new Message("Invalid token"), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{project}/{cacheName}")
    public ResponseEntity getRow(
        @PathVariable String project,
        @PathVariable String cacheName,
        @RequestParam(required = true) String consumer,
        @RequestParam(required = true) DatapoolStrategy strategy,
        @RequestParam(required = false) Long key,
        @RequestHeader(required = false) String token,
        HttpServletRequest request
    ){
        Long cacheKey = -1l;
        int hash = 0;
        if(tokenProcessing(token, project)){
            InternalApiRequest result = new InternalApiRequest();
            if (key == null){
                key = -1l;
            }
            try {
                CacheMetadataKey cache = new CacheMetadataKey(cacheName, project);
                result = getData(strategy, cache, key);
                if (strategy.name().equals(DatapoolStrategy.HASH.name())){
                    DataPoolHashData resRow = (DataPoolHashData) result.getResult();
                    DataPoolItem item = new DataPoolItem();
                    item.setData(resRow.getData());
                    item.setHash(item.getHash());
                    item.setKey(-1l);
                } else {
                    DataPoolItem item = (DataPoolItem) result.getResult();
                    hash = item.getHash();
                    cacheKey = item.getKey();
                }

            } catch (Exception e){
                incrementConsumerMetric(consumer, cacheName, project, strategy, false, hash, cacheKey);
                e.printStackTrace();
                result.setSuccess(false);
                result.setResult(new Message("Internal error"));
                result.setMessage("Internal error");
            }
            if (!result.isSuccess()){
                return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            incrementConsumerMetric(consumer, cacheName, project, strategy, true, hash, cacheKey);
            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            return new ResponseEntity(new Message("Invalid token"), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{project}/{cacheName}/{hash}")
    public ResponseEntity getDataByHash(
            @PathVariable String project,
            @PathVariable String cacheName,
            @PathVariable Integer hash,
            @RequestHeader(required = false) String token
    ){
        if(tokenProcessing(token, project)){
            InternalApiRequest result = new InternalApiRequest();
            try {
                result = datapoolService.getDataByHash(new CacheMetadataKey(cacheName, project), hash);
            } catch (Exception e){
                e.printStackTrace();
            }
            if (result.getMessage().equals("bad data")){
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            return new ResponseEntity(new Message("Invalid token"), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{project}/{cacheName}")
    public ResponseEntity postData(
            @RequestBody PutData body,
            @RequestHeader(required = false) String token
    ){
        if(tokenProcessing(token, body.getKey().getProject())){
            InternalApiRequest result = new InternalApiRequest();
            try {
                result = datapoolService.putData(body);
            } catch (Exception e){
                e.printStackTrace();
            }
            if (result.getMessage().equals("bad data")){
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            return new ResponseEntity(new Message("Invalid token"), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/data")
    public ResponseEntity getDataPage(
            @RequestParam String project,
            @RequestParam String cacheName,
            @RequestParam Long start,
            @RequestParam Long end,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();

        if(tokenProcessing(token, project)){
            try {
                CacheMetadataKey cache = new CacheMetadataKey(cacheName, project);
                result = datapoolService.getDataPage(new CacheMetadataKey(cacheName, project), start, end);
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setResult(new Message("Internal error"));
                result.setMessage("Internal error");
            }
            if (!result.isSuccess()){
                return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            return new ResponseEntity(new Message("Invalid token"), HttpStatus.FORBIDDEN);
        }
    }

    private InternalApiRequest getData(DatapoolStrategy strategy, CacheMetadataKey cache, long key){
        InternalApiRequest result = new InternalApiRequest();
        switch (strategy){
            case RANDOM:
                result = datapoolService.getRandomRow(cache);
                break;
            case SEQUENTIAL:
                result = datapoolService.getSequentialRow(cache);
                break;
            case KEY:
                result = datapoolService.getValueByKey(cache, key);
                break;
            case STACK:
                result = datapoolService.getAsStack(cache);
                break;
            case HASH:
                result = datapoolService.getDataByHash(cache, Math.toIntExact(key));
        }
        return result;
    }

    private void incrementConsumerMetric(String consumer, String cacheName, String project, DatapoolStrategy strategy, boolean status, int hash, Long key){
        CacheConsumer cacheConsumer = new CacheConsumer()
                .setConsumer(consumer)
                .setCache(new CacheMetadataKey(cacheName, project))
                .setStrategy(strategy)
                .setHash(hash)
                .setKey(key)
                .setSuccess(status);
        consumersMetrics.incrementConsumer(cacheConsumer);
    }

    private boolean tokenProcessing(String token, String project){
        logger.debug("TOKEN: " + token);
        if (configuration.isAuth()){
            if (datapoolService.checkToken(token, project) | checkMasterToken(token)){
                return true;
            } else return false;
        } else {
            return true;
        }
    }

    private boolean checkMasterToken(String token){
        if (configuration.getSecret().equals(token)){
            return true;
        } else {
            return false;
        }
    }
}
