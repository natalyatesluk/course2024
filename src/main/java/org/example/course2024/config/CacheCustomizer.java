package org.example.course2024.config;

import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;

import java.util.Arrays;

@EnableCaching
@Component
public class CacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(Arrays.asList("masters", "customers", "price", "schedules", "appointment"));
    }
}
