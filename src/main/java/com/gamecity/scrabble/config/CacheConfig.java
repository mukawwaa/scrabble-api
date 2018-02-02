package com.gamecity.scrabble.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.gamecity.scrabble.listener.RedisListener;
import com.gamecity.scrabble.model.Player;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport
{
    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private Integer redisPort;

    @Autowired
    private RedisListener redisListener;

    @Bean
    public JedisConnectionFactory connectionFactory()
    {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory)
    {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager(JedisConnectionFactory connectionFactory)
    {
        return RedisCacheManager.create(connectionFactory);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(JedisConnectionFactory connectionFactory)
    {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(
            messageListenerAdapter(redisListener, Constants.RedisListener.Method.RECEIVE_USER_UPDATE, Player.class),
            new PatternTopic(Constants.RedisListener.BOARD_USER));
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
