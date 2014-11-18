package org.fao.amis.dataset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.inject.Inject;
import org.fao.amis.dataset.dto.FilterDatabase;
import org.fao.amis.dataset.dto.FilterYear;
import org.fao.amis.server.tools.jdbc.ConnectionManager;
import org.fao.amis.server.tools.utils.DatabaseUtils;
import org.fao.amis.server.tools.utils.DatasourceObject;
import org.fao.amis.server.tools.utils.YearObject;

public class FilterData
{

    @Inject
    private DatabaseUtils utils;

    @Inject
    private ConnectionManager connectionManager;
    private static String queryDatabase = "select distinct database from national_forecast where region_code =? and database is not null";
    private static String queryYears = "select distinct season, year from national_forecast where region_code = ? and product_code = ?  and season is not null and year is not null order by year DESC";

    public DatasourceObject getDatabase(FilterDatabase filter) throws Exception
    {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryDatabase);
        statement.setInt(1, filter.getRegionCode().intValue());
        return this.utils.getDataSource(statement.executeQuery());
    }

    public ArrayList<YearObject> getYears(FilterYear filter) throws Exception
    {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryYears);
        statement.setInt(1, filter.getRegionCode().intValue());
        statement.setInt(2, filter.getProductCode().intValue());
        return this.utils.getYear(statement.executeQuery());
    }
}