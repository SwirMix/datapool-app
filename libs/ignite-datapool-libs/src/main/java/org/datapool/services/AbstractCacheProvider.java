package org.datapool.services;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.datapool.CacheFactory;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.DataPoolToken;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;

public abstract class AbstractCacheProvider {
    protected static Ignite ignite;
    private String igniteCfg;
    protected IgniteCache<CacheMetadataKey, CacheMetadata> metadataCache;
    protected IgniteCache<CacheMetadataKey, Long> cursorsCache;
    protected IgniteCache<String, DataPoolToken> tokenIgniteCache;
    protected String mainDataRegion = "datapool-persistence";

    public String getMainDataRegion() {
        return mainDataRegion;
    }

    public String getIgniteCfg() {
        return igniteCfg;
    }

    public AbstractCacheProvider setIgniteCfg(String igniteCfg) {
        this.igniteCfg = igniteCfg;
        return this;
    }

    public AbstractCacheProvider setMainDataRegion(String mainDataRegion) {
        this.mainDataRegion = mainDataRegion;
        return this;
    }

    public Ignite getIgnite() {
        if (ignite==null){
            ignite = Ignition.start(igniteCfg);
            String id = String.valueOf(ignite.configuration().getConsistentId());

        }
        return ignite;
    }


    public AbstractCacheProvider setIgnite(Ignite ignite) {
        this.ignite = ignite;
        return this;
    }


    protected IgniteCache<CacheMetadataKey, CacheMetadata> getMetadataCache(){
        if (metadataCache==null){
            CacheConfiguration<CacheMetadataKey, CacheMetadata> metadataCfg = CacheFactory.metadataCache(
                    CacheMode.REPLICATED,
                    CacheAtomicityMode.TRANSACTIONAL,
                    mainDataRegion
            );
            metadataCache = ignite.getOrCreateCache(metadataCfg);
        }
        return metadataCache;
    }

    protected IgniteCache<CacheMetadataKey, Long> getCursorsCache(){
        if (cursorsCache==null){
            CacheConfiguration<CacheMetadataKey, Long> metadataCfg = CacheFactory.cursorsCache(
                    CacheMode.REPLICATED,
                    CacheAtomicityMode.TRANSACTIONAL,
                    mainDataRegion
            );
            cursorsCache = ignite.getOrCreateCache(metadataCfg);
        }
        return cursorsCache;
    }

    protected IgniteCache<String, DataPoolToken> getTokensCache(){
        if (tokenIgniteCache==null){
            CacheConfiguration<String, DataPoolToken> metadataCfg = CacheFactory.tokenRepository(
                    CacheMode.REPLICATED,
                    CacheAtomicityMode.TRANSACTIONAL,
                    mainDataRegion
            );
            tokenIgniteCache = ignite.getOrCreateCache(metadataCfg);
        }
        return tokenIgniteCache;
    }


}
