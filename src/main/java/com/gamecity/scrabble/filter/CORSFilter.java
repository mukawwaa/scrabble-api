package com.gamecity.scrabble.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.gamecity.scrabble.Constants;

public class CORSFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader(Constants.Headers.ALLOW_ORIGIN, "*");
        response.setHeader(Constants.Headers.ALLOW_CREDENTIALS, "true");
        response.setHeader(Constants.Headers.ALLOW_METHODS, "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader(Constants.Headers.MAX_AGE, "3600");
        response.setHeader(
            Constants.Headers.ALLOW_HEADERS,
            "X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig)
    {
        // nothing to do
    }

    @Override
    public void destroy()
    {
        // nothing to do
    }
}
