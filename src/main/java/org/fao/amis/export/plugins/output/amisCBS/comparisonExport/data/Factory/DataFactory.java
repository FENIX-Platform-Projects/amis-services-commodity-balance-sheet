package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.Factory;


import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator.DataCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.forecast.Forecast;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.formula.FormulaHandler;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.natMarkBean.HarvestBean;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.natMarkBean.NationalMarketingBean;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.query.AMISQuery;

import java.io.IOException;
import java.util.*;


public class DataFactory {

    private final int[] ORDER_MYSERVICE = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private DataCreator fakeConstructor;

    private HashMap<String, NationalMarketingBean> mapMarketingYear;

    private FormulaHandler formulaHandler;

    private AMISQuery qvo;

    private Forecast forecasts;

    public DataFactory(ArrayList<Object[]> data, LinkedHashMap filterData, ArrayList<Object[]> nationalMarkData) {

        formulaHandler = new FormulaHandler();
        try {

            this.fakeConstructor = new DataCreator(data, filterData);

            createMarketingYearMap(nationalMarkData);

            qvo = new AMISQuery();
            qvo.init();

            forecasts = new Forecast();
            forecasts.initData(this.fakeConstructor.getData());

            formulaHandler.createCalculatedModel(forecasts.getUnorderedMap());

            forecasts.createOrderedMaps(qvo.getFoodBalanceElements(), "national");
            forecasts.createOrderedMaps(qvo.getItyElements(), "international");
            forecasts.createOrderedMaps(qvo.getOtherElements(), "others");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public DataCreator getDataCreatorIstance() {
        return this.fakeConstructor;
    }


    public AMISQuery getAMISQueryIstance() {
        return this.qvo;
    }


    public Forecast getForecastIstance() {
        return this.forecasts;
    }


    private void createMarketingYearMap(ArrayList<Object[]> data) {

        this.mapMarketingYear = new HashMap<String, NationalMarketingBean>();
        Iterator<?> iterator = data.iterator();
        while (iterator.hasNext()) {
            Object[] row = ((Collection)iterator.next()).toArray();
            NationalMarketingBean temp = new NationalMarketingBean(
                    row[ORDER_MYSERVICE[0]].toString(), row[ORDER_MYSERVICE[1]].toString(),
                    row[ORDER_MYSERVICE[2]].toString(), row[ORDER_MYSERVICE[3]].toString(),
                    row[ORDER_MYSERVICE[4]].toString(), row[ORDER_MYSERVICE[5]].toString(),
                    new HarvestBean(
                            row[ORDER_MYSERVICE[6]].toString(), row[ORDER_MYSERVICE[7]].toString(),
                            row[ORDER_MYSERVICE[7]].toString(), row[ORDER_MYSERVICE[9]].toString()
                    )
            );
            this.mapMarketingYear.put(row[0].toString(), temp);

        }
    }


    public HashMap<String, NationalMarketingBean> getMapMarketingYear(){
        return  this.mapMarketingYear;
    }
}
