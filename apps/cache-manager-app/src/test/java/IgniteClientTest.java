import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.cache.Cache;
import java.util.Iterator;

public class IgniteClientTest {
    public static final String config = "/home/ponchick/datapool-param-service/support/configs/management-node.xml";
    private Ignite ignite;

    @BeforeTest
    public void init(){
        ignite = Ignition.start(config);
    }

    @Test
    public void test(){
        IgniteCache<Integer, DataPoolHashData> cache = ignite.cache("9178aa5a-ca3b-408b-8894-36f41a7be4ed");
        System.out.println(cache.metrics().getCacheSize());
        CacheMetadataKey key = new CacheMetadataKey("adtrs-adtrs-adtrs-adtrs-adtrs", "test_for_test");
    }
}
