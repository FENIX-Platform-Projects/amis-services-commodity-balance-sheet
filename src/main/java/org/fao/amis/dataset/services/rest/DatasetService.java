package org.fao.amis.dataset.services.rest;

import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import org.fao.amis.dataset.dao.DatasetData;
import org.fao.amis.dataset.dao.FilterData;
import org.fao.amis.dataset.dao.SupportData;
import org.fao.amis.dataset.dto.DatasetFilter;
import org.fao.amis.dataset.dto.DatasetFilterWithDate;
import org.fao.amis.dataset.dto.DatasetNationalFilter;
import org.fao.amis.dataset.dto.DatasetUpdate;
import org.fao.amis.dataset.dto.DatasetUpdatePrevSeason;
import org.fao.amis.dataset.dto.DateFilter;
import org.fao.amis.dataset.dto.FilterCrops;
import org.fao.amis.dataset.dto.FilterDatabase;
import org.fao.amis.dataset.dto.FilterYear;
import org.fao.amis.server.tools.utils.DatasourceObject;
import org.fao.amis.server.tools.utils.YearObject;

@Path("dataset")
@Produces({"application/json"})
@Consumes({"application/json"})
public class DatasetService
{

    @Inject
    private DatasetData dao;

    @Inject
    private SupportData dao2;

    @Inject
    private FilterData filterDao;

    @Inject
    private FilterCrops filterCrops;

    @POST
    @Consumes({"application/json"})
    @Path("national")
    public Iterator<Object[]> getData(DatasetFilter filter)
            throws Exception
    {
        return this.dao.getNationalData(filter);
    }

    @PUT
    @Path("national")
    public void getData(DatasetUpdate data) throws Exception {
        this.dao.updNationalData(data);
    }

    @PUT
    @Path("previous/national")
    public void getData(DatasetUpdatePrevSeason data) throws Exception {
        this.dao.updNationalDataPreviousYear(data);
    }

    @POST
    @Path("population")
    public Iterator<Object[]> getData(DatasetNationalFilter filter) throws Exception {
        return this.dao.getPopulationData(filter);
    }

    @POST
    @Path("recentDate")
    public Iterator<Object[]> getData(DateFilter filter) throws Exception {
        return this.dao2.getMostRecentForecastDate(filter);
    }

    @POST
    @Path("previousYear")
    public Iterator<Object[]> getData(DatasetFilterWithDate filter) throws Exception {
        return this.dao2.getPreviousYearForecast(filter);
    }

    @POST
    @Path("datasource")
    public DatasourceObject getData(FilterDatabase filter) throws Exception {
        return this.filterDao.getDatabase(filter);
    }

    @POST
    @Path("year")
    public ArrayList<YearObject> getData(FilterYear filter) throws Exception {
        return this.filterDao.getYears(filter);
    }

    @POST
    @Path("crops")
    public String getData(FilterCrops filter) throws Exception {
        return this.dao2.getCropsNumber(filter);
    }

    /*

    @POST
    @Path("export")
    @Produces("application/vnd.ms-excel")
    public Response getExcel(FilterExcel  filter) { return this.dao3.build();}

*/

    }