package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration;
import org.springframework.cloud.aws.cache.config.annotation.CacheClusterConfig;
import org.springframework.cloud.aws.cache.config.annotation.EnableElastiCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = ElastiCacheAutoConfiguration.class)
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Configuration
    @EnableCaching
    @Profile("local")
    protected static class LocalCacheConfiguration {

        @Bean
        public CacheManager createSimpleCacheManager() {
            logger.info(" =============== >>>>> inside of createSimpleCacheManager .........");
            SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
            List<Cache> caches = new ArrayList<>(2);
            caches.add(new ConcurrentMapCache("easewebpoc1"));
            caches.add(new ConcurrentMapCache("GitHubSourceCode"));
            simpleCacheManager.setCaches(caches);

            return simpleCacheManager;
        }

    }

    @Configuration
    @EnableElastiCache({@CacheClusterConfig(name = "easewebpoc1", expiration = 23)})
    @Profile("aws")
    protected static class ElastiCacheConfiguration {

    }

}
