
package jp.co.axa.apidemo.redis;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import jp.co.axa.apidemo.entities.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
@Slf4j
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	//Creating Connection with Redis
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	//Creating RedisTemplate for Entity 'Employee'
	@Bean
	public RedisTemplate<String, Employee> redisTemplate(){
		RedisTemplate<String, Employee> empTemplate = new RedisTemplate<>();
		empTemplate.setConnectionFactory(redisConnectionFactory());
		return empTemplate;
	}
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
		RedisCacheConfiguration redisCacheConfiguration = config
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair
				.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration)
			.build();
	}

	@PostConstruct
	public void clearCache() {
		log.info("In Clear Cache");
		Jedis jedis = new Jedis(redisHost, redisPort, 1000);
		jedis.flushAll();
		jedis.close();
	}

}
