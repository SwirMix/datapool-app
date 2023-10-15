package org.datapool;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.datapool.services.CacheManagerService;
import org.datapool.services.UserTokenService;
import org.datapool.services.cache.LoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@AutoConfigurationPackage
public class CacheManagerStarter implements CommandLineRunner {
    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private ApplicationContext context;

    @Bean
    public UserTokenService tokenService(){
        UserTokenService userTokenService = new UserTokenService(configuration.getIgniteClient());
        return userTokenService;
    }

    @Bean
    public LoaderService createJdbcLoadService(){
        return new LoaderService(context);
    }

    @Bean
    public CacheManagerService cacheManagerService(){
        CacheManagerService cacheManagerService = new CacheManagerService(){
            @Override
            public Ignite getIgnite(){
                if (ignite==null){
                    ignite = Ignition.start(configuration.getIgniteClient());
                }
                return ignite;
            }
        };
        cacheManagerService.setConfiguration(configuration);
        Ignite ignite = Ignition.start(configuration.getIgniteClient());
        cacheManagerService.setIgnite(ignite);
        return cacheManagerService;
    }


    public static void main(String[] args) {
        SpringApplication.run(CacheManagerStarter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
