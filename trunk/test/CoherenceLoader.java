
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import org.junit.Test;

/**
 * @author Живко
 */
public class CoherenceLoader {
    @Test public void createCache(){
        NamedCache cache = CacheFactory.getCache("test");
        cache.put("key1", "value1");
        
    }
}
