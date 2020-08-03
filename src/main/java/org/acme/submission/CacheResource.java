package org.acme.submission;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class CacheResource {

    private final Logger log = LoggerFactory.getLogger(CacheResource.class);

    RemoteCache<String, Double> vibeCache;
    RemoteCache<String, Double> capacityCache;

    @Inject
    RemoteCacheManager cacheManager;

    private static final String CACHE_CONFIG = "<infinispan><cache-container>" +
            "<distributed-cache name=\"%s\" mode=\"ASYNC\"></distributed-cache>" +
            "</cache-container></infinispan>";

    void onStart(@Observes @Priority(value = 1) StartupEvent ev) {
        log.info("On start - get caches");
        vibeCache = cacheManager.administration().getOrCreateCache("vibeCache", new XMLStringConfiguration(String.format(CACHE_CONFIG, "vibeCache")));
        capacityCache = cacheManager.administration().getOrCreateCache("capacityCache", new XMLStringConfiguration(String.format(CACHE_CONFIG, "capacityCache")));
        log.info("Existing stores are " + cacheManager.getCacheNames().toString());
    }

    @Incoming("tv-in-r1")
    public void storeInCacheR1(JsonObject aggregation) {
        log.info("::storeInCacheR1: " + aggregation);
        String cacheKey = String.format("%s-%s", "r1", aggregation.getString("key"));
        capacityCache.put(cacheKey, aggregation.getDouble("capacityAvg"), 1, TimeUnit.HOURS);
        vibeCache.put(cacheKey, aggregation.getDouble("vibeAvg"), 1, TimeUnit.HOURS);
    }

    @Incoming("tv-in-r5")
    public void storeInCacheR5(JsonObject aggregation) {
        log.info("::storeInCacheR5: " + aggregation);
        String cacheKey = String.format("%s-%s", "r5", aggregation.getString("key"));
        capacityCache.put(cacheKey, aggregation.getDouble("capacityAvg"), 1, TimeUnit.HOURS);
        vibeCache.put(cacheKey, aggregation.getDouble("vibeAvg"), 1, TimeUnit.HOURS);
    }

    @Incoming("tv-in-t1")
    public void storeInCacheT1(JsonObject aggregation) {
        log.info("::storeInCacheT1: " + aggregation);
        String cacheKey = String.format("%s-%s", "t1", aggregation.getString("key"));
        capacityCache.put(cacheKey, aggregation.getDouble("capacityAvg"), 1, TimeUnit.HOURS);
        vibeCache.put(cacheKey, aggregation.getDouble("vibeAvg"), 1, TimeUnit.HOURS);
    }

    @Incoming("tv-in-t5")
    public void storeInCacheT5(JsonObject aggregation) {
        log.info("::storeInCacheT5: " + aggregation);
        String cacheKey = String.format("%s-%s", "t5", aggregation.getString("key"));
        capacityCache.put(cacheKey, aggregation.getDouble("capacityAvg"), 1, TimeUnit.HOURS);
        vibeCache.put(cacheKey, aggregation.getDouble("vibeAvg"), 1, TimeUnit.HOURS);
    }
}
