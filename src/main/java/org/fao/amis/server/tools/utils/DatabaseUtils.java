package org.fao.amis.server.tools.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class DatabaseUtils
{
    public void fillStatement(PreparedStatement statement, int[] types, Object[] row)
            throws SQLException
    {
        for (int i = 0; i < row.length; i++)
            if (row[i] != null)
                statement.setObject(i + 1, row[i], types[i]);
            else
                statement.setNull(i + 1, types[i]);
    }

    public DatasourceObject getDataSource(ResultSet source) throws SQLException
    {
        DatasourceObject result = new DatasourceObject();
        while (source.next()) {
            result.setDatasource(source.getString(1));
        }

        return result;
    }

    public ArrayList<YearObject> getYear(ResultSet source) throws SQLException
    {
        ArrayList result = new ArrayList();
        while (source.next()) {
            YearObject yearObject = new YearObject();
            yearObject.setYearLabel(source.getString(1));
            yearObject.setYear(Integer.valueOf(source.getInt(2)));
            result.add(yearObject);
        }
        return result;
    }

    public String getCrops(ResultSet source) throws SQLException
    {
        String result = null;
        if (source.next()) {
            result = source.getString(1);
        }
        return result;
    }

    public Iterator<Object[]> getDataIterator(final ResultSet source)
            throws SQLException
    {
        return new Iterator() {
            private int columnsNumber = source.getMetaData().getColumnCount();
            private Object[] next = null;
            private boolean consumed = true;

            private Object[] loadNext() throws SQLException {
                if (this.consumed) {
                    this.consumed = false;
                    if (source.next()) {
                        this.next = new Object[this.columnsNumber];
                        for (int i = 0; i < this.columnsNumber; i++)
                            this.next[i] = source.getObject(i + 1);
                    } else {
                        this.next = null;
                    }
                }
                return this.next;
            }

            public boolean hasNext()
            {
                try {
                    return loadNext() != null;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            public Object[] next()
            {
                Object[] row = null;
                try {
                    row = loadNext();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                this.consumed = true;
                return row;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
}