package org.fao.amis.server.tools.jdbc;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@WebFilter(filterName="OrientConnectionManager", urlPatterns={"/*"})
public class ConnectionManager implements Filter {

    private static ThreadLocal<Connection> threadConnection = new ThreadLocal<>();
    public Connection getConnection() {
        return threadConnection.get();
    }


    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private String url,usr,psw;

    @Override public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        url = context.getInitParameter("url");
        usr = context.getInitParameter("usr");
        psw = context.getInitParameter("psw");
    }
    @Override public void destroy() { }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Connection connection = null;
        try {
            threadConnection.set(connection = DriverManager.getConnection(url, usr, psw));
        } catch (Exception ex) {
            throw new ServletException("Database connection error.", ex);
        }

        try {
            filterChain.doFilter(servletRequest,servletResponse);
        } finally {
            if (connection!=null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
}
