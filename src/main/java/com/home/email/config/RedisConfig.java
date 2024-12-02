package com.home.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      final RedisConnectionFactory redisConnectionFactory) {

    final RedisTemplate<String, Object> template = new RedisTemplate<>();

    final Jackson2JsonRedisSerializer<Object> jsonSerializer =
        new Jackson2JsonRedisSerializer<>(Object.class);

    template.setValueSerializer(jsonSerializer);
    template.setKeySerializer(new StringRedisSerializer());

    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}
