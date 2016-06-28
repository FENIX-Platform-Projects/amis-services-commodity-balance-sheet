package org.fao.amis.dataset.services.rest;

import org.fao.amis.dataset.dao.DatasetData;
import org.fao.amis.dataset.dao.FilterData;
import org.fao.amis.dataset.dao.SupportData;
import org.fao.amis.dataset.dto.*;
import org.fao.amis.server.tools.utils.DatasourceObject;
import org.fao.amis.server.tools.utils.YearObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Iterator;

@Path("dataset")
@Produces(MediaType.APPLICATION_JSON+"; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
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
    @Consumes(MediaType.APPLICATION_JSON)
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

    @PUT
    @Path("annual/national")
    public void getData(DatasetAnnualForecast data) throws Exception {
        this.dao.updateAnnualForecast(data);
    }

    @POST
    @Path("population")
    public Iterator<Object[]> getData(DatasetNationalFilter filter) throws Exception {
        return this.dao.getPopulationData(filter);
    }

    @POST
    @Path("recentDate")
    public Iterator<Object[]> getData(MostRecentDateFilter filter) throws Exception {
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


    @POST
    @Path("populationData")
    public Iterator<Object[]> getData(PopulationFilter filter) throws Exception { return this.dao2.getPopulationData(filter);}


    @PUT
    @Path("populationData")
    public void getData(PopulationDataset filter) throws Exception {  this.dao2.updPopulationData(filter);}


    @POST
    @Path("annualExport")
    public Iterator<Object[]> getData(FilterExport filter) throws Exception { return this.dao.getAnnualExportData(filter); }

    @POST
    @Path("mostRecentDateForYears")
    public ArrayList<MostRecentDateForProductResult> getMostRecentDataForProduct (RegionCodeFilter regionCode)throws Exception { return this.dao.getMostRecentDateForProduct(regionCode); }


    /*




    @PUT

    @POST
    @Path("export")
    @Produces("application/vnd.ms-excel")
    public Response getExcel(FilterExcel  filter) { return this.dao3.build();}

*/

    }