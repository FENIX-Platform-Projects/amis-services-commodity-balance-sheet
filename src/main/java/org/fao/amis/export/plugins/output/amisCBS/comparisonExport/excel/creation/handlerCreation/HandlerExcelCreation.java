package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.handlerCreation;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator.DataCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.daoValue.DaoForecastValue;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.forecast.Forecast;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.query.AMISQuery;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.utils.commodity.CommodityParser;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.creator.SheetCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils.AmisExcelUtils;

import java.util.*;


public class HandlerExcelCreation {

    private SheetCreator sheetCreator;

    private Map<String, String> mapColumnsToView;


    private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(HandlerExcelCreation.class);


    public HandlerExcelCreation() {
        this.sheetCreator = new SheetCreator();
    }


    public HSSFWorkbook init(Forecast forecast, AMISQuery qvo, DataCreator dataModel) {

        // create the Excel file
        HSSFWorkbook workbook = new HSSFWorkbook();
        AmisExcelUtils.initStyles((HSSFWorkbook) workbook);

        //Initialize font
        AmisExcelUtils.initializeHSSFFontStyles(workbook);

        int[] commodityList = forecast.getCommodityList();


        CommodityParser commParser = new CommodityParser();


        // for each sheet
        for (int commodity : commodityList) {

            String commodityString = "" + commodity;


            String commodityLabel = commParser.getCommodityLabel(commodityString);

            Sheet sheet = workbook.createSheet(commodityLabel);

            int rowCounter = 0;

            /* summary title (immutable ) */
            rowCounter = this.sheetCreator.createSummary(rowCounter, sheet, workbook, dataModel, commodityLabel);
            rowCounter = this.sheetCreator.createSheetTitle(rowCounter,sheet,workbook);


            /* body part (mutable) */

            /*
                -------------------------    FoodBalance   -------------------------------------------------
             */


            LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> foodBalanceResults = forecast.getFoodBalanceResults().get(commodityString);


            createColumnVisualizationType(foodBalanceResults);


            rowCounter = this.sheetCreator.createHeadersGroup(rowCounter,sheet,workbook, foodBalanceResults, "foodBalance",this.mapColumnsToView);

            // list of elements to show on the left
            HashMap< Integer, HashMap<Integer, String>> elements =  qvo.getFoodBalanceElements();

            // put on the excel the elements and the values
            rowCounter = this.sheetCreator.createDataTableGroup(rowCounter,sheet,workbook,elements.get(commodity), foodBalanceResults, this.mapColumnsToView);

            rowCounter++;

              /*
                -------------------------    ITY RESULTS   -------------------------------------------------
             */

            LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> ityResults = forecast.getItyResults().get(commodityString);

            rowCounter = this.sheetCreator.createHeadersGroup(rowCounter,sheet,workbook, ityResults, "international",this.mapColumnsToView);

            // list of elements to show on the left
            HashMap< Integer, HashMap<Integer, String>> elementsITY =  qvo.getItyElements();

            // put on the excel the elements and the values
            rowCounter = this.sheetCreator.createDataTableGroup(rowCounter,sheet,workbook,elementsITY.get(commodity), ityResults, this.mapColumnsToView);

            rowCounter++;
              /*
                -------------------------    OTHERS   -------------------------------------------------
             */

            LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> otherResults = forecast.getOtherResults().get(commodityString);

            rowCounter = this.sheetCreator.createHeadersGroup(rowCounter,sheet,workbook, otherResults, "others",this.mapColumnsToView);

            // list of elements to show on the left
            HashMap< Integer, HashMap<Integer, String>> elementsOTH =  qvo.getOtherElements();

            // put on the excel the elements and the values
            rowCounter = this.sheetCreator.createDataTableGroup(rowCounter,sheet,workbook,elementsOTH.get(commodity), otherResults,this.mapColumnsToView);

            sheet.createFreezePane(2,0,2,2);

        }


        return workbook;
    }


    private void createColumnVisualizationType ( LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> forecastsForCommodity ){

        List<String> datesList = new ArrayList<String>();

        datesList.addAll(forecastsForCommodity.keySet());

        // LAST value
        int counter = 1;
        int datesSize = datesList.size();
        mapColumnsToView = new HashMap<String, String>();
        String lastDate = datesList.get(datesSize - counter);
        mapColumnsToView.put(lastDate, "complete");
        counter++;

        int lastYear = Integer.parseInt(lastDate.substring(0, 4));

        String otherDate = datesList.get(datesSize - counter);

        int year = Integer.parseInt(otherDate.substring(0, 4));

        int tmpYear = lastYear;

        while(year >= lastYear-1){
            // put date
            if(year == tmpYear){
                mapColumnsToView.put(otherDate, "show");
            }else{
                mapColumnsToView.put(otherDate, "complete");
                tmpYear = year;
            }
            // update
            counter++;
            otherDate = datesList.get(datesSize - counter);
            year = Integer.parseInt(otherDate.substring(0, 4));
        }

        mapColumnsToView.put(otherDate, "showOnly");
        counter++;

        while(counter <= datesSize){
            mapColumnsToView.put(datesList.get(datesSize - counter), "hidden");
            counter++;
        }

        LOGGER.error(mapColumnsToView.toString());

    }


    public Map<String, String> getMapColumnsToView() { return mapColumnsToView; }


}




