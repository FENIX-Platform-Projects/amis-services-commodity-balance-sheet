package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;

public class StylesFont {

    private HSSFCellStyle styleElementsKey;
    private HSSFCellStyle styleBodyElement;

    public StylesFont(HSSFCellStyle styleElementsKey, HSSFCellStyle styleBodyElement) {
        this.styleElementsKey = styleElementsKey;
        this.styleBodyElement = styleBodyElement;
    }

    public HSSFCellStyle getStyleElementsKey() {
        return styleElementsKey;
    }

    public void setStyleElementsKey(HSSFCellStyle styleElementsKey) {
        this.styleElementsKey = styleElementsKey;
    }

    public HSSFCellStyle getStyleBodyElement() {
        return styleBodyElement;
    }

    public void setStyleBodyElement(HSSFCellStyle styleBodyElement) {
        this.styleBodyElement = styleBodyElement;
    }
}
