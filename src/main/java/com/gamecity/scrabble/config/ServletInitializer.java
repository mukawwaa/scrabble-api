package com.gamecity.scrabble.config;

import javax.servlet.Filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.gamecity.scrabble.filter.CORSFilter;
import com.gamecity.scrabble.servlet.JerseyServlet;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        return new Class<?>[] { JerseyServlet.class };
    }

    @Override
    protected String[] getServletMappings()
    {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters()
    {
        return new Filter[] { new OpenEntityManagerInViewFilter(), new CORSFilter() };
    }
}
