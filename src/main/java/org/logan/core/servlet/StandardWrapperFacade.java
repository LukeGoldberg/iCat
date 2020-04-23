package org.logan.core.servlet;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public final class StandardWrapperFacade implements ServletConfig {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new facade around a StandardWrapper.
     * @param config the associated wrapper
     */
    public StandardWrapperFacade(ServletConfig config) {
        super();
        this.config = config;
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * Wrapped config.
     */
    private final ServletConfig config;

    /**
     * Wrapped context (facade).
     */
    private ServletContext context = null;

    // -------------------------------------------------- ServletConfig Methods

    @Override
    public String getServletName() {
        return config.getServletName();
    }

    @Override
    public ServletContext getServletContext() {
        if (context == null) {
            context = config.getServletContext();
        }
        return context;
    }

    @Override
    public String getInitParameter(String name) {
        return config.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return config.getInitParameterNames();
    }

}
