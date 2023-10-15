package org.datapool;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.datapool.service.DatapoolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@AutoConfigurationPackage
public class DatapoolAppStarter implements CommandLineRunner {
    @Autowired
    private ApplicationConfiguration configuration;

    @Bean
    public DatapoolServiceImpl datapoolService(){
        return new DatapoolServiceImpl().setIgnite(initIgniteClient());
    }

    @Bean
    public MeterRegistry getMeterRegistry() {
        CompositeMeterRegistry meterRegistry = new CompositeMeterRegistry();
        return meterRegistry;
    }

    public Ignite initIgniteClient(){
        return Ignition.start(configuration.getIgniteClient());
    }

    public static void main(String[] args) {
        SpringApplication.run(DatapoolAppStarter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
