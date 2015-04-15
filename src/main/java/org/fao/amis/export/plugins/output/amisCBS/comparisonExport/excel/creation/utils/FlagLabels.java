package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.utils;


public enum FlagLabels {

    C("Automatically calculated"),
    R("Repeated"),
    G("Official Governmental Statistics"),
    T("Trend"),
    S("Survey"),
    I("Satellite imagery"),
    M("Models"),
    F("Formula using fixing coefficients"),
    O("Other methodologies"),
    B("Balancing elements");

    private String label;

    FlagLabels(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
