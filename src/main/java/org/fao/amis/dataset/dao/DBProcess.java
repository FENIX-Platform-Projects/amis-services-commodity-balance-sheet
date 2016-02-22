package org.fao.amis.dataset.dao;

import org.fao.amis.server.tools.jdbc.ConnectionManager;
import org.fao.amis.server.tools.utils.DatabaseUtils;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;

public class DBProcess implements Runnable {

    @Inject
    private DatabaseUtils utils;

    private Connection connection;

    private String query;

    public void init(String query, Connection connection) {
        this.query = query;
        this.connection = connection;
    }


    public void run() {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = this.accessDb();
            statement = connection.prepareStatement(this.query);
            statement.execute();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }


    private Connection accessDb() throws ClassNotFoundException, NamingException, SQLException {
        Class.forName("org.postgresql.Driver");
        Context context = (Context) new InitialContext().lookup("java:comp/env");
        String usr = (String) context.lookup("usr_n");
        String psw = (String) context.lookup("psw_n");
        String url = (String) context.lookup("url_n");
        return DriverManager.getConnection(url, usr, psw);
    }
}
