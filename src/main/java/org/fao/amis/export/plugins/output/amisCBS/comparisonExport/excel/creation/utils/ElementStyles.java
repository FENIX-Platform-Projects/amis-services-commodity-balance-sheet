package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils;


import org.apache.poi.ss.usermodel.CellStyle;

public enum ElementStyles {

    a19(AmisExcelUtils.getGreyBoldCellStyle(),  AmisExcelUtils.getNormalWithBold()),         // Total Supply
    a18(AmisExcelUtils.getGreyCellStyle(),      AmisExcelUtils.getBasicWithBorders()),       // Opening Stock
    a5(AmisExcelUtils.getGreyCellStyle(),       AmisExcelUtils.getBasicWithBorders()),       // Production
    a998(AmisExcelUtils.getGreyCellStyle(),     AmisExcelUtils.getBasicWithBorders()),       // Production Paddy
    a7(AmisExcelUtils.getGreyCellStyle(),       AmisExcelUtils.getBasicWithBorders()),       // Imports (NMY)
    a35(AmisExcelUtils.getGreyBoldCellStyle(),  AmisExcelUtils.getNormalWithBold()),         // Total Utilization
    a20(AmisExcelUtils.getGreyCellStyle(),      AmisExcelUtils.getBasicWithBorders()),       // Domestic Utilization
    a14(AmisExcelUtils.getGreyItalicCellStyle(),AmisExcelUtils.getNormalWithItalic()),       // Food Use
    a36(AmisExcelUtils.getGreyItalicCellStyle(),AmisExcelUtils.getNormalWithItalic()),       // Crush
    a13(AmisExcelUtils.getGreyItalicCellStyle(),AmisExcelUtils.getNormalWithItalic()),       // Feed Use
    a15(AmisExcelUtils.getGreyItalicCellStyle(),AmisExcelUtils.getNormalWithItalic()),       // Other Uses
    a21(AmisExcelUtils.getGreyWithSmallBold(),  AmisExcelUtils.getNormalWithSmallBold()),    // Seeds
    a34(AmisExcelUtils.getGreyWithSmallBold(),  AmisExcelUtils.getNormalWithSmallBold()),    // Post Harvest Losses
    a28(AmisExcelUtils.getGreyWithSmallBold(),  AmisExcelUtils.getNormalWithSmallBold()),    // Industrial Uses Supply
    a27(AmisExcelUtils.getGreyCellStyle(),      AmisExcelUtils.getBasicWithBorders()),       // Domestic Supply
    a10(AmisExcelUtils.getGreyCellStyle(),      AmisExcelUtils.getBasicWithBorders()),       // Exports (NMY)
    a16(AmisExcelUtils.getGreyCellStyle(),      AmisExcelUtils.getBasicWithBorders()),       // Closing Stock
    a999(AmisExcelUtils.getGreyBoldCellStyle(), AmisExcelUtils.getNormalWithBold()),         // Unbalanced
    a12(AmisExcelUtils.getGreyCellStyle(),      AmisExcelUtils.getBasicWithBorders()),       // Exports (ITY)
    a8(AmisExcelUtils.getGreyCellStyle(),       AmisExcelUtils.getBasicWithBorders()),       // Imports (ITY)
    a2(AmisExcelUtils.getGreyCellStyle(),       AmisExcelUtils.getBasicWithBorders()),       // Area HArv
    a4(AmisExcelUtils.getGreyCellStyle(),       AmisExcelUtils.getBasicWithBorders()),       // Yield
    a1(AmisExcelUtils.getGreyCellStyle(),       AmisExcelUtils.getBasicWithBorders()),       // Population
    a3(AmisExcelUtils.getGreyCellStyle(),       AmisExcelUtils.getBasicWithBorders()),       // Extraction Rate
    a37(AmisExcelUtils.getGreyCellStyle(),      AmisExcelUtils.getBasicWithBorders()),       // Area Planted
    a25(AmisExcelUtils.getGreyCellStyle(),      AmisExcelUtils.getBasicWithBorders());       // Per capita food use

    private CellStyle styleElements, styleBody;

    ElementStyles(CellStyle styleElements,CellStyle styleBody) {
        this.styleElements = styleElements;
        this.styleBody = styleBody;
    }

    public CellStyle getStyleElements() {
        return styleElements;
    }

    public CellStyle getStyleBody() {
        return styleBody;
    }
}
