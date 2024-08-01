package com.avanza.license.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new CustomRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}


//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return new SimpleKeyGenerator();
//    }
//}
//package com.avanza.license.config;
//
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.cache.interceptor.SimpleKeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
//@Configuration
//@EnableCaching
//public class RedisConfig extends CachingConfigurerSupport {
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(20))
//                .disableCachingNullValues()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .build();
//    }
//
////    @Bean
////    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
////        RedisTemplate<String, Object> template = new RedisTemplate<>();
////        template.setConnectionFactory(redisConnectionFactory);
////        template.setKeySerializer(new StringRedisSerializer());
////        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
////        template.setHashKeySerializer(new StringRedisSerializer());
////        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
////        template.afterPropertiesSet();
////        return template;
////    }
//
//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return new SimpleKeyGenerator();
//    }
//}
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.concurrent.ConcurrentMapCache;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//public class CacheConfig {
//
//    @Bean
//    public CacheManager cacheManager() {
//        return new ExpiringCacheManager();
//    }
//
//    public static class ExpiringCacheManager extends ConcurrentMapCacheManager {
//        @Override
//        protected Cache createConcurrentMapCache(final String name) {
//            return new ExpiringConcurrentMapCache(name);
//        }
//    }
//
//    public static class ExpiringConcurrentMapCache extends ConcurrentMapCache {
//        private final ConcurrentMap<Object, Long> expiryMap = new ConcurrentHashMap<>();
//        private final long expiryTime = TimeUnit.MINUTES.toMillis(3);
//
//        public ExpiringConcurrentMapCache(String name) {
//            super(name);
//        }
//
//        @Override
//        public void put(Object key, Object value) {
//            super.put(key, value);
//            expiryMap.put(key, System.currentTimeMillis() + expiryTime);
//            System.out.println("Put key: " + key + ", expires at: " + expiryMap.get(key));
//        }
//
//        @Override
//        public ValueWrapper get(Object key) {
//            if (isExpired(key)) {
//                evict(key);
//                return null;
//            }
//            System.out.println("Get key: " + key + ", expires at: " + expiryMap.get(key));
//            return super.get(key);
//        }
//
//        @Override
//        public void evict(Object key) {
//            super.evict(key);
//            expiryMap.remove(key);
//            System.out.println("Evict key: " + key);
//        }
//
//        private boolean isExpired(Object key) {
//            Long expiryTime = expiryMap.get(key);
//            boolean expired = expiryTime != null && System.currentTimeMillis() > expiryTime;
//            System.out.println("Is key: " + key + " expired? " + expired);
//            return expired;
//        }
//
//        public Long getRemainingTime(Object key) {
//            Long expiryTime = expiryMap.get(key);
//            if (expiryTime == null) {
//                return null;
//            }
//            long remainingTime = expiryTime - System.currentTimeMillis();
//            System.out.println("Remaining time for key: " + key + " is " + remainingTime + "ms");
//            return remainingTime > 0 ? remainingTime : 0;
//        }
//    }
//}
