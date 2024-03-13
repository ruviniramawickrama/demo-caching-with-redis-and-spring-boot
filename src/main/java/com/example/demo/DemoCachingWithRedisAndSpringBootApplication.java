/**
 * @author  Ruvini Ramawickrama
 */
package com.example.demo;

import com.example.demo.util.BookCacheKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@SpringBootApplication
@EnableCaching
public class DemoCachingWithRedisAndSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoCachingWithRedisAndSpringBootApplication.class, args);
	}

	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		return (builder) -> builder
				.withCacheConfiguration("bookCache",
						RedisCacheConfiguration
								.defaultCacheConfig()
								.entryTtl(Duration.ofSeconds(30)));
	}

	@Bean("bookCacheKeyGenerator")
	public KeyGenerator keyGenerator() {
		return new BookCacheKeyGenerator();
	}

}
