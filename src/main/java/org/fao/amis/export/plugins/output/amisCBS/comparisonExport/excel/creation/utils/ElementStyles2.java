package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils;

import java.util.HashMap;

public class ElementStyles2 {

    private AmisExcelUtils amisExcelUtils;
    private HashMap<String, StylesFont> styles;


    public ElementStyles2 (AmisExcelUtils amisExcelUtils) {
        this.amisExcelUtils = amisExcelUtils;
        this.styles = new HashMap<String, StylesFont>();
    }


    public void init () {
        this.styles.put("a19", new StylesFont(amisExcelUtils.getGreyBoldCellStyle(), amisExcelUtils.getNormalWithBold()));      // Total Supply
        this.styles.put("a18", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorders()));  // Opening Stock
        this.styles.put("a5", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorders()));   // Production
        this.styles.put("a998", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorders())); // Production Paddy
        this.styles.put("a7", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorders()));   // Imports (NMY)
        this.styles.put("a35", new StylesFont(amisExcelUtils.getGreyBoldCellStyle(), amisExcelUtils.getNormalWithBold()));  // Total Utilization
        this.styles.put("a20", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorders()));  // Domestic Utilization
        this.styles.put("a14", new StylesFont(amisExcelUtils.getGreyItalicCellStyle(),       amisExcelUtils.getNormalWithItalic()));  // Food Use
        this.styles.put("a36", new StylesFont(amisExcelUtils.getGreyItalicCellStyle(),       amisExcelUtils.getNormalWithItalic()));  // Crush
        this.styles.put("a13", new StylesFont(amisExcelUtils.getGreyItalicCellStyle(),       amisExcelUtils.getNormalWithItalic()));  // Feed Use
        this.styles.put("a15", new StylesFont(amisExcelUtils.getGreyItalicCellStyle(),       amisExcelUtils.getNormalWithItalic()));  // Other Uses
        this.styles.put("a21", new StylesFont(amisExcelUtils.getGreyWithSmallBold(),       amisExcelUtils.getNormalWithSmallBold()));  // Seeds
        this.styles.put("a34", new StylesFont(amisExcelUtils.getGreyWithSmallBold(),       amisExcelUtils.getNormalWithSmallBold()));  // Post Harvest Losses
        this.styles.put("a28", new StylesFont(amisExcelUtils.getGreyWithSmallBold(),       amisExcelUtils.getNormalWithSmallBold()));  // Industrial Uses Supply
        this.styles.put("a27", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorders()));  // Domestic Supply
        this.styles.put("a10", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorders()));  // Exports (NMY)
        this.styles.put("a16", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorders()));  // Closing Stock
        this.styles.put("a999", new StylesFont(amisExcelUtils.getGreyBoldCellStyle(), amisExcelUtils.getNormalWithBold())); // Unbalanced
        this.styles.put("a12", new StylesFont(amisExcelUtils.getGreyCellStyle(),      amisExcelUtils.getBasicWithBorderStyle()));      // Exports (ITY)
        this.styles.put("a8", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorderStyle()));   // Imports (ITY)
        this.styles.put("a2", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorderStyle()));   // Area HArv
        this.styles.put("a4", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorderStyle()));   // Yield
        this.styles.put("a1", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorderStyle()));   // Population
        this.styles.put("a3", new StylesFont(amisExcelUtils.getGreyCellStyle(),       amisExcelUtils.getBasicWithBorderStyle()));   // Extraction Rate
        this.styles.put("a37", new StylesFont(amisExcelUtils.getGreyCellStyle(),      amisExcelUtils.getBasicWithBorderStyle()));   // Area Planted
        this.styles.put("a25", new StylesFont(amisExcelUtils.getGreyCellStyle(),      amisExcelUtils.getBasicWithBorderStyle()));   // Per capita food use
    }

    public HashMap<String, StylesFont> getStyles() {
        return styles;
    }



}
