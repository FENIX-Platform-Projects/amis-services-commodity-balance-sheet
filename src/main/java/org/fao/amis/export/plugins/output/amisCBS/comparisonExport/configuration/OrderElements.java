package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.configuration;

public enum OrderElements {
    soybeansCodes(new int[]{19, 18, 5, 7, 35, 20, 14, 13, 36, 15, 21, 34, 10, 16, 999, 8, 12, 1, 2,37, 4,  25}),
    riceCodes    (new int[]{19, 18, 5, 7, 35, 20, 14, 13, 15, 21, 34, 28, 10, 16, 999, 29, 30, 31, 32, 33, 8, 12, 1, 2, 37, 4, 3,  25}),
    maizeAndWheat(new int[]{19, 18, 5, 7, 35, 20, 14, 13, 15, 21, 34, 28, 10, 16, 999, 29, 30, 31, 32, 33, 8, 12, 1, 2, 37, 4,  25});

    private int[] codes;

    OrderElements(int[] codes) {
        this.codes = codes;
    }

    public int[] getCodes() {
        return codes;
    }
}
