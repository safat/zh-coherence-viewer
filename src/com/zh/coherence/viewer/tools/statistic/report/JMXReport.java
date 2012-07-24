package com.zh.coherence.viewer.tools.statistic.report;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JMXReport {
    private List<List> clusterInfo;

    private NodeReport nodeReport = new NodeReport();

    private CacheReport cacheReport = new CacheReport();

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private Map<String, String> properties = new HashMap<String, String>();

    public CacheReport getCacheReport() {
        return cacheReport;
    }

    public void setCacheReport(CacheReport cacheReport) {
        this.cacheReport = cacheReport;
    }

    public List<List> getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(List<List> clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public NodeReport getNodeReport() {
        return nodeReport;
    }

    public void setNodeReport(NodeReport nodeReport) {
        this.nodeReport = nodeReport;
    }

    public void refresh() {
        nodeReport.updateData();
        cacheReport.updateData();

        for (ChangeListener l : listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public JMXReport() {
        initProperties();
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    private void initProperties() {
        properties.put("AverageGetMillis",
                "The average number of milliseconds per \"get\" invocation since the cache statistics were last reset.");
        properties.put("AverageHitMillis", "The average number of milliseconds per \"get\" invocation that is a hit.");
        properties.put("AverageMissMillis", "The average number of milliseconds per \"get\" invocation that is a miss.");
        properties.put("AveragePutMillis", "The average number of milliseconds per \"put\" invocation since the cache statistics were last reset.");
        properties.put("BatchFactor",
                "The BatchFactor attribute is used to calculate the soft-ripe time for write-behind queue entries. A queue entry is considered to be ripe for a write operation if it has been in the write-behind queue for no less than the QueueDelay interval. The soft-ripe time is the point in time prior to the actual ripe time after which an entry is included in a batch asynchronous write operation to the cache store (along with all other ripe and soft-ripe entries). This attribute is only applicable if asynchronous writes are enabled (that is, the value of the QueueDelay attribute is greater than zero) and the cache store implements the storeAll() method. The value of the element is expressed as a percentage of the QueueDelay interval. Valid values are doubles in the interval [0.0, 1.0].");
        properties.put("CacheHits", "The rough number of cache hits since the last time statistics were reset. A cache hit is a read operation invocation (that is, get()) for which an entry exists in this map.");
        properties.put("CacheHitsMillis", "The total number of milliseconds (since the last time statistics were reset) for the get() operations for which an entry existed in this map.");
        properties.put("CacheMisses", "The rough number of cache misses since the last time statistics were reset.");
        properties.put("CacheMissesMillis", "The total number of milliseconds (since the last time statistics were reset) for the get() operations for which no entry existed in this map.");
        properties.put("CachePrunes", "The number of prune operations since the last time statistics were reset. A prune operation occurs every time the cache reaches its high watermark as specified by the HighUnits attribute.");
        properties.put("CachePrunesMillis", "The total number of milliseconds for the prune operations since the last time statistics were reset.");
        properties.put("Description", "The cache description.");
        properties.put("ExpiryDelay", "The time-to-live for cache entries in milliseconds. Value of zero indicates that the automatic expiry is disabled. Change of this attribute will not affect already-scheduled expiry of existing entries.");
        properties.put("FlushDelay", "The number of milliseconds between cache flushes. Value of zero indicates that the cache will never flush.");
        properties.put("HighUnits", "The limit of the cache size measured in units. The cache will prune itself automatically once it reaches its maximum unit level. This is often referred to as the high water mark of the cache.");
        properties.put("HitProbability", "The rough probability (0 <= p <= 1) that the next invocation will be a hit, based on the statistics collected since the last time statistics were reset.");
        properties.put("LowUnits", "The number of units to which the cache will shrink when it prunes. This is often referred to as a low water mark of the cache.");
        properties.put("PersistenceType", "The persistence type for this cache. Possible values include: NONE, READ-ONLY, WRITE-THROUGH, WRITE-BEHIND.");
        properties.put("QueueDelay", "The number of seconds that an entry added to a write-behind queue sits in the queue before being stored using a cache store. This attribute is only applicable if the persistence type is WRITE-BEHIND.");
        properties.put("QueueSize", "The size of the write-behind queue size. This attribute is only applicable if the persistence type is WRITE-BEHIND.");
        properties.put("RefreshFactor", "This attribute is used to calculate the soft-expiration time for cache entries. Soft-expiration is the point in time prior to the actual expiration after which any access request for an entry will schedule an asynchronous load request for the entry. This attribute is only applicable for a read write backing map which has an internal local cache with scheduled automatic expiration. The value of this element is expressed as a percentage of the internal local cache expiration interval. Valid values are doubles in the interval [0.0, 1.0]. If zero, refresh-ahead scheduling is disabled.");
        properties.put("RefreshTime", "The timestamp when this model was last retrieved from a corresponding node. For local servers it is the local time.");
        properties.put("RequeueThreshold", "The maximum size of the write-behind queue for which failed cache store write operations are requeued. If zero, the write-behind requeueing will be disabled. This attribute is only applicable if the persistence type is WRITE-BEHIND.");
        properties.put("Size", "The number of entries in the cache.");
        properties.put("StoreAverageBatchSize", "The average number of entries stored for each cache store write operation. A call to the store() method is counted as a batch of one, whereas a call to the storeAll() method is counted as a batch of the passed Map size. The value is -1 if the persistence type is NONE.");
        properties.put("StoreAverageReadMillis", "The average time (in milliseconds) spent per read operation. The value is -1 if the persistence type is NONE.");
        properties.put("StoreAverageWriteMillis", "The average time (in milliseconds) spent per write operation. The value is -1 if the persistence type is NONE.");
        properties.put("StoreFailures", "The total number of cache store failures (load, store and erase operations). The value is -1 if the persistence type is NONE.");
        properties.put("StoreReadMillis", "The cumulative time (in milliseconds) spent on load operations. The value is -1 if the persistence type is NONE.");
        properties.put("StoreReads", "The total number of load operations. The value is -1 if the persistence type is NONE.");
        properties.put("StoreWriteMillis", "The cumulative time (in milliseconds) spent on store and erase operations. The value is -1 if the persistence type is NONE or READ-ONLY.");
        properties.put("StoreWrites", "The total number of store and erase operations. The value is -1 if the persistence type is NONE or READ-ONLY.");
        properties.put("TotalGets", "The total number of get() operations since the last time statistics were reset.");
        properties.put("TotalGetsMillis", "The total number of milliseconds spent on get() operations since the last time statistics were reset.");
        properties.put("TotalPuts", "The total number of put() operations since the last time statistics were reset.");
        properties.put("TotalPutsMillis", "The total number of milliseconds spent on put() operations since the last time statistics were reset.");
        properties.put("UnitFactor", "The factor by which the Units, LowUnits and HighUnits properties are adjusted. Using a BINARY unit calculator, for example, the factor of 1048576 could be used to count megabytes instead of bytes.");
        properties.put("Units", "The size of the cache measured in units. This value needs to be adjusted by the UnitFactor.");
    }
}
