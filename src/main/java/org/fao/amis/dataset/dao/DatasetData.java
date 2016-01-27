package org.fao.amis.dataset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import javax.inject.Inject;

import org.fao.amis.dataset.dto.*;
import org.fao.amis.server.tools.jdbc.ConnectionManager;
import org.fao.amis.server.tools.utils.DatabaseUtils;

public class DatasetData {

    @Inject
    private DatabaseUtils utils;

    @Inject
    private ConnectionManager connectionManager;

    private static String queryDeleteAll = "delete from national_forecast where region_code = ? and product_code = ? and ( (";
    private static String queryFunction = "select create_national_datasources()";
    private static String queryLoad = "select element_code, units, date, value, flag,  notes from national_forecast where region_code = ? and product_code = ? and year = ? and season =?";
    private static String queryLoadNational = "select element_code, units, value, flag,notes from national_population where region_code = ? and element_code = ? and year = ? ";
    private static String queryDelete = "delete from national_forecast where region_code = ? and product_code = ? and year = ? and season = ?";
    private static String queryDeletePrevYear = "delete from national_forecast where region_code = ? and product_code = ? and year = ? and season = ? and date = ? and database =?";
    private static String queryMostRecentDate = "select distinct date from national_forecast where region_code = ? and product_code = ? and year = ? order by date ASC";
    private static String queryDeleteNational = "delete from national_population where region_code = ? and product_code = ? and year = ? and database =?";
    private static String queryInsert = "insert into national_forecast(region_code, product_code, year,season,database, element_code, units, date, value, flag, notes) values (?,?,?,?,?,?,?,?,?,?,?)";
    private static String queryExportTotalAnnual =
            "select  product_code , element_code, units, season||' (' ||date|| ')' as date  ,value,flag, notes from (select region_code ,\n" +
                    "  region_name ,\n" +
                    "  product_code ,\n" +
                    "  product_name ,\n" +
                    "  element_code ,\n" +
                    "  element_name ,\n" +
                    "  season ,\n" +
                    "  units,\n" +
                    "  year,\n" +
                    "  date ,\n" +
                    "  value ,\n" +
                    "  flag, \n" +
                    "  notes ,\n" +
                    "  max(date) over (partition by season, product_code) as maxdate\n" +
                    "  from (SELECT    \n" +
                    "   t1.product_code,\n" +
                    "         t1.product_name,\n" +
                    "         t1.region_code, \n" +
                    "         t1.region_name, \n" +
                    "         t1.element_code,\n" +
                    "         t1.element_name,\n" +
                    "         t1.year,       \n" +
                    "         t1.date,       \n" +
                    "         t1.season ,    \n" +
                    "         t1.units,      \n" +
                    "         t1.value,      \n" +
                    "         t1.flag,       \n" +
                    "         t1.notes       \n" +
                    "    FROM national_forecast t1\n" +
                    "LEFT JOIN national_population t2 ON t2.region_code = t1.region_code \n" +
                    "and \n" +
                    " t2.year = t1.year\n" +
                    "UNION\n" +
                    "    SELECT t1.product_code,\n" +
                    "           t1.product_name,\n" +
                    "           t2.region_code, \n" +
                    "           t2.region_name, \n" +
                    "           t2.element_code,\n" +
                    "           t2.element_name,\n" +
                    "           t2.year,     \n" +
                    "           t1.date,     \n" +
                    "           t1.season ,  \n" +
                    "           t2.units,    \n" +
                    "           t2.value,    \n" +
                    "           t2.flag,     \n" +
                    "           t2.notes     \n" +
                    "  FROM national_forecast t1                                                 \n" +
                    "LEFT JOIN national_population t2 ON t2.region_code = t1.region_code and\n" +
                    "t2.year = t1.year) as nationalJOIN \n" +
                    "  where region_code = ?) t\n" +
                    "where date = maxdate \n" +
                    "\n" +
                    "UNION\n" +
                    "\n" +
                    "select  product_code , element_code, units, season||' (' ||date|| ')' as date  ,value,flag, notes from national_forecast where  (product_code, season, date) in (\n" +
                    "select   distinct on (product_code  ,season,date)\n" +
                    "product_code  ,season,date\n" +
                    "from (select * from national_forecast where region_code =?\n" +
                    ") as before_last\n" +
                    "where season in (select distinct season from national_forecast where region_code =?\n" +
                    "order by season DESC limit 2) \n" +
                    "and date in (select distinct  date from national_forecast where region_code =? \n" +
                    "and season in ( before_last.season) and product_code in ( before_last.product_code) order by date DESC limit 1 offset 1)\n" +
                    "order by product_code, season DESC, date DESC\n" +
                    ") and region_code = ? order by product_code, date, date DESC";

    private static int[] queryInsertTypes = {Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.REAL, Types.VARCHAR, Types.VARCHAR};

    public Iterator<Object[]> getNationalData(DatasetFilter filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryLoad);

        statement.setInt(1, filter.getRegion().intValue());
        statement.setInt(2, filter.getProduct().intValue());
        statement.setInt(3, filter.getYear().intValue());
        statement.setString(4, "" + filter.getSeason());

        return this.utils.getDataIterator(statement.executeQuery());
    }

    public Iterator<Object[]> getPopulationData(DatasetNationalFilter filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryLoadNational);

        statement.setInt(1, filter.getRegion().intValue());
        statement.setInt(2, filter.getElement().intValue());
        statement.setInt(3, filter.getYear().intValue());

        return this.utils.getDataIterator(statement.executeQuery());
    }

    public void updNationalData(DatasetUpdate data) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        try {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(queryDelete);
            statement.setInt(1, data.getFilter().getRegion().intValue());
            statement.setInt(2, data.getFilter().getProduct().intValue());
            statement.setInt(3, data.getFilter().getYear().intValue());
            statement.setString(4, data.getFilter().getSeason());

            System.out.println("--------------------------------------");
            System.out.println("before delete!!!; statment is : " + statement.toString());

            statement.executeUpdate();


            System.out.println("after delete!!!; statment is : " + statement.toString());
            System.out.println("--------------------------------------");

            if (data.getData() != null) {
                statement = connection.prepareStatement(queryInsert);
                for (Object[] row : data.getData()) {
                    this.utils.fillStatement(statement, queryInsertTypes, new Object[]{data.getFilter().getRegion(), data.getFilter().getProduct(), data.getFilter().getYear(), data.getFilter().getSeason(), data.getFilter().getDatasource(), row[0], row[1], row[2], row[3], row[4], row[5]});

                    statement.addBatch();
                }

                System.out.println("*************************************************************");
                System.out.println("THIS IS THE QUERY: ");
                System.out.println(statement.toString());
                System.out.println("*************************************************************");

                statement.executeBatch();
            }

          /*  statement = connection.prepareStatement(queryFunction);
            statement.execute();*/

            connection.commit();
        } catch (Exception ex) {

            System.out.println("GET NEXT EXCEPTION: ");
            System.out.println("INTO CATCH: the exception is: " + ex.toString());
            System.out.println(((SQLException) ex).getNextException().toString());
            connection.rollback();PreparedStatement statement;
            throw ex;
        } finally {
            connection.setAutoCommit(true);PreparedStatement statement;
        }
    }

    public void updateAnnualForecast(DatasetAnnualForecast data) throws Exception {

        Connection connection = this.connectionManager.getConnection();
        try {
            PreparedStatement statement;
            connection.setAutoCommit(false);
            // Prepare the delete query
            StringBuilder queryToFinish = new StringBuilder().append(queryDeleteAll);
            if (data.getFilters() != null) {
                for (int i = 0, length = data.getFilters().length; i < length; i++) {
                    AnnualFilter filter = data.getFilters()[i];
                    queryToFinish.append(" season = '").append(filter.getSeason().toString())
                            .append("' AND date = '").append(filter.getDate().toString())
                            .append("' AND year = ").append(filter.getYear().intValue())
                            .append(" )");

                    if (i == length - 1) {
                        queryToFinish.append(" )");
                    } else {
                        queryToFinish.append(" OR (");
                    }
                }

                statement = connection.prepareStatement(queryToFinish.toString());
                statement.setInt(1, data.getRegion().intValue());
                statement.setInt(2, data.getProduct());
                statement.executeUpdate();
            }

            if (data.getFilters() != null) {
                statement = connection.prepareStatement(queryInsert);
                for(AnnualFilter filter: data.getFilters()) {
                    for (Object[] row : filter.getData()) {
                        this.utils.fillStatement(statement, queryInsertTypes, new Object[]{data.getRegion(), data.getProduct(), filter.getYear(), filter.getSeason(), data.getDatasource(), row[0], row[1], row[2], row[3], row[4], row[5]});
                        statement.addBatch();
                    }
                }
                statement.executeBatch();
            }
            statement = connection.prepareStatement(queryFunction);
            statement.execute();
            connection.commit();
        } catch (Exception ex) {
            System.out.println("The Exception is: " + ex.toString());
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }


    public void updNationalDataPreviousYear(DatasetUpdatePrevSeason data) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        try {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(queryDeletePrevYear);
            statement.setInt(1, data.getFilter().getRegion().intValue());
            statement.setInt(2, data.getFilter().getProduct().intValue());
            statement.setInt(3, data.getFilter().getYear().intValue());
            statement.setString(4, data.getFilter().getSeason());
            statement.setString(5, data.getFilter().getDate());
            statement.setString(6, data.getFilter().getDatasource());
            statement.executeUpdate();

            if (data.getData() != null) {
                statement = connection.prepareStatement(queryInsert);
                for (Object[] row : data.getData()) {
                    this.utils.fillStatement(statement, queryInsertTypes, new Object[]{data.getFilter().getRegion(), data.getFilter().getProduct(), data.getFilter().getYear(), data.getFilter().getSeason(), data.getFilter().getDatasource(), row[0], row[1], row[2], row[3], row[4], row[5]});

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

    public Iterator<Object[]> getMostRecentForecastDate(DatasetFilter filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryMostRecentDate);

        statement.setInt(1, filter.getRegion().intValue());
        statement.setInt(2, filter.getProduct().intValue());
        statement.setInt(3, filter.getYear().intValue());

        return this.utils.getDataIterator(statement.executeQuery());
    }

    public Iterator<Object[]> getAnnualExportData(FilterExport filter) throws Exception {
        Connection connection = this.connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(queryExportTotalAnnual);

        Integer[] regionCodes = filter.getRegionCode();
        for (int i = 0; i < regionCodes.length; i++)
            statement.setInt(i + 1, regionCodes[i].intValue());

        return this.utils.getDataIterator(statement.executeQuery());
    }

}