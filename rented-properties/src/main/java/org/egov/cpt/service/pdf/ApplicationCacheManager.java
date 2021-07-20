package org.egov.cpt.service.pdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class ApplicationCacheManager {

    @Autowired
    private CacheManager cacheManager;

    public void put(Object key, Object value) {
        cacheManager.getCache("ch.chandigarh").put(key, value);
    }

    public Object get(Object key) {
        return cacheManager.getCache("ch.chandigarh").get(key).get();
    }

    public <T> T get(Object key, Class<T> returnType) {
        return cacheManager.getCache("ch.chandigarh").get(key, returnType);
    }

    public void remove(Object key) {
        cacheManager.getCache("ch.chandigarh").evict(key);
    }
}
