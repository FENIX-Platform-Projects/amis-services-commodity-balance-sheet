package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.creator;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.configuration.URLGetter;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator.DataCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.daoValue.DaoForecastValue;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.natMarkBean.NationalMarketingBean;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils.*;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.formula.translator.CellMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.util.*;

public class SheetCreator {

    private static final Logger LOGGER = Logger.getLogger(SheetCreator.class);
    private final String NATIONAL = "foodBalance";
    private final String INTERNATIONAL = "international";
    private final String NATIONAL_TITLE = "National Marketing Year (NMY):";
    private final String INTERNATIONAL_TITLE = "International Trade Year (ITY):";
    private final String OTHERS_TITLE = "Others";
    private final String FLAG_HEADER = "Forecasting \n Methodology";
    private final String NOTES_HEADER = "Notes";
    private HashMap<String, StylesFont> mapStyles;
    private static final int ROW_START_ELEMENTS = 9;
    private static final String EVERY_SHEETS_TITLE = "amis commodity forecasts";
    private String[] nationalCodes;
    private String lastSeason, commodityChosen;
    private int rowStartingITY;
    private AmisExcelUtils amisExcelUtils;
    private CellMapper cellMappers;
    private URLGetter urlGetter;
    private ElementStyles2 elementStyles2;
    private ArrayList<Integer> firstYearSeason;
    private static  final HashMap<Integer,Integer>  SPACE_ELEMENT = new HashMap<Integer,Integer>(){
        {put(13,1); put(14,1); put(15,1); put(36,1);
            put(21,2);put(34,2);put(28,2);
            put(999,3); put(19, 3); put(35,3);
        }
    };


    public SheetCreator() {
        urlGetter = new URLGetter();
    }

    public void init(ElementStyles2 elementStyles2, AmisExcelUtils amisExcelUtils) {
        this.elementStyles2 = elementStyles2;
        this.amisExcelUtils = amisExcelUtils;

    }


    /**
     * Create the summary
     * @param rowCounter
     * @param sheet
     * @param workbook
     * @param dataCreator
     * @param commodityLabel
     * @return
     */
    public int createSummary(int rowCounter, Sheet sheet, HSSFWorkbook workbook, DataCreator dataCreator, String commodityLabel) {

        //Create Date Last Updated
        String commodity = commodityLabel;
        this.commodityChosen = commodityLabel;
        lastSeason = dataCreator.getSeason();
        String dataSource = dataCreator.getDatasource();
        String country = dataCreator.getCountry();
        HashMap<String,String> mapProductDate = dataCreator.getMapProductDate();
        sheet.setColumnWidth(1, 3000);

        rowCounter = createLegendRow(rowCounter, sheet, workbook, "COUNTRY: ", country);
        rowCounter = createLegendRow(rowCounter, sheet, workbook, "COMMODITY: ", commodity);
        rowCounter = createLegendRow(rowCounter, sheet, workbook, "LAST SEASON: ", lastSeason);
        rowCounter = createLegendRow(rowCounter, sheet, workbook, "DATASOURCE: ", dataSource);
        rowCounter = createLegendRow(rowCounter, sheet, workbook, "Data Last Updated on: ", mapProductDate.get(commodity));


        rowCounter = amisExcelUtils.createEmptyRow(rowCounter, sheet, workbook);

        return rowCounter;
    }

    /**
     *Create the legend with country, commodity, last season, and datasource
     * @param rowCounter
     * @param sheet
     * @param workbook
     * @param header
     * @param headerValue
     * @return
     */
    private int createLegendRow(int rowCounter, Sheet sheet, HSSFWorkbook workbook, String header, String headerValue) {
        Row row = sheet.createRow(rowCounter++);

        if (header != null && headerValue == null) {
            Cell cell = row.createCell((short) 0);
            cell.setCellStyle(amisExcelUtils.getBoldTextCellStyle(workbook, null));
            cell.setCellValue(header);

            row.createCell((short) 1).setCellValue("");
        } else {

            Cell cell = row.createCell((short) 0);
            cell.setCellStyle(amisExcelUtils.getRightAlignmentStyle());
            cell.setCellValue(header);

            cell = row.createCell((short) 1);
            cell.setCellStyle(amisExcelUtils.getBoldTextCellStyle(workbook, null));
            cell.setCellValue(headerValue);
        }

        return rowCounter;
    }

    /**
     * Create the title of each sheet
     * @param rowCounter
     * @param sheet
     * @param workbook
     * @return
     */
    public int createSheetTitle(int rowCounter, Sheet sheet, HSSFWorkbook workbook) {
        Row row = sheet.createRow(rowCounter++);
        this.cellMappers = new CellMapper();
        Cell cell = row.createCell((short) 0);
        cell.setCellStyle(amisExcelUtils.getBoldTextCellStyle(workbook, null));
        cell.setCellValue(EVERY_SHEETS_TITLE.toUpperCase());
        sheet.autoSizeColumn(0);
        rowCounter++;

        return rowCounter;
    }


    /**
     * Create the header of each forecast
     * @param rowCounter
     * @param sheet
     * @param workbook
     * @param mapGroup
     * @param type
     * @param mapColumnsToView
     * @param months
     * @return
     */
    public int createHeadersGroup(int rowCounter, Sheet sheet, HSSFWorkbook workbook,
                                  LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> mapGroup, String type,
                                  Map<String, String> mapColumnsToView, String months) {

        String title;


        if (type == NATIONAL) {
            title = NATIONAL_TITLE;
            this.nationalCodes = urlGetter.getOperandsFormulaNational();
        } else if (type == INTERNATIONAL) {
            title = INTERNATIONAL_TITLE;
        } else {
            title = OTHERS_TITLE;
            this.nationalCodes = urlGetter.getOperandsFormulaOthers();
        }

        int columnNumber = 0;

        Row row = sheet.createRow(rowCounter++);
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(amisExcelUtils.getBoldTextWithVerticalAl(workbook, null));
        cell.setCellValue(title);
        sheet.setColumnWidth(100, columnNumber);

        //MARKETING YEAR
        if (months != null) {
            columnNumber = putMarketingYear(sheet, workbook, row, columnNumber, months);
            columnNumber++;
        } else {
            columnNumber += 2;
        }
        Object[] dates = mapGroup.keySet().toArray();

        firstYearSeason = (ArrayList) createSeasonOrderedArray(dates);

        for (int i = 0; i < dates.length; i++) {
            String date = dates[i].toString();
            String resultColumn = mapColumnsToView.get(dates[i].toString());
            switch (resultColumn) {
                case "show":
                    columnNumber = createHeadersValuesSimpleShow(date, columnNumber, row, sheet);
                    break;

                case "showOnly":
                    columnNumber = createHeadersValuesSimpleShowOnly(date, columnNumber, row, sheet);
                    break;

                case "complete":
                    columnNumber = createHeadersValuesWithFlagsAndComments(date, columnNumber, row, (HSSFWorkbook) workbook, sheet);
                    break;

                default:
                    columnNumber = createHeadersValuesSimpleHidden(date, columnNumber, row, sheet);
                    break;
            }
        }
        rowCounter++;
        return rowCounter;
    }


    /**
     * Create the body of the sheet, including the list of elements and the forecasts
     * @param rowCounter
     * @param sheet
     * @param elements
     * @param foodBalanceResults
     * @param mapColumnsToView
     * @return the number of column used
     */
    public int createDataTableGroup(int rowCounter, Sheet sheet,
                                    HashMap<Integer, String> elements,
                                    LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> foodBalanceResults,
                                    Map<String, String> mapColumnsToView) {

        Set<Integer> codes = elements.keySet();
        mapStyles = elementStyles2.getStyles();

        for (int code : codes) {

            int columnNumber = 0;

            Row row = sheet.createRow(rowCounter++);

            Cell cell = row.createCell((short) columnNumber);
            CellStyle elemStyle = null;

          /*  Font font = discoverFont(code);*/
            elemStyle = mapStyles.get("a" + code).getStyleElementsKey();
          /*  elemStyle.setFont(font);*/
            cell.setCellStyle(elemStyle);

            String value = setSpaceToElement(code,elements.get(code));
            cell.setCellValue(value);
            // sheet.autoSizeColumn(columnNumber);
            sheet.setColumnWidth(200, columnNumber);

            columnNumber += 2;

            Object[] dates = foodBalanceResults.keySet().toArray();

            int length = dates.length;

            for (int j = 0; j < length; j++) {
                String date = dates[j].toString();
                String resultColumn = mapColumnsToView.get(dates[j].toString());

                switch (resultColumn) {
                    case "show":
                        columnNumber = fillForecastElementsShow(columnNumber, row, foodBalanceResults.get(date), code, date);
                        break;

                    case "complete":
                        columnNumber = fillForecastElementsWithFlagsAndComments(columnNumber, row, foodBalanceResults.get(date), code, sheet, date);
                        break;

                    default:
                        columnNumber = fillForecastElementsShowOnlyAndDefault(columnNumber, row, foodBalanceResults.get(date), code, date);
                        break;
                }
            }
        }

        rowCounter++;
        return rowCounter;
    }

    /**
     * Create the headers complete, with flags and comments, for the last forecast of the last season and last season -1
     * @param date
     * @param columnNumber
     * @param row
     * @param workbook
     * @param sheet
     * @return the number of column used
     */
    private int createHeadersValuesWithFlagsAndComments(String date, int columnNumber, Row row, HSSFWorkbook workbook, Sheet sheet) {

        row.setHeight((short) (3 * 260));
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(amisExcelUtils.getBlueCellStyle());
        cell.setCellValue(reformatDate(date));
        sheet.autoSizeColumn(columnNumber);

        columnNumber++;

        Cell cell2 = row.createCell((short) columnNumber);
        cell2.setCellStyle(amisExcelUtils.getBlueCellStyle());
        cell2.setCellValue(FLAG_HEADER);
        sheet.autoSizeColumn(columnNumber);

        columnNumber++;

        Cell cell3 = row.createCell((short) columnNumber);
        cell3.setCellStyle(amisExcelUtils.getBlueCellStyle());
        cell3.setCellValue(NOTES_HEADER);
        sheet.autoSizeColumn(columnNumber);

        columnNumber += 2;

        return columnNumber;
    }


    /**
     * Create the headers with only value, for the last forecast -1 of last season and last season -1
     * @param date
     * @param columnNumber
     * @param row
     * @param sheet
     * @return the number of column used
     */
     private int createHeadersValuesSimpleShow(String date, int columnNumber, Row row, Sheet sheet) {

        row.setHeight((short) (3 * 260));
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(amisExcelUtils.getBlueCellStyle());
        cell.setCellValue(reformatDate(date));
        sheet.autoSizeColumn(columnNumber);

        columnNumber++;

        return columnNumber;
    }

    /**
     * Create the header for only value to show for the last season -2
     * @param date
     * @param columnNumber
     * @param row
     * @param sheet
     * @return the column number
     */
    private int createHeadersValuesSimpleShowOnly(String date, int columnNumber, Row row, Sheet sheet) {


        row.setHeight((short) (3 * 260));
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(amisExcelUtils.getBlueCellStyle());
        cell.setCellValue(reformatDate(date));
        sheet.autoSizeColumn(columnNumber);

        // hide the column hust before this
        sheet.setColumnHidden(columnNumber - 1, true);

        columnNumber += 2;

        return columnNumber;
    }

    // For other seasons hidden
    private int createHeadersValuesSimpleHidden(String date, int columnNumber, Row row, Sheet sheet) {

        AmisExcelUtils.setHeaderHeight(row);
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(amisExcelUtils.getBlueCellStyle());
        cell.setCellValue(reformatDate(date));
        sheet.autoSizeColumn(columnNumber);
        sheet.setColumnHidden(columnNumber, true);

        // set the "blank" column to hidden
        columnNumber++;
        sheet.setColumnHidden(columnNumber, true);

        columnNumber++;

        return columnNumber;
    }


    private int fillForecastElementsWithFlagsAndComments(int columnNumber, Row row, LinkedHashMap<String, DaoForecastValue> elements, int code, Sheet sheet, String date) {

        DaoForecastValue forecast = elements.get("" + code);

        if (forecast != null) {
            // value

            double value = forecast.getValue();
            value = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();


            Cell cell = row.createCell((short) columnNumber);
            CellStyle cellStyle = mapStyles.get("a" + code).getStyleBodyElement();

            Font font = discoverFont(code, (HSSFCellStyle)cellStyle);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);

            if (value == -1) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(value);
            }
            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);

            columnNumber++;

            // flags
            Cell cell1 = row.createCell((short) columnNumber);

            cell1.setCellStyle(getFlagAndNotesStyle(code,true));
/*
            cell1.setCellStyle(amisExcelUtils.getBasicWithRightAlWithBorders());
*/
            cell1.setCellValue((forecast.getFlags().equals("null")) ? "" : forecast.getFlags());

            String indexLetter1 = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "flags", indexLetter1);

            columnNumber++;

            // notes
            Cell cell2 = row.createCell((short) columnNumber);
            cell2.setCellStyle(getFlagAndNotesStyle(code,false));

            cell2.setCellValue((forecast.getNotes() == null || forecast.getNotes().equals("null")) ? "" : forecast.getNotes());
            sheet.autoSizeColumn(columnNumber);

            String indexLetter2 = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "notes", indexLetter2);

            columnNumber += 2;

        } else {

            Cell cell = row.createCell((short) columnNumber);
            CellStyle cellStyle = mapStyles.get("a" + code).getStyleBodyElement();

            cell.setCellStyle(cellStyle);

            cell.setCellValue("");

            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);

            columnNumber++;

            // flags

            Cell cell1 = row.createCell((short) columnNumber);

            cell1.setCellStyle(getFlagAndNotesStyle(code,true));
            cell1.setCellValue("");

            String indexLetter1 = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "flags", indexLetter1);

            columnNumber++;

            // notes
            Cell cell2 = row.createCell((short) columnNumber);
            //discoverFont(code,(HSSFCellStyle)cellStyleNotes);

            cell2.setCellStyle(getFlagAndNotesStyle(code,false));
            cell2.setCellValue("");

            String indexLetter2 = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "notes", indexLetter2);

            columnNumber += 2;

        }

        return columnNumber;
    }


    private int fillForecastElementsShow(int columnNumber, Row row, LinkedHashMap<String, DaoForecastValue> elements, int code, String date) {

        DaoForecastValue forecast = elements.get("" + code);

        if (forecast != null) {
            // value

            double value = forecast.getValue();
            value = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();


            Cell cell = row.createCell((short) columnNumber);
            CellStyle cellStyle = mapStyles.get("a" + code).getStyleBodyElement();

            Font font = discoverFont(code, (HSSFCellStyle)cellStyle);
        //    cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);

            if (value == -1) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(value);
            }
            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);

            columnNumber++;

        } else {

            Cell cell = row.createCell((short) columnNumber);
            CellStyle cellStyle = mapStyles.get("a" + code).getStyleBodyElement();

            Font font = discoverFont(code, (HSSFCellStyle) cellStyle);
      //      cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("");

            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);
            columnNumber++;
        }

        return columnNumber;
    }


    private int fillForecastElementsShowOnlyAndDefault(int columnNumber, Row row, LinkedHashMap<String,
            DaoForecastValue> elements, int code, String date) {

        DaoForecastValue forecast = elements.get("" + code);

        if (forecast != null) {
            // value

            double value = forecast.getValue();
            value = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();

            System.out.println("value: "+value);


            Cell cell = row.createCell((short) columnNumber);
            CellStyle cellStyle = mapStyles.get("a" + code).getStyleBodyElement();

            Font font = discoverFont(code, (HSSFCellStyle) cellStyle);
          //  cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);

            if (value == -1) {

                cell.setCellValue("");
            } else {
                cell.setCellValue(value);
            }
            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);

            columnNumber += 2;

        } else {

            Cell cell = row.createCell((short) columnNumber);
            CellStyle cellStyle = mapStyles.get("a" + code).getStyleBodyElement();

            Font font = discoverFont(code, (HSSFCellStyle) cellStyle);
          //  cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);

            cell.setCellValue("");

            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);

            columnNumber += 2;
        }

        return columnNumber;
    }


    private boolean checkIfExist(String addendumCode, Sheet sheet) {

        boolean result = true;

        Cell cell;
        CellReference referenceValue = new CellReference(addendumCode);
        Row rowIndex = sheet.getRow(referenceValue.getRow());
        cell = rowIndex.getCell(referenceValue.getCol());

        if (cell.getCellType() == 1 && cell.getStringCellValue() == "" || cell.getCellType() == 0 && cell.getNumericCellValue() == 0.0) {
            result = false;
        }

        return result;
    }


    private Collection<Integer> createSeasonOrderedArray(Object[] dates) {

        Collection<Integer> seasons = new ArrayList<Integer>();

        // order season as integer considering the first year
        // ( ex: 2000 for "2000/01"
        for (Object date : dates)
            seasons.add(Integer.parseInt(date.toString().substring(0, 4)));

        return ((Collection) seasons);

    }


    private int putMarketingYear(Sheet sheet, Workbook workbook, Row row, int columnNumber, String valueToPut) {

        columnNumber++;
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(amisExcelUtils.getBoldTextCellStyleWithAlignment((HSSFWorkbook) workbook, null));
        String value = reformatMarketingYear(valueToPut);

        cell.setCellValue(value);
/*
        sheet.autoSizeColumn(columnNumber);
*/

        return columnNumber;

    }

    public int createFooterMarketingYear(int rowCounter, Sheet sheet, Workbook workbook, NationalMarketingBean bean, String type, Map<String, String> datesMap) {


        Row row = sheet.createRow(rowCounter);
        Cell cell = row.createCell((short) 0);

        String footerValue = "";
        String lastSeasonStart = lastSeason.split("/")[0];
        String months, cropsMonths, startingYear, endingYear, startingYearCrops, endingYearCrops;

        switch (type) {
            case "national":
                months = bean.getNmyMonths();
                startingYear = "" + (Integer.parseInt(bean.getNmyStartingYear()) + Integer.parseInt(lastSeasonStart));
                endingYear = "" + (Integer.parseInt(bean.getNmyEndingYear()) + +Integer.parseInt(lastSeasonStart));

                String startingCropsMonths = bean.getHarvestBean().getBeginningOfHarvest();
                String endingCropsMonths = bean.getHarvestBean().getEndOfHarvest();

                startingYearCrops = "" + (Integer.parseInt(bean.getHarvestBean().getBeginningHarvestYear()) + Integer.parseInt(lastSeasonStart));
                endingYearCrops = "" + (Integer.parseInt(bean.getHarvestBean().getEndHarvestYear()) + Integer.parseInt(lastSeasonStart));


                if (!months.equals("-1") && !startingYear.equals("-1") && !endingYear.equals("-1")) {
                    footerValue += "In the " + lastSeason + " forecasts, the NMY covers the period from " + months.split("/")[0] + " " + startingYear +
                            " to " + months.split("/")[1] + " " + endingYear;
                }

                if (!startingCropsMonths.equals("-1") && !startingYearCrops.equals("-1") && !endingCropsMonths.equals("-1") && !endingYearCrops.equals("-1")) {
                    footerValue += " and refers to the crop that is harvested mainly from " + startingCropsMonths + " " + startingYearCrops +
                            " to " + endingCropsMonths + " " + endingYearCrops;
                }
                break;

            case "ity":

                months = bean.getItyMonths();

                startingYear = "" + (Integer.parseInt(bean.getItyStartingYear()) + Integer.parseInt(lastSeasonStart));
                endingYear = "" + (Integer.parseInt(bean.getItyEndingYear()) + +Integer.parseInt(lastSeasonStart));

                if (!months.equals("-1") && !startingYear.equals("-1") && !endingYear.equals("-1")) {
                    footerValue += "In the " + lastSeason + " forecasts, the ITY covers the period from " + months.split("/")[0] + " " + startingYear +
                            " to " + months.split("/")[1] + " " + endingYear;
                }
                break;
        }

        cell.setCellValue(footerValue);
        sheet.setColumnWidth(10, 0);
        rowCounter++;
        return rowCounter;
    }

    public void createNoDataAvailable(Workbook workbook, Sheet sheet) {

        Cell cell = sheet.createRow(5).createCell(3);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 30);
        font.setFontName("IMPACT");
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        cell.setCellValue("NO DATA AVAILABLE");
        cell.setCellStyle(style);
    }


    private String reformatDate(String entireDate) {
        String result = "";

        String[] splitted = entireDate.split("\\(");
        result += splitted[0] + " \n";

        String[] forecastDate = splitted[1].split("-");
        result += "(";
        result += new DateFormatSymbols(new Locale("en", "GB")).getMonths()[Integer.parseInt(forecastDate[1]) - 1] + " ";
        result += forecastDate[0] + ")";
        return result;
    }


    private String reformatMarketingYear(String entireDate) {

        String result = "";
        String[] splitted = entireDate.split("/");
        result += splitted[0] + " /";
        result += " \n";
        result += splitted[1];
        return result;
    }


    private String setSpaceToElement (int code, String value) {
        String result = "";
        if(SPACE_ELEMENT.get(code) != null) {
            if(SPACE_ELEMENT.get(code) == 1) {
                result += "  "+value;
            }else if(SPACE_ELEMENT.get(code) == 2){
                result += "    "+value;
            }
            else{
                result = value;
            }
        }else{
            result  =value;
        }
        return result;
    }


    private Font discoverFont (int code, HSSFCellStyle cellStyle) {
        Font result =null;
        if(SPACE_ELEMENT.get(code) != null) {
            if (SPACE_ELEMENT.get(code) == 1) {
                amisExcelUtils.putItalicFont(cellStyle);
                result = amisExcelUtils.getItalicFont();
            } else if (SPACE_ELEMENT.get(code) == 2) {
                amisExcelUtils.getSmallTextCellStyle(cellStyle,true);
                result = amisExcelUtils.getBoldSmallFont();
            } else if (SPACE_ELEMENT.get(code) == 3) {
                amisExcelUtils.getSmallTextCellStyle(cellStyle,true);
                result = amisExcelUtils.getBoldFont();
            }
        }else{
            result = amisExcelUtils.getBasicFont();
        }
        return result;
    }


    private HSSFCellStyle getFlagAndNotesStyle (int code,  boolean isFlag) {

        HSSFCellStyle result =null;
        if(SPACE_ELEMENT.get(code) != null) {
            if (SPACE_ELEMENT.get(code) == 1) {
                result = (isFlag)? amisExcelUtils.getFlagWithItalic(): amisExcelUtils.getNotesWithItalic();
                amisExcelUtils.putItalicFont(result);
            } else if (SPACE_ELEMENT.get(code) == 2) {
                result = (isFlag)? amisExcelUtils.getFlagWithSmallBold(): amisExcelUtils.getNotesWithSmallBold();
                amisExcelUtils.putSmallBoldFont(result);
            } else if (SPACE_ELEMENT.get(code) == 3) {
                result = (isFlag)? amisExcelUtils.getFlagWithBold(): amisExcelUtils.getNotesWithBold();
                amisExcelUtils.putBoldFont(result);
            }
        }else{
            result =  (isFlag)?amisExcelUtils.getRightAlignmentWithBordersStyle(): amisExcelUtils.getBasicWithBorders();
        }
        return result;
    }


}