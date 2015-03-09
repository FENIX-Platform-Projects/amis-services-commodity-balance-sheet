package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;


public class AmisExcelUtils {

    public static HSSFFont boldFont;
    public static HSSFFont italicFont;
    public static HSSFFont italicisedSmallFont;
    public static HSSFFont whiteFont;
    public static HSSFFont bigBoldFont;
    public static HSSFFont smallFont;
    public static HSSFFont boldSmallFont;
    private static HSSFPalette palette;

    private static Map<String, HSSFCellStyle> styles;

    public static void initStyles(HSSFWorkbook workbook) {
        setCustomizedPalette(workbook);
        initializeHSSFFontStyles(workbook);

        styles = new HashMap<String, HSSFCellStyle>();

        styles.put("rightAllignment", createRightAlignmentStyle(workbook));
        styles.put("leftAllignment", createLeftAlignmentStyle(workbook));
        styles.put("basicStyle", createBasicCellStyle(workbook));
        styles.put("rightAllignmentWithBorders", createRightAlignmentWithBordersStyle(workbook));
        styles.put("blueStyle", createBlueCellStyle(workbook));
        styles.put("greyStyle", createGreyCellStyle(workbook));
        styles.put("centerAlignment", createCenterAlignmentStyle(workbook));


    }

    public static int createEmptyRow(int rowCounter, Sheet sheet, Workbook workbook) {
        Row row = sheet.createRow(rowCounter++);
        row.createCell((short) 0).setCellValue("");
        row.createCell((short) 1).setCellValue("");

        return rowCounter;
    }

    public static void setCustomizedPalette(HSSFWorkbook workbook) {


        palette = workbook.getCustomPalette();
        //customized Blue
        palette.setColorAtIndex(HSSFColor.BLUE.index,
                (byte) 116,  //RGB red (0-255)
                (byte) 166,    //RGB green
                (byte) 189     //RGB blue
        );

        //customized Grey
        palette.setColorAtIndex(HSSFColor.GREY_50_PERCENT.index,
                (byte) 238,  //RGB red (0-255)
                (byte) 232,    //RGB green
                (byte) 205     //RGB blue
        );


    }

    public static HSSFCellStyle getRightAlignmentStyle() {
        return styles.get("rightAllignment");
    }

    private static HSSFCellStyle createRightAlignmentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return style;
    }

    public static HSSFCellStyle getLeftAlignmentStyle() {

        return styles.get("leftAllignment");
    }

    private static HSSFCellStyle createLeftAlignmentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        return style;
    }

    public HSSFCellStyle getRightAlignmentWithBordersStyle() {
        return styles.get("rightAllignmentWithBorders");
    }


    private static HSSFCellStyle createRightAlignmentWithBordersStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = getBordersStyle(workbook, null);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return style;
    }

    public static HSSFCellStyle getBasicCellStyle() {
        return styles.get("basicStyle");
    }

    private static HSSFCellStyle createBasicCellStyle(HSSFWorkbook workbook) {
        return workbook.createCellStyle();
    }

    public static HSSFCellStyle getBlueCellStyle() {
        return styles.get("blueStyle");
    }

    private static HSSFCellStyle createBlueCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        getBordersStyle(workbook, cellStyle);

        cellStyle.setFont(whiteFont);

        cellStyle.setWrapText(true);

        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return cellStyle;

    }

    public static HSSFCellStyle getGreyCellStyle() {
        return styles.get("greyStyle");

    }

    private static HSSFCellStyle createGreyCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        getBordersStyle(workbook, cellStyle);

        return cellStyle;
    }

    public static HSSFCellStyle getBigBoldTextCellStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {

        if (cellStyle == null) {
            cellStyle = getBasicCellStyle();
        }

        cellStyle.setFont(bigBoldFont);

        return cellStyle;

    }

    public HSSFCellStyle getSmallTextCellStyle(HSSFCellStyle cellStyle, Boolean setBold) {

        if (cellStyle == null) {
            cellStyle = getBasicCellStyle();
        }

        if (setBold)
            cellStyle.setFont(boldSmallFont);
        else
            cellStyle.setFont(smallFont);


        return cellStyle;

    }

    public static HSSFCellStyle getBoldTextCellStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {

        if (cellStyle == null) {
            cellStyle = getBasicCellStyle();
        }

        cellStyle.setFont(boldFont);

        return cellStyle;

    }

    public static HSSFCellStyle getBordersStyle(HSSFWorkbook workbook, HSSFCellStyle cellStyle) {
        if (cellStyle == null) {
            cellStyle = getBasicCellStyle();
        }
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLUE_GREY.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLUE_GREY.getIndex());
        cellStyle.setTopBorderColor(IndexedColors.BLUE_GREY.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLUE_GREY.getIndex());

        return cellStyle;
    }

    public static void initializeHSSFFontStyles(HSSFWorkbook workbook) {
        boldFont = workbook.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        bigBoldFont = workbook.createFont();
        bigBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bigBoldFont.setFontHeightInPoints((short) 11);

        whiteFont = workbook.createFont();
        whiteFont.setColor(HSSFColor.WHITE.index);

        italicFont = workbook.createFont();
        italicFont.setItalic(true);

        italicisedSmallFont = workbook.createFont();
        italicisedSmallFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        italicisedSmallFont.setFontHeightInPoints((short) 8);

        smallFont = workbook.createFont();
        smallFont.setFontHeightInPoints((short) 8);

        boldSmallFont = workbook.createFont();
        boldSmallFont.setFontHeightInPoints((short) 8);
        boldSmallFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

    }

    public int createNoAvailableDataTable(int rowCounter, HSSFSheet sheet, HSSFWorkbook workbook, String title) {
        rowCounter = createEmptyRow(rowCounter, sheet, workbook);

        //Title Row
        rowCounter = createHeadingRow(rowCounter, sheet, workbook, title, null);

        HSSFRow row = sheet.createRow(rowCounter++);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellStyle(getLeftAlignmentStyle());
        cell.setCellValue("No Data Available");

        return rowCounter;
    }

    public static int createHeadingRow(int rowCounter, HSSFSheet sheet, HSSFWorkbook workbook, String header, String headerValue) {
        HSSFRow row = sheet.createRow(rowCounter++);
        // LOGGER.info("----------- createHeadingRow .... START ");

        if (header != null && headerValue == null) {
            HSSFCell cell = row.createCell((short) 0);
            cell.setCellStyle(getBigBoldTextCellStyle(workbook, null));
            cell.setCellValue(header);

            row.createCell((short) 1).setCellValue("");
        } else {
            // LOGGER.info("----------- header  "+header);

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellStyle(getRightAlignmentStyle());
            cell.setCellValue(header);

            cell = row.createCell((short) 1);
            cell.setCellStyle(getBoldTextCellStyle(workbook, null));
            cell.setCellValue(headerValue);
        }

        // LOGGER.info("----------- createHeadingRow .... END rowCounter =  "+rowCounter);

        return rowCounter;
    }

    private static HSSFCellStyle createCenterAlignmentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
        return style;
    }

    public static HSSFCellStyle getCenterAlignmentStyle() {
        return styles.get("centerAlignment");
    }

}
