package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.configuration;

public enum OrderElements {

    maizeAndWheat(new int[]{19, 18, 5, 7, 27, 999, 35, 20, 13, 14, 15, 21, 28, 34, 29, 30, 31, 32, 33, 8, 10, 12, 16, 1, 2, 4, 37, 25}),
    riceCodes (new int[]{19,18,998,5,7,27,999,35,20,13,14,15,21,28,34,8,10,12, 16,1,2,4,3 ,37,25 }),
    soybeansCodes(new int[]{19,18,5,7,27,999,35,20,36,31,13,14, 15,21,28,34, 10,12, 8,16,1,2,4 ,37,25});

    private int[] codes;

    OrderElements(int[] codes) { this.codes=  codes;}

    public int[] getCodes() {
        return codes;
    }

}
