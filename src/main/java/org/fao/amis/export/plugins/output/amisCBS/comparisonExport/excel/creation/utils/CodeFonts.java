package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils;


import org.apache.poi.hssf.usermodel.HSSFFont;


public enum CodeFonts {

    a19(AmisExcelUtils.getBoldFont()),  // Total Supply
    a18(AmisExcelUtils.getBasicFont()),  // Opening Stock
    a5(AmisExcelUtils.getBasicFont()),   // Production
    a998(AmisExcelUtils.getBasicFont()), // Production Paddy
    a7(AmisExcelUtils.getBasicFont()),   // Imports (NMY)
    a35(AmisExcelUtils.getBoldFont()),  // Total Utilization
    a20(AmisExcelUtils.getBoldFont()),  // Domestic Utilization
    a14(AmisExcelUtils.getItalicFont()),  // Food Use
    a36(AmisExcelUtils.getItalicFont()),  // Crush
    a13(AmisExcelUtils.getItalicFont()),  // Feed Use
    a15(AmisExcelUtils.getItalicFont()),  // Other Uses
    a21(AmisExcelUtils.getBoldSmallFont()),  // Seeds
    a34(AmisExcelUtils.getBoldSmallFont()),  // Post Harvest Losses
    a28(AmisExcelUtils.getBoldSmallFont()),  // Industrial Uses Supply
    a27(AmisExcelUtils.getBasicFont()),  // Domestic Supply
    a10(AmisExcelUtils.getBasicFont()),  // Exports (NMY)
    a16(AmisExcelUtils.getBasicFont()),  // Closing Stock
    a999(AmisExcelUtils.getBoldFont()); // Unbalanced



    private HSSFFont font;

    CodeFonts(HSSFFont font) {
        this.font = font;
    }

    public HSSFFont getFont() {
        return font;
    }
}
