package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.creator;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.configuration.URLGetter;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator.DataCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.daoValue.DaoForecastValue;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils.AmisExcelUtils;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.formula.configurator.ConfigurationReader;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.formula.translator.CellMapper;

import java.util.*;

public class SheetCreator {

    private static final Logger LOGGER = Logger.getLogger(SheetCreator.class);

    private String[] nationalCodes;

    private static String FORMULA_URL;

    private ConfigurationReader configurationReader;


    private static final int SPACE_NAT_INT = 4;
    private static final int UM_COLUMN_NUMBER = 1;
    private static final int SPACE_INT_OTH = 4;
    private static final int ROW_START_ELEMENTS = 9;


    private String commodityChosen;

    private CellMapper cellMappers;

    private URLGetter urlGetter;

    private ArrayList<Integer> firstYearSeason;
    private HashMap<String,String> othersMeasureUnitsMap;

    public SheetCreator() {

        urlGetter = new URLGetter();
        this.othersMeasureUnitsMap = new HashMap<String,String>();
        this.othersMeasureUnitsMap.put("Population","1000s");
        this.othersMeasureUnitsMap.put("Area Harvested","Thousand Ha");
        this.othersMeasureUnitsMap.put("Area Planted","Thousand Ha");
        this.othersMeasureUnitsMap.put("Yield","Tonnes/Ha");
        this.othersMeasureUnitsMap.put("Extraction Rate","%");
    }

    public int createSummary(int rowCounter, Sheet sheet, HSSFWorkbook workbook, DataCreator dataCreator, String commodityLabel) {


        //Create Date Last Updated
        String commodity = commodityLabel;
        this.commodityChosen = commodityLabel;
        String season = dataCreator.getSeason();
        String dataSource = dataCreator.getDatasource();
        String country = dataCreator.getCountry();

        rowCounter = createHeadingRow(rowCounter, sheet, workbook, "COMMODITY: ", commodity);
        rowCounter = createHeadingRow(rowCounter, sheet, workbook, "LAST SEASON: ", season);
        rowCounter = createHeadingRow(rowCounter, sheet, workbook, "COUNTRY: ", country);
        rowCounter = createHeadingRow(rowCounter, sheet, workbook, "DATASOURCE: ", dataSource);

        rowCounter = AmisExcelUtils.createEmptyRow(rowCounter, sheet, workbook);

        return rowCounter;
    }

    private int createHeadingRow(int rowCounter, Sheet sheet, HSSFWorkbook workbook, String header, String headerValue) {
        Row row = sheet.createRow(rowCounter++);

        if (header != null && headerValue == null) {
            Cell cell = row.createCell((short) 0);
            cell.setCellStyle(AmisExcelUtils.getBigBoldTextCellStyle(workbook, null));
            cell.setCellValue(header);

            row.createCell((short) 1).setCellValue("");
        } else {

            Cell cell = row.createCell((short) 0);
            cell.setCellStyle(AmisExcelUtils.getRightAlignmentStyle());
            cell.setCellValue(header);

            cell = row.createCell((short) 1);
            cell.setCellStyle(AmisExcelUtils.getBoldTextCellStyle(workbook, null));
            cell.setCellValue(headerValue);
        }

        return rowCounter;
    }

    public int createSheetTitle(int rowCounter, Sheet sheet, HSSFWorkbook workbook) {
        Row row = sheet.createRow(rowCounter++);
        this.cellMappers = new CellMapper();
        String title = "amis commodity balance sheet";
        Cell cell = row.createCell((short) 0);
        cell.setCellStyle(AmisExcelUtils.getBoldTextCellStyle(workbook, null));
        cell.setCellValue(title.toUpperCase());

        rowCounter = AmisExcelUtils.createEmptyRow(rowCounter, sheet, workbook);

        return rowCounter;
    }


    public int createHeadersGroup(int rowCounter, Sheet sheet, HSSFWorkbook workbook,
                                  LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> mapGroup, String type,
                                  Map<String, String> mapColumnsToView) {

        String title;

        if (type == "foodBalance") {
            title = "National Marketing Year (NMY):";
            this.nationalCodes = urlGetter.getOperandsFormulaNational();
            //       this.FORMULA_URL = urlGetter.getFormulaNational();
            //       this.configurationReader = new ConfigurationReader(FORMULA_URL);
        } else if (type == "international") {
            LOGGER.info("----------------------------------------");
            LOGGER.info("INTERMATIONAL START;;;;;;;;;;;;;");
            //          this.FORMULA_URL = null;

            title = "International Trade Year (ITY):";
        } else {

            title = "Other";
            this.nationalCodes = urlGetter.getOperandsFormulaOthers();
//            this.FORMULA_URL = urlGetter.getFormulaOthers();
//            this.configurationReader = new ConfigurationReader(FORMULA_URL);

        }

        int columnNumber = 0;

        Row row = sheet.createRow(rowCounter++);
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(AmisExcelUtils.getBoldTextCellStyle(workbook, null));
        cell.setCellValue(title);
        sheet.autoSizeColumn(columnNumber);
        //TODO put here the marketing year
        columnNumber+=2;

        Object[] dates = mapGroup.keySet().toArray();

        firstYearSeason = (ArrayList) createSeasonOrderedArray(dates);

        for (int i = 0; i < dates.length; i++) {
            String date = dates[i].toString();
            String resultColumn = mapColumnsToView.get(dates[i].toString());

            switch (resultColumn) {

                case "show":
                    columnNumber = createHeadersValuesSimpleShow(date, columnNumber, row, (HSSFWorkbook) workbook, sheet);
                    break;

                case "showOnly":
                    columnNumber = createHeadersValuesSimpleShowOnly(date, columnNumber, row, (HSSFWorkbook) workbook, sheet);
                    break;

                case "complete":
                    columnNumber = createHeadersValuesWithFlagsAndComments(date, columnNumber, row, (HSSFWorkbook) workbook, sheet);
                    break;

                default:
                    columnNumber = createHeadersValuesSimpleHidden(date, columnNumber, row, (HSSFWorkbook) workbook, sheet);
                    break;
            }

        }

        rowCounter++;

        return rowCounter;
    }


    public int createDataTableGroup(int rowCounter, Sheet sheet, HSSFWorkbook workbook,
                                    HashMap<Integer, String> elements,
                                    LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> foodBalanceResults,
                                    Map<String, String> mapColumnsToView) {

        Set<Integer> codes = elements.keySet();


        int rowUM = rowCounter+1;
        int columnUM = 1;
        Cell cellUM = sheet.createRow(rowUM).createCell((short) columnUM);
        cellUM.setCellStyle(AmisExcelUtils.getGreyCellStyle());
        putMeasurementUnitValues(elements, "national", columnUM, rowCounter,sheet,cellUM);


        for (int code : codes) {

            int columnNumber = 0;

            Row row = sheet.createRow(rowCounter++);

            Cell cell = row.createCell((short) columnNumber);
            cell.setCellStyle(AmisExcelUtils.getGreyCellStyle());
            cell.setCellValue(elements.get(code));
            sheet.autoSizeColumn(columnNumber);


            columnNumber+=2;
            //


            Object[] dates = foodBalanceResults.keySet().toArray();

            int length = dates.length;

            for (int j = 0; j < length; j++) {
                String date = dates[j].toString();
                String resultColumn = mapColumnsToView.get(dates[j].toString());

                switch (resultColumn) {
                    case "show":
                        columnNumber = fillForecastElementsShow(columnNumber, row, workbook, foodBalanceResults.get(date), code, sheet, date);
                        break;

                    case "complete":
                        columnNumber = fillForecastElementsWithFlagsAndComments(columnNumber, row, workbook, foodBalanceResults.get(date), code, sheet, date);
                        break;

                    default:
                        columnNumber = fillForecastElementsShowOnlyAndDefault(columnNumber, row, workbook, foodBalanceResults.get(date), code, sheet, date);
                        break;
                }
            }
        }

        return rowCounter;
    }

    private int createHeadersValuesWithFlagsAndComments(String date, int columnNumber, Row row, HSSFWorkbook workbook, Sheet sheet) {
        String flags = "Forecasting Methodology";
        String notes = "Notes";

        row.setHeight((short) (3 * 260));
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(AmisExcelUtils.getBlueCellStyle());
        cell.setCellValue(date);
        sheet.autoSizeColumn(columnNumber);

        columnNumber++;

        Cell cell2 = row.createCell((short) columnNumber);
        cell2.setCellStyle(AmisExcelUtils.getBlueCellStyle());
        cell2.setCellValue(flags);
        sheet.autoSizeColumn(columnNumber);

        columnNumber++;

        Cell cell3 = row.createCell((short) columnNumber);
        cell3.setCellStyle(AmisExcelUtils.getBlueCellStyle());
        cell3.setCellValue(notes);
        sheet.autoSizeColumn(columnNumber);

        columnNumber += 2;

        return columnNumber;
    }

    // For the last update-1 forecast of the season
    private int createHeadersValuesSimpleShow(String date, int columnNumber, Row row, HSSFWorkbook workbook, Sheet sheet) {


        row.setHeight((short) (3 * 260));
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(AmisExcelUtils.getBlueCellStyle());
        cell.setCellValue(date);
        sheet.autoSizeColumn(columnNumber);

        columnNumber++;

        return columnNumber;
    }

    // For two season before last one
    private int createHeadersValuesSimpleShowOnly(String date, int columnNumber, Row row, HSSFWorkbook workbook, Sheet sheet) {


        row.setHeight((short) (3 * 260));
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(AmisExcelUtils.getBlueCellStyle());
        cell.setCellValue(date);
        sheet.autoSizeColumn(columnNumber);

        // hide the column hust before this
        sheet.setColumnHidden(columnNumber-1, true);

        columnNumber += 2;

        return columnNumber;
    }

    // For other seasons hidden
    private int createHeadersValuesSimpleHidden(String date, int columnNumber, Row row, HSSFWorkbook workbook, Sheet sheet) {


        row.setHeight((short) (3 * 260));
        Cell cell = row.createCell((short) columnNumber);
        cell.setCellStyle(AmisExcelUtils.getBlueCellStyle());
        cell.setCellValue(date);
        sheet.autoSizeColumn(columnNumber);
        sheet.setColumnHidden(columnNumber, true);

        // set the "blank" column to hidden
        columnNumber++;
        sheet.setColumnHidden(columnNumber, true);

        columnNumber ++;

        return columnNumber;
    }


    private int fillForecastElementsWithFlagsAndComments(int columnNumber, Row row, HSSFWorkbook workbook, LinkedHashMap<String,
            DaoForecastValue> elements, int code, Sheet sheet, String date) {
        DaoForecastValue forecast = elements.get("" + code);


        if (forecast != null) {
            // value

            int value = (int) forecast.getValue();
            Cell cell = row.createCell((short) columnNumber);
            cell.setCellStyle(AmisExcelUtils.getBasicCellStyle());
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
            cell1.setCellStyle(AmisExcelUtils.getBasicCellStyle());
            cell1.setCellValue((forecast.getFlags().equals("null")) ? "" : forecast.getFlags());

            String indexLetter1 = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "flags", indexLetter1);

            columnNumber++;

            // notes
            Cell cell2 = row.createCell((short) columnNumber);
            cell2.setCellStyle(AmisExcelUtils.getBasicCellStyle());

            cell2.setCellValue((forecast.getNotes() == null || forecast.getNotes().equals("null")) ? "" : forecast.getNotes());
            sheet.autoSizeColumn(columnNumber);

            String indexLetter2 = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "notes", indexLetter2);

            columnNumber += 2;

        } else {

            Cell cell = row.createCell((short) columnNumber);
            cell.setCellStyle(AmisExcelUtils.getBasicCellStyle());
            cell.setCellValue("");

            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);

            columnNumber++;

            // flags

            Cell cell1 = row.createCell((short) columnNumber);
            cell1.setCellStyle(AmisExcelUtils.getBasicCellStyle());
            cell1.setCellValue("");

            String indexLetter1 = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "flags", indexLetter1);

            columnNumber++;


            // notes
            Cell cell2 = row.createCell((short) columnNumber);
            cell2.setCellStyle(AmisExcelUtils.getBasicCellStyle());
            cell2.setCellValue("");

            String indexLetter2 = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "notes", indexLetter2);

            columnNumber += 2;

        }

        return columnNumber;
    }


    private int fillForecastElementsShow(int columnNumber, Row row, HSSFWorkbook workbook, LinkedHashMap<String,
            DaoForecastValue> elements, int code, Sheet sheet, String date) {

        DaoForecastValue forecast = elements.get("" + code);

        if (forecast != null) {
            // value

            int value = (int) forecast.getValue();
            Cell cell = row.createCell((short) columnNumber);
            cell.setCellStyle(AmisExcelUtils.getBasicCellStyle());
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
            cell.setCellStyle(AmisExcelUtils.getBasicCellStyle());
            cell.setCellValue("");

            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);

            columnNumber++;

        }

        return columnNumber;
    }


    private int fillForecastElementsShowOnlyAndDefault(int columnNumber, Row row, HSSFWorkbook workbook, LinkedHashMap<String,
            DaoForecastValue> elements, int code, Sheet sheet, String date) {

        DaoForecastValue forecast = elements.get("" + code);

        if (forecast != null) {
            // value

            int value = (int) forecast.getValue();
            Cell cell = row.createCell((short) columnNumber);
            cell.setCellStyle(AmisExcelUtils.getBasicCellStyle());
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
            cell.setCellStyle(AmisExcelUtils.getBasicCellStyle());
            cell.setCellValue("");

            String indexLetter = CellReference.convertNumToColString(columnNumber) + "" + (cell.getRowIndex() + 1);
            cellMappers.putData(date, "" + code, "value", indexLetter);

            columnNumber += 2;
        }

        return columnNumber;
    }


    /*private void handleFormulas(LinkedList<FormulaBean> formulaBeans, Sheet sheet, HSSFWorkbook wb, String date) {

        LinkedHashMap<String, String> mapper = cellMappers.getMapCells();


        for (FormulaBean formulaBean : formulaBeans) {
            String codeOperand = formulaBean.getOperand();
            LOGGER.info("codeOperand");
            LOGGER.info(codeOperand);

            String operandCodeValue = mapper.get(date + "*" + codeOperand + "*" + "value");
            LOGGER.info(operandCodeValue);

            String operandCodeFlags = mapper.get(date + "*" + codeOperand + "*" + "flags");
            LOGGER.info(operandCodeFlags);


            LinkedList<String> addendumsCodes = new LinkedList<String>();
            String operator = formulaBean.getOperator();
            LOGGER.info(operator);


            for (String addendum : formulaBean.getAddendums()) {
                addendumsCodes.add(mapper.get(date + "*" + addendum + "*" + "value"));
            }
            LOGGER.info(addendumsCodes.toString());
            makeFormula(operandCodeFlags, operandCodeValue, operator, addendumsCodes, sheet);

        }
    }

    private void makeFormula(String operandCodeFlags, String operandCodeValue,
                             String operator, LinkedList<String> addendumsCodes,
                             Sheet sheet) {

        LOGGER.info("StartedMAKE FORMULA");

        Cell operandCellValue = null;
        Cell operandCellFlag = null;


        CellReference referenceValue = new CellReference(operandCodeValue);
        Row rowIndex = sheet.getRow(referenceValue.getRow());
        if (rowIndex != null) {
            operandCellValue = rowIndex.getCell(referenceValue.getCol());
        }

        String formula = "";

        int lengthAddendums = addendumsCodes.size();

        boolean operandsExist = true;

        int i = 0;
        for (String addendumCode : addendumsCodes) {

            operandsExist = checkIfExist(addendumCode, sheet);

            CellReference tempReference = new CellReference(operandCodeValue);
            Row tempRow = sheet.getRow(tempReference.getRow());
            Cell tempCell = tempRow.getCell(tempReference.getCol());


            formula += addendumCode;
            if (i != lengthAddendums - 1) {
                formula += operator;
            }
            i++;
        }

        if (operandsExist) {
            operandCellValue.setCellFormula(formula);

            // set the flag to C
*//*
            CellReference referenceFlags = new CellReference(operandCodeFlags);
            Row rowIndexFlag = sheet.getRow(referenceFlags.getRow());
            if (rowIndexFlag != null) {
                operandCellFlag = rowIndexFlag.getCell(referenceFlags.getCol());
            }

            operandCellFlag.setCellValue("C");
        }
        *//*

        }
    }*/


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


    private boolean putMeasurementUnitValues (HashMap<Integer, String> elements, String type, int columnNumber, int rowNumber, Sheet sheet, Cell cell) {


        int startRowUnitsNat = 9;



        switch (type) {

            case "national":
                cell.setCellValue("Thousand tonnes");
                sheet.addMergedRegion(new CellRangeAddress(ROW_START_ELEMENTS, ROW_START_ELEMENTS+elements.size(),UM_COLUMN_NUMBER,UM_COLUMN_NUMBER));

                break;
            case "international":

                break;

            default:

                break;
        }


        return true;
    }

}
