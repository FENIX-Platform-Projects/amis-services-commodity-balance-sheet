package org.fao.amis.dataset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Iterator;
import javax.inject.Inject;

import org.fao.amis.dataset.dto.*;
import org.fao.amis.server.tools.jdbc.ConnectionManager;
import org.fao.amis.server.tools.utils.DatabaseUtils;

public class SupportData
{

    @Inject
    private DatabaseUtils utils;

    @Inject
    private ConnectionManager connectionManager;
    private static String queryFunction = "select create_population_datasources()";
    private static String queryMostRecentDate = "select distinct date from national_forecast where region_code = ? and product_code =? and year = ? order by date";
    private static int[] queryInsertTypes = { 4, 4, 4, 4, 12, 12, 7, 12, 12 };
    private static String queryLoad = "select element_code, units, date, value, flag,  notes from national_forecast where region_code = ? and product_code = ? and year = ? and date =? and season=?";
    private static String queryCrops = "select crops_num from amis_crops where region_code =? and product_code =?";
    private static String queryPopulationLoad = "select distinct on (year) region_code, region_name, element_code,element_name, units, year, value, flag, notes from national_population where region_code = ? order by year DESC";
    private static  String queryDeletePopulation = "delete from national_population where region_code =?";
    private static int[] queryInsertTypesPopulation = { Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.VARCHAR,Types.VARCHAR, Types.INTEGER,Types.REAL,  Types.VARCHAR, Types.VARCHAR };
    private static String queryInsertPopulation = "insert into national_population (region_code, region_name, element_code, element_name, units,year,value,flag,notes) values (?,?,?,?,?,?,?,?,?)";


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
        statement.setString(5,filter.getSeason());

        return this.utils.getDataIterator(statement.executeQuery());
    }

    public String getCropsNumber(FilterCrops filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryCrops);

        statement.setString(1, filter.getRegionCode());
        statement.setString(2, filter.getProductCode());

        return this.utils.getCrops(statement.executeQuery());
    }


    public Iterator<Object[]> getPopulationData(PopulationFilter filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryPopulationLoad);

        statement.setInt(1,filter.getRegionCode());

        return  this.utils.getDataIterator(statement.executeQuery());
    }

    public void updPopulationData(PopulationDataset data) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        try {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(queryDeletePopulation);
            statement.setInt(1, data.getFilter().getRegionCode());
            statement.executeUpdate();

            if (data.getData() != null) {
                statement = connection.prepareStatement(queryInsertPopulation);
                for (Object[] row : data.getData()) {
                    this.utils.fillStatement(statement, queryInsertTypesPopulation, new Object[] { data.getFilter().getRegionCode(),  row[1],row[2],row[3],row[4],row[5],row[6],row[7],row[8]});

                    statement.addBatch();
                }
                statement.executeBatch();
            }


            statement = connection.prepareStatement(queryFunction);
            statement.execute();

            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}