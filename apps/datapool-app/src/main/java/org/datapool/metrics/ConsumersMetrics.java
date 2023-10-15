package org.datapool.metrics;

import com.google.gson.Gson;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.datapool.ApplicationConfiguration;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.InfluxPoint;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ConsumersMetrics {
    private Map<CacheConsumer, Counter> consumers = new HashMap<>();
    private CompositeMeterRegistry meterRegistry;

    public ConsumersMetrics(CompositeMeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementConsumer(CacheConsumer consumer){
        getCacheMetric(consumer).increment();
    }

    private Counter getCacheMetric(CacheConsumer key){
        if(consumers.containsKey(key)){
            return consumers.get(key);
        } else {
            Counter counter = Counter
                    .builder("cache_consumer")
                    .tag("project", key.getCache().getProject())
                    .tag("cache", key.getCache().getCacheName())
                    .tag("consumer", key.getConsumer())
                    .tag("strategy", key.getStrategy().name())
                    .register(meterRegistry);
            consumers.put(key, counter);
            return counter;
        }
    }
}
