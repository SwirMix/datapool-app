package org.datapool.victoria.dto;

public class ConsumerCountData {
    private String project = "";
    private String consumer = "";
    private String cache = "";
    private Double count = 0.0;
    private Double percentage = 0.0;
    private String strategy;

    public String getStrategy() {
        return strategy;
    }

    public ConsumerCountData setStrategy(String strategy) {
        this.strategy = strategy;
        return this;
    }

    public ConsumerCountData setPercentage(Double percentage) {
        this.percentage = percentage;
        return this;
    }

    public double getPercentage() {
        return percentage;
    }

    public void incrementCount(Double data){
        this.count += data;
    }

    public ConsumerCountData setPercentage(double percentage) {
        this.percentage = percentage;
        return this;
    }

    public String getProject() {
        return project;
    }

    public ConsumerCountData setProject(String project) {
        this.project = project;
        return this;
    }

    public String getConsumer() {
        return consumer;
    }

    public ConsumerCountData setConsumer(String consumer) {
        this.consumer = consumer;
        return this;
    }

    public String getCache() {
        return cache;
    }

    public ConsumerCountData setCache(String cache) {
        this.cache = cache;
        return this;
    }

    public Double getCount() {
        return count;
    }

    public ConsumerCountData setCount(Double count) {
        this.count = count;
        return this;
    }
}
