package org.datapool.services;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.datapool.ApplicationConfiguration;
import org.datapool.CacheFactory;
import org.datapool.CacheManager;
import org.datapool.ExcludeCache;
import org.datapool.dto.CacheInfo;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.ProjectCaches;
import org.datapool.dto.commons.CreateCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.dto.metadata.CacheStatus;
import org.datapool.dto.metadata.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.datapool.services.AbstractCacheProvider.ignite;

public class CacheManagerService extends org.datapool.proxy.CacheManagerService {

    private ApplicationConfiguration configuration;

    public ApplicationConfiguration getConfiguration() {
        return configuration;
    }

    public CacheManagerService setConfiguration(ApplicationConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public Ignite getIgnite(){
        if (ignite==null){
            ignite = Ignition.start(configuration.getIgniteClient());
        }
        return ignite;
    }
}
