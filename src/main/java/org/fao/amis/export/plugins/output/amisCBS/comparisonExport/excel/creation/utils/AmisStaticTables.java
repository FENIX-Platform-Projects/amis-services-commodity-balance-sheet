package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils;


import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AmisStaticTables {

    private static final String TABLE_TITLE = "List of forecasting methodologies";
    private static int startColumnRange = 0;
    private static int endColumnRange = 1;
    private CellStyle tableHeader;
    private CellStyle tableContent;

    public AmisStaticTables (Workbook workbook) {
        tableHeader = workbook.createCellStyle();
     //   AmisExcelUtils.setHeaderFlagTable(tableHeader);
        tableContent = workbook.createCellStyle();
      //  AmisExcelUtils.setFlagTableBody(tableContent);
    }


    public void buildTable (Sheet sheet, int rowCounter, Workbook workbook) {

        rowCounter += 2;
        rowCounter = createTitle(sheet, rowCounter, workbook);
        buildBody(rowCounter, sheet);
    }


    private int createTitle (Sheet sheet, int rowCounter, Workbook workbook ){

        Row row = sheet.createRow(rowCounter-1);
        row.setHeight((short) (3 * 150));
        Cell cell = row.createCell(startColumnRange);
        CellStyle cellStyle = workbook.createCellStyle();
        Font font  = workbook.createFont();
        font.setFontHeight((short) 200);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        cellStyle.setFont(font);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cell.setCellStyle(cellStyle);


        Cell cell2 = row.createCell(endColumnRange);
        cell2.setCellStyle(cellStyle);

        cell.setCellValue(TABLE_TITLE.toUpperCase());
        CellRangeAddress region = new CellRangeAddress(rowCounter-1, rowCounter-1,startColumnRange, endColumnRange);
        sheet.addMergedRegion(region);
        return rowCounter++;
    }


    private void buildBody (int rowCounter, Sheet sheet ) {

        for( FlagLabels f: FlagLabels.values()) {
            Row row = sheet.createRow(rowCounter);
            Cell cellLabel = row.createCell(0);
            cellLabel.setCellStyle(AmisExcelUtils.getGreyCellStyle());
            cellLabel.setCellValue(f.getLabel());
            Cell cellCode = row.createCell(1);
            cellCode.setCellStyle(AmisExcelUtils.getGreyCellStyle());
            cellCode.setCellValue(f.toString());
            rowCounter++;
        }
        buildCreationDate(rowCounter,sheet);
    }


    private void buildCreationDate( int rowCounter, Sheet sheet) {

        rowCounter++;
        Date d = Calendar.getInstance().getTime(); // Current time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Set your date format
        String currentData = sdf.format(d); // Get Date String according to date format
        String valueCell = "Sheet created on  "+currentData;
        CellStyle dateCreationStyle = AmisExcelUtils.getNormalWithItalic();
        dateCreationStyle.setFont(AmisExcelUtils.getItalicFont());
        Cell dateCreated =  sheet.createRow(rowCounter).createCell(0);
        dateCreated.setCellStyle(dateCreationStyle);
        dateCreated.setCellValue(valueCell);
    }

}
