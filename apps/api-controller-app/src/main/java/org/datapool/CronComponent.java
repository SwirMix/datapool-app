package org.datapool;

import org.datapool.services.StorageManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronComponent {

    @Autowired
    private StorageManagerService storageManagerService;

    @Scheduled(fixedRate = 60_000*360)
    public void reportCurrentTime() {
        storageManagerService.clearDoneTasks();
    }
}
