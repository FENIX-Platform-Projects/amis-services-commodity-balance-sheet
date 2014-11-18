package org.fao.amis.dataset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import javax.inject.Inject;
import org.fao.amis.dataset.dto.DatasetFilterWithDate;
import org.fao.amis.dataset.dto.DateFilter;
import org.fao.amis.dataset.dto.FilterCrops;
import org.fao.amis.server.tools.jdbc.ConnectionManager;
import org.fao.amis.server.tools.utils.DatabaseUtils;

public class SupportData
{

    @Inject
    private DatabaseUtils utils;

    @Inject
    private ConnectionManager connectionManager;
    private static String queryMostRecentDate = "select distinct date from national_forecast where region_code = ? and product_code =? and year = ? order by date";
    private static int[] queryInsertTypes = { 4, 4, 4, 4, 12, 12, 7, 12, 12 };
    private static String queryLoad = "select element_code, units, date, value, flag,  notes from national_forecast where region_code = ? and product_code = ? and year = ? and date =?";
    private static String queryCrops = "select crops_num from amis_crops where region_code =? and product_code =?";

    public Iterator<Object[]> getMostRecentForecastDate(DateFilter filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryMostRecentDate);

        statement.setInt(1, filter.getRegion().intValue());
        statement.setInt(2, filter.getProduct().intValue());
        statement.setInt(3, filter.getYear().intValue());

        return this.utils.getDataIterator(statement.executeQuery());
    }

    public Iterator<Object[]> getPreviousYearForecast(DatasetFilterWithDate filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryLoad);

        statement.setInt(1, filter.getRegion().intValue());
        statement.setInt(2, filter.getProduct().intValue());
        statement.setInt(3, filter.getYear().intValue());
        statement.setString(4, filter.getDate());

        return this.utils.getDataIterator(statement.executeQuery());
    }

    public String getCropsNumber(FilterCrops filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryCrops);

        statement.setString(1, filter.getRegionCode());
        statement.setString(2, filter.getProductCode());

        return this.utils.getCrops(statement.executeQuery());
    }
}