package org.datapool;

import okhttp3.OkHttpClient;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.datapool.csv.FileApiService;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.jwt.PasswordJwtService;
import org.datapool.repository.ProjectPermissionRepository;
import org.datapool.repository.SettingsRepository;
import org.datapool.services.DatapoolService;
import org.datapool.services.StorageManagerService;
import org.datapool.victoria.ConsumersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EnableScheduling
@SpringBootApplication
@AutoConfigurationPackage
public class ApplicationStarter implements CommandLineRunner {

    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private ProjectPermissionRepository permissionRepository;

    @Bean
    public PasswordJwtService getPasswordJwtService(){
        String secret = settingsRepository.findByName(configuration.getPasswordSecret()).getValue();
        PasswordJwtService passwordJwtService = new PasswordJwtService(secret);
        return passwordJwtService;
    }

    @Bean
    public StorageManagerService fileApiService(){
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setAddresses(configuration.getIgnite());
        StorageManagerService fileApiService = new StorageManagerService(configuration.getCsvStorage(), clientConfiguration, 50);
        return fileApiService;
    }


    @Bean
    public GlobalTokenService getGlobalTokenService(){
        String secret = settingsRepository.findByName(configuration.getJwtSecret()).getValue();
        GlobalTokenService tokenService = new GlobalTokenService(secret);
        return tokenService;
    }

    @Bean(name = "cacheManagerServer")
    public Retrofit cacheManagerServer(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(settingsRepository.findByName(configuration.getCacheManagerApp()).getValue())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    @Bean
    public ConsumersService consumersService(){
        return new ConsumersService(
                settingsRepository.findByName(configuration.getMetricsPusher()).getValue()
        );
    }
    @Bean
    public DatapoolService dataPoolServer(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(settingsRepository.findByName(configuration.getDatapoolApp()).getValue())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        DatapoolService service = new DatapoolService(
                settingsRepository.findByName(configuration.getMasterToken()).getValue(),
                retrofit
        );
        return service;
    }



    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
