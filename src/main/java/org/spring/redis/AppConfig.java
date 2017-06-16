package org.spring.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@SpringBootApplication
public class AppConfig {

	// http://localhost/?data=123

	public static void main(String[] args) {
		SpringApplication.run(AppConfig.class, args);
	}

	@Autowired
	private RedisTemplate<String, String> template;

	public String getValue(final String key) {
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		return template.opsForValue().get(key);
	}

	public void setValue(final String value) {
		template.opsForValue().set("data", value);
	}

	@RequestMapping("/")
	public String test(@RequestParam("data") String value) {
		System.out.println(value);
		setValue(value);
		return "data" + getValue("data");
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName("10.100.8.176");
		jedisConFactory.setPort(6379);
		return jedisConFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}
}
