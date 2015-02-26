package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.formula.translator;

import java.util.LinkedHashMap;

public class CellMapper{

   private LinkedHashMap<String,String> mapCells;


    public CellMapper() {
        this.mapCells = new LinkedHashMap<String, String>();
    }

    @Override
    public String toString() {
        return "CellMapper{" +
                "mapCells=" + mapCells +
                '}';
    }


    public LinkedHashMap<String, String> getMapCells() {
        return mapCells;
    }


    public void putData(String date, String code, String type, String value){
        this.mapCells.put(""+date+"*"+code+"*"+type+"",value);
    }


}