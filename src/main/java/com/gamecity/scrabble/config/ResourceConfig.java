package com.gamecity.scrabble.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

import com.gamecity.scrabble.Constants;

@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter
{
    @Override
    public void configure(ResourceServerSecurityConfigurer resources)
    {
        resources.resourceId(Constants.BASE_APP_REST_RESOURCE).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http
            .anonymous().disable() //
            .authorizeRequests().antMatchers("/rest/**").hasAnyRole("ADMIN", "USER", "SYSTEM") //
            .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
