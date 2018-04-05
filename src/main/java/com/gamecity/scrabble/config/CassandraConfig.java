package com.gamecity.scrabble.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;

@Configuration
@PropertySource("classpath:cassandra.properties")
public class CassandraConfig
{
    @Value("${cassandra.contactpoints}")
    private String contactPoints;

    @Value("${cassandra.port}")
    private int port;

    @Value("${cassandra.keyspace}")
    private String keySpace;

    @Value("${cassandra.user}")
    private String user;

    @Value("${cassandra.password}")
    private String password;

    @Value("${cassandra.consistencyLevel}")
    private ConsistencyLevel consistencyLevel;

    @Bean
    public Cluster cluster()
    {
        Builder builder = Cluster.builder();

        for (String host : contactPoints.split(","))
        {
            builder.addContactPoint(host).withPort(port);
        }

        QueryOptions options = new QueryOptions();
        options.setConsistencyLevel(consistencyLevel);
        builder.withQueryOptions(options);

        return builder.withCredentials(user, password).build();
    }

    @Bean
    public Session session()
    {
        return cluster().connect(keySpace);
    }

    @Bean
    public MappingManager mappingContext()
    {
        return new MappingManager(session());
    }
}
