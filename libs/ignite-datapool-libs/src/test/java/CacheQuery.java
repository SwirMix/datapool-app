import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.datapool.dto.DataPoolItem;
import org.testng.annotations.Test;

import javax.cache.Cache;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CacheQuery {
    private String cfg = "/home/ponchick/datapool-param-service/support/configs/management-node.xml";

    @Test
    public void query(){
        Ignite client = Ignition.start(cfg);
        IgniteCache<Integer, BinaryObject> cache = client.cache("168f5212-d040-4bb3-906c-849c9031dc1c");
        int count = 0;
        for (Cache.Entry<Integer, BinaryObject> entry : cache) {
            count++;
            Integer key = entry.getKey();
            BinaryObject value = entry.getValue();
            System.out.println("total cities count = " + count);
        }

    }
}
