package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.formula;


import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.daoValue.DaoForecastValue;

import java.util.LinkedHashMap;

public class FormulaUtils {


    private  String findValueInTheForecast ( LinkedHashMap<String, DaoForecastValue> forecasts){
        return null;
    }


  /*  public int getIndexElementToFind ( String elementToFind, LinkedHashMap<String, DaoForecastValue> forecast ){
        int counter = 0;
        for(String element: forecast.keySet()){
            if(forecast.get(element).equals(elementToFind)){
                return counter;
            }
            counter++;
        }
        return -1;
    }*/

    public boolean checkIfCalculated (DaoForecastValue forecastValue){
        return (forecastValue.getFlags() != null) && forecastValue.getFlags().split(",")[0] == "C";
    }



    public boolean checkIfOk (String valuesToSearch,  LinkedHashMap<String, DaoForecastValue> forecast) {

        boolean found = (forecast.get(valuesToSearch) != null) && (forecast.get(valuesToSearch).getValue() != -1);
        return (forecast.get(valuesToSearch) != null) && (forecast.get(valuesToSearch).getValue() != -1);
    }

    public boolean checkIfPaddyValues (LinkedHashMap<String, DaoForecastValue> forecast){
        return (forecast.get("998") != null) && (forecast.get("996") != null);
    }

   /* // return index of the element
    public int getIndexOrCreateValue (String elementToFind,  LinkedHashMap<String, DaoForecastValue> forecast){
        int counter = 0;
        for(String element: forecast.keySet()){
            if(forecast.get(element).equals(elementToFind)){
                return counter;
            }
            counter++;
        }
        DaoForecastValue newValue = new DaoForecastValue(null,null,-1);
        forecast.put(elementToFind, newValue);

        return forecast.size()-1;
    }*/


}
