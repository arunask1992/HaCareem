package com.careem.commons;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.transaction.Transactional;
import java.io.IOException;

@Component
public class DatabaseFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        // Do nothing
    }

    @Transactional(rollbackOn = Exception.class)
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
