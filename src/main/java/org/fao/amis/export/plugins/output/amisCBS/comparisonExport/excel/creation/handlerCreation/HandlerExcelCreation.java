package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.handlerCreation;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator.DataCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.daoValue.DaoForecastValue;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.forecast.Forecast;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.natMarkBean.NationalMarketingBean;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.query.AMISQuery;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.utils.commodity.CommodityParser;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.creator.SheetCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils.AmisExcelUtils;

import java.util.*;


public class HandlerExcelCreation {

    private SheetCreator sheetCreator;

    private Map<String, String> mapColumnsToView;

    private List<String> datesList; // oreder ASC


    private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(HandlerExcelCreation.class);


    public HandlerExcelCreation() {
        this.sheetCreator = new SheetCreator();
    }


    public HSSFWorkbook init(Forecast forecast, AMISQuery qvo, DataCreator dataModel, HashMap<String,NationalMarketingBean> marketingYearMap) {

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



            if(foodBalanceResults.size() != 0) {
                createColumnVisualizationType(foodBalanceResults);

                rowCounter = this.sheetCreator.createHeadersGroup(rowCounter, sheet, workbook, foodBalanceResults, "foodBalance", this.mapColumnsToView, marketingYearMap.get(commodityString).getNmyMonths());

                // list of elements to show on the left
                HashMap<Integer, HashMap<Integer, String>> elements = qvo.getFoodBalanceElements();

                // put on the excel the elements and the values
                int rowUM = rowCounter + 1;
                int columnUM = 1;
                // Cell cellUM = sheet.createRow(rowUM).createCell((short) columnUM);
//              this.sheetCreator.putMeasurementUnitValues(elements.get(commodity), "national", columnUM, rowCounter, sheet);
                rowCounter = this.sheetCreator.createDataTableGroup(rowCounter, sheet, workbook, elements.get(commodity), foodBalanceResults, this.mapColumnsToView);

                Row row = sheet.createRow(9);
                Cell cell = row.createCell((short) 1);
                cell.setCellStyle(AmisExcelUtils.getCenterAlignmentStyle());
                cell.setCellValue("Thousand tonnes");
                int endNmy = 9+ elements.get(commodity).size();
                CellRangeAddress region =new CellRangeAddress(9,endNmy-1,1,1);
                sheet.addMergedRegion(region);

                rowCounter++;

                rowCounter = this.sheetCreator.createFooterMarketingYear(rowCounter, sheet, workbook, marketingYearMap.get(commodityString), "national", this.mapColumnsToView);

                rowCounter++;

              /*
                -------------------------    ITY RESULTS   -------------------------------------------------
             */

                LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> ityResults = forecast.getItyResults().get(commodityString);

                rowCounter = this.sheetCreator.createHeadersGroup(rowCounter, sheet, workbook, ityResults, "international", this.mapColumnsToView, marketingYearMap.get(commodityString).getItyMonths());

                // list of elements to show on the left
                HashMap<Integer, HashMap<Integer, String>> elementsITY = qvo.getItyElements();

                // put on the excel the elements and the values
           //     this.sheetCreator.putMeasurementUnitValues(elementsITY.get(commodity), "international", 1, rowCounter, sheet);
                rowCounter = this.sheetCreator.createDataTableGroup(rowCounter, sheet, workbook, elementsITY.get(commodity), ityResults, this.mapColumnsToView);

                int startIty = endNmy+ 7;
                Row rowIt = sheet.getRow(startIty - 1);
                Cell cellIt1 = rowIt.createCell((short) 1);
                cellIt1.setCellValue("Thousand tonnes");
                Row rowIt2 = sheet.getRow(startIty);
                Cell cellIt2 = rowIt2.createCell((short) 1);
                cellIt2.setCellValue("Thousand tonnes");
                int startOth = startIty+7;

                rowCounter++;

                rowCounter = this.sheetCreator.createFooterMarketingYear(rowCounter, sheet, workbook, marketingYearMap.get(commodityString), "ity", this.mapColumnsToView);

                rowCounter++;
              /*
                -------------------------    OTHERS   -------------------------------------------------
             */

                LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> otherResults = forecast.getOtherResults().get(commodityString);

                rowCounter = this.sheetCreator.createHeadersGroup(rowCounter, sheet, workbook, otherResults, "others", this.mapColumnsToView, null);

                // list of elements to show on the left
                HashMap<Integer, HashMap<Integer, String>> elementsOTH = qvo.getOtherElements();

                // put on the excel the elements and the values
             //   this.sheetCreator.putMeasurementUnitValues(elementsOTH.get(commodity), "others", 1, rowCounter, sheet);
                rowCounter = this.sheetCreator.createDataTableGroup(rowCounter, sheet, workbook, elementsOTH.get(commodity), otherResults, this.mapColumnsToView);
                Row rowOth;
                Cell cellOth;
                if(elementsOTH.get(commodity).size()>4){

                    rowOth = sheet.getRow(startOth );
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("1000s");


                    rowOth = sheet.getRow(startOth +1);
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("Thousand Ha");


                    rowOth = sheet.getRow(startOth +2);
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("%");


                    rowOth = sheet.getRow(startOth +3);
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("Tonnes/Ha");


                    rowOth = sheet.getRow(startOth +4);
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("Thousand Ha");

                }else{

                    rowOth = sheet.getRow(startOth );
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("1000s");


                    rowOth = sheet.getRow(startOth +1);
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("Thousand Ha");


                    rowOth = sheet.getRow(startOth +2);
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("Tonnes/Ha");


                    rowOth = sheet.getRow(startOth +3);
                    cellOth = rowOth.createCell((short) 1);
                    cellOth.setCellValue("Thousand Ha");

                }
                sheet.createFreezePane(2, 0, 2, 2);
            }else {
                this.sheetCreator.createNoDataAvailable(workbook, sheet);
            }

        }


        return workbook;
    }


    private void createColumnVisualizationType ( LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> forecastsForCommodity ){

        datesList = new ArrayList<String>();

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
            if(datesSize - counter >=0) {
                otherDate = datesList.get(datesSize - counter);
                year = Integer.parseInt(otherDate.substring(0, 4));
            }else{
                year--;
            }
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




