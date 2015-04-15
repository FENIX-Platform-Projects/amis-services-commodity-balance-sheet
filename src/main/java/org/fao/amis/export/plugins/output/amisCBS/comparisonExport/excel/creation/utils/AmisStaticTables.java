package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class AmisStaticTables {

    private static final String TABLE_TITLE = "List of forecasting methodologies";
    private static int startColumnRange = 0;
    private static int endColumnRange = 1;




    public void buildTables (Sheet sheet, int rowCounter, Workbook workbook) {

        rowCounter += 2;
        rowCounter = createTitle(workbook,sheet,rowCounter);
        for( FlagLabels f: FlagLabels.values()) {
            Row row = sheet.createRow(rowCounter);
            Cell cellCode = row.createCell(0);
            cellCode.setCellValue(f.getLabel());
            Cell cellLAbel = row.createCell(1);
            cellLAbel.setCellValue(f.toString());

            rowCounter++;


        }

    }


    private int createTitle (Workbook workbook,Sheet sheet, int rowCounter ){

        Row row = sheet.createRow(rowCounter-1);
        Cell cell = row.createCell(0);
        cell.setCellValue(TABLE_TITLE.toUpperCase());
        CellRangeAddress region = new CellRangeAddress(rowCounter-1, rowCounter-1,startColumnRange, endColumnRange);
        sheet.addMergedRegion(region);
        return rowCounter++;

    }


}
