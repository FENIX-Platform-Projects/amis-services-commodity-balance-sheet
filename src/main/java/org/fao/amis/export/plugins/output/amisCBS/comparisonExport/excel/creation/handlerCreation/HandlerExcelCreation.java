package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.handlerCreation;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator.DataCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.daoValue.DaoForecastValue;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.forecast.Forecast;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.natMarkBean.NationalMarketingBean;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.query.AMISQuery;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.utils.commodity.CommodityParser;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.creator.SheetCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils.AmisExcelUtils;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils.AmisStaticTables;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils.ElementStyles2;

import java.util.*;


public class HandlerExcelCreation {

    private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(HandlerExcelCreation.class);
    private SheetCreator sheetCreator;
    private Map<String, String> mapColumnsToView;
    private List<String> datesList; // order ASC
    private final static String[] OTH_UM_RICE = {"1000s", "Thousand Ha", "Thousand Ha", "Tonnes/Ha", "%", "Kg/Yr"};
    private final static String[] OTH_UM_OTH_COMM = {"1000s", "Thousand Ha", "Thousand Ha","Tonnes/Ha",  "Kg/Yr"};
    private final String THOUSAND_TONNES_ITY = "Thousand \n tonnes";
    private final String THOUSAND_TONNES_REGION = "                 Thousand tonnes";

    private final static int NMY_START_ROW = 9;
    private final static int SPACE_SECTIONS = 6;
    private final String NATIONAL = "foodBalance";
    private final String INTERNATIONAL = "international";
    private final String OTHERS = "others";
    private AmisStaticTables amisStaticTables;
    private ElementStyles2 elementStyles2;
    private AmisExcelUtils amisExcelUtils;


    public HandlerExcelCreation() {
        this.sheetCreator = new SheetCreator();
    }


    /**
     * Create the workbook, composed by sheet (one for each commodity)
     * @param forecast
     * @param qvo
     * @param dataModel
     * @param marketingYearMap
     * @return Workbook created
     */
    public HSSFWorkbook init(Forecast forecast, AMISQuery qvo, DataCreator dataModel, HashMap<String, NationalMarketingBean> marketingYearMap) {

        amisExcelUtils = new AmisExcelUtils();
        // create the Excel file
        HSSFWorkbook workbook = new HSSFWorkbook();

        amisExcelUtils.initStyles((HSSFWorkbook) workbook);

        //Initialize font
        elementStyles2 = new ElementStyles2(amisExcelUtils);
        elementStyles2.init();
        amisStaticTables = new AmisStaticTables(workbook);
        sheetCreator.init(elementStyles2, amisExcelUtils);

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
            rowCounter = this.sheetCreator.createSheetTitle(rowCounter, sheet, workbook);


            /* body part (mutable) */
            /*    -------------------------    FoodBalance   ------------------------------------------------*/

            LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> foodBalanceResults = forecast.getFoodBalanceResults().get(commodityString);

            if (foodBalanceResults.size() != 0) {
                createColumnVisualizationType(foodBalanceResults);

                rowCounter = this.sheetCreator.createHeadersGroup(rowCounter, sheet, workbook, foodBalanceResults, NATIONAL, this.mapColumnsToView, marketingYearMap.get(commodityString).getNmyMonths());

                // list of elements to show on the left
                HashMap<Integer, LinkedHashMap<Integer, String>> elements = qvo.getFoodBalanceElements();
                rowCounter = this.sheetCreator.createDataTableGroup(rowCounter, sheet, elements.get(commodity), foodBalanceResults, this.mapColumnsToView);

                // for UM column ONLY
                Row row = sheet.getRow(NMY_START_ROW)== null? sheet.createRow(NMY_START_ROW):sheet.getRow(NMY_START_ROW);
                Cell cell = row.createCell((short) 1);
                cell.setCellStyle(AmisExcelUtils.getBasicWithBordersOnRegion());


                cell.setCellValue(THOUSAND_TONNES_REGION);
                int endNmy = NMY_START_ROW + elements.get(commodity).size();
                CellRangeAddress region = new CellRangeAddress(NMY_START_ROW, endNmy - 1, 1, 1);
                AmisExcelUtils.setRegionBorders(region,sheet,workbook);

                sheet.addMergedRegion(region);

                rowCounter++;
                rowCounter = this.sheetCreator.createFooterMarketingYear(rowCounter, sheet, workbook, marketingYearMap.get(commodityString), "national", this.mapColumnsToView);
                rowCounter++;

              /*
                -------------------------    ITY RESULTS   -------------------------------------------------
             */

                LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> ityResults = forecast.getItyResults().get(commodityString);

                rowCounter = this.sheetCreator.createHeadersGroup(rowCounter, sheet, workbook, ityResults, INTERNATIONAL, this.mapColumnsToView, marketingYearMap.get(commodityString).getItyMonths());

                // list of elements to show on the left
                HashMap<Integer, LinkedHashMap<Integer, String>> elementsITY = qvo.getItyElements();

                // put on the excel the elements and the values
                rowCounter = this.sheetCreator.createDataTableGroup(rowCounter, sheet, elementsITY.get(commodity), ityResults, this.mapColumnsToView);

                int startIty = endNmy + SPACE_SECTIONS;
                Row rowIt = sheet.getRow(startIty - 1)== null? sheet.createRow(startIty - 1): sheet.getRow(startIty - 1);
                Cell cellIt1 = rowIt.createCell((short) 1);
                CellStyle styleWrap = workbook.createCellStyle();
                styleWrap.setWrapText(true);
                styleWrap.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                cellIt1.setCellStyle(styleWrap);
                cellIt1.setCellValue(THOUSAND_TONNES_ITY);
                CellRangeAddress regionITY = new CellRangeAddress(startIty-1, startIty , 1, 1);
                sheet.addMergedRegion(regionITY);
                AmisExcelUtils.setRegionBorders(regionITY,sheet,workbook);


                int startOth = startIty + SPACE_SECTIONS;

                rowCounter++;
                rowCounter = this.sheetCreator.createFooterMarketingYear(rowCounter, sheet, workbook, marketingYearMap.get(commodityString), "ity", this.mapColumnsToView);
                rowCounter++;
              /*
                -------------------------    OTHERS   -------------------------------------------------
             */

                LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> otherResults = forecast.getOtherResults().get(commodityString);

                rowCounter = this.sheetCreator.createHeadersGroup(rowCounter, sheet, workbook, otherResults, OTHERS, this.mapColumnsToView, null);

                // list of elements to show on the left
                HashMap<Integer, LinkedHashMap<Integer, String>> elementsOTH = qvo.getOtherElements();

                // put on the excel the elements and the values
                rowCounter = this.sheetCreator.createDataTableGroup(rowCounter, sheet, elementsOTH.get(commodity), otherResults, this.mapColumnsToView);
                Row rowOth;
                Cell cellOth;
                if (elementsOTH.get(commodity).size() > 4) {
                    String[] measuremntUnits = (elementsOTH.get(commodity).size() > 5) ? OTH_UM_RICE : OTH_UM_OTH_COMM;

                    for (int count = 0; count < elementsOTH.get(commodity).size(); count++) {
                        rowOth = sheet.getRow(startOth + count)== null? sheet.createRow(startOth + count): sheet.getRow(startOth + count);;;
                        cellOth = rowOth.createCell((short) 1);
                        cellOth.setCellStyle((AmisExcelUtils.getBasicWithBorders()));
                        cellOth.setCellValue(measuremntUnits[count]);
                    }
                    sheet.createFreezePane(2, 0, 2, 2);
                } else {
                    this.sheetCreator.createNoDataAvailable(workbook, sheet);
                }
            }

            amisStaticTables.buildTable(sheet, rowCounter, workbook);
            AmisExcelUtils.setLandscapeAndFitOnePg(sheet);
        }

        return workbook;
    }


    /**
     * Create a map that establish a way to how visualize each date in the sheet
     * Type of visualization admitted : complete, showOnly, show, hidden
     * @param forecastsForCommodity , an list ordered by date
     */
    private void createColumnVisualizationType(LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> forecastsForCommodity) {

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

        int index = (datesSize-counter>0)? datesSize-counter: 0;

        String otherDate = datesList.get(index);

        int year = Integer.parseInt(otherDate.substring(0, 4));

        int tmpYear = lastYear;

        while (year >= lastYear - 1) {
            // put date
            if (year == tmpYear) {
                mapColumnsToView.put(otherDate, "show");
            } else {
                mapColumnsToView.put(otherDate, "complete");
                tmpYear = year;
            }
            // update
            counter++;
            if (datesSize - counter >= 0) {
                otherDate = datesList.get(datesSize - counter);
                year = Integer.parseInt(otherDate.substring(0, 4));
            } else {
                year--;
            }
        }

        mapColumnsToView.put(otherDate, "showOnly");
        counter++;

        while (counter <= datesSize) {
            mapColumnsToView.put(datesList.get(datesSize - counter), "hidden");
            counter++;
        }
    }


    public Map<String, String> getMapColumnsToView() {
        return mapColumnsToView;
    }


}




