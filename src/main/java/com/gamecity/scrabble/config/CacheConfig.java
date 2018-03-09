package com.gamecity.scrabble.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.gamecity.scrabble.Constants;
import com.gamecity.scrabble.entity.BoardUserHistory;
import com.gamecity.scrabble.listener.RedisListener;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport
{
    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private Integer redisPort;

    @Bean
    public JedisConnectionFactory connectionFactory()
    {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisConfiguration);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory)
    {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager(JedisConnectionFactory connectionFactory)
    {
        RedisCacheManager cacheManager = RedisCacheManager.create(connectionFactory);
        cacheManager.afterPropertiesSet();
        return cacheManager;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(JedisConnectionFactory connectionFactory, RedisListener redisListener)
    {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(
            messageListenerAdapter(redisListener, Constants.RedisListener.Method.RECEIVE_USER_UPDATE, BoardUserHistory.class),
            new PatternTopic(Constants.RedisListener.BOARD_USER_HISTORY));
        container.afterPropertiesSet();
        return container;
    }

    private MessageListenerAdapter messageListenerAdapter(Object listener, String method, Class clazz)
    {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(listener, method);
        messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(clazz));
        messageListenerAdapter.afterPropertiesSet();
        return messageListenerAdapter;
    }
}
