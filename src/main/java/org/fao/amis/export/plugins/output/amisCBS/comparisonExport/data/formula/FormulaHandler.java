package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.formula;


import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.daoValue.DaoForecastValue;

import java.util.*;

public class FormulaHandler {


    FormulaUtils formulaUtils;


    public FormulaHandler(){
        formulaUtils = new FormulaUtils();
    }


    public  void createCalculatedModel(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>>> forecasts) {

        for (String commodity : forecasts.keySet()) {
            LinkedHashMap<String, LinkedHashMap<String, DaoForecastValue>> commForecast = forecasts.get(commodity);

            for (String date : commForecast.keySet()) {
                LinkedHashMap<String, DaoForecastValue> dateForecasts = commForecast.get(date);
/*
                domesticSupplyFormula(dateForecasts);
*/
                totalSupplyFormula(dateForecasts);
                yieldFormula(dateForecasts, commodity);
                domUtilizationFormula(dateForecasts, commodity);
                totalUtilizationFormula(dateForecasts);
                unbalancedFormula(dateForecasts);
                perCapFoodUseFormula(dateForecasts);
            }
        }
    }


  /*  private void domesticSupplyFormula ( LinkedHashMap<String, DaoForecastValue> dateForecasts){

       if(formulaUtils.checkIfOk("18",dateForecasts) && formulaUtils.checkIfOk("5",dateForecasts)){
           double finalValue = dateForecasts.get("18").getValue() + dateForecasts.get("5").getValue();
           DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
           dateForecasts.put("27", newValue);
       }
    }*/


    private void totalSupplyFormula(LinkedHashMap<String, DaoForecastValue> dateForecasts){

        if(
                formulaUtils.checkIfOk("18",dateForecasts) &&
                        formulaUtils.checkIfOk("5",dateForecasts) &&
                        formulaUtils.checkIfOk("7",dateForecasts) ){

            double finalValue =  dateForecasts.get("18").getValue() + dateForecasts.get("5").getValue() + dateForecasts.get("7").getValue();
            DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
            dateForecasts.put("19", newValue);
        }


/*
        if(formulaUtils.checkIfOk("27",dateForecasts) && formulaUtils.checkIfOk("7",dateForecasts)){
            double finalValue = dateForecasts.get("27").getValue() + dateForecasts.get("7").getValue();
            DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
            dateForecasts.put("19", newValue);
        }*/
    }


    private void yieldFormula (LinkedHashMap<String, DaoForecastValue> dateForecasts, String commodity){

        // commodity different from rice
        if(!commodity.equals("4")){
            // normal and not calculated
            if(formulaUtils.checkIfOk("5",dateForecasts) && formulaUtils.checkIfOk("2",dateForecasts)
                    && !formulaUtils.checkIfCalculated(dateForecasts.get("5"))
                    && !formulaUtils.checkIfCalculated(dateForecasts.get("2"))){

                double finalValue = dateForecasts.get("5").getValue() / dateForecasts.get("2").getValue();
                DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
                dateForecasts.put("4", newValue);

                // with AH and not calculated
            }
            else if(formulaUtils.checkIfOk("5",dateForecasts) && formulaUtils.checkIfOk("37",dateForecasts)
                    && !formulaUtils.checkIfCalculated(dateForecasts.get("5"))
                    && !formulaUtils.checkIfCalculated(dateForecasts.get("37"))){

                double finalValue = dateForecasts.get("5").getValue() / dateForecasts.get("37").getValue();
                DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
                dateForecasts.put("4", newValue);
            }
        }else{
            // if are not paddy values, proceed normal
            if(!formulaUtils.checkIfPaddyValues(dateForecasts)){
                if(formulaUtils.checkIfOk("5",dateForecasts) && formulaUtils.checkIfOk("2",dateForecasts)
                        && !formulaUtils.checkIfCalculated(dateForecasts.get("5"))
                        && !formulaUtils.checkIfCalculated(dateForecasts.get("2"))){

                    double finalValue = dateForecasts.get("5").getValue() / dateForecasts.get("2").getValue();
                    DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
                    dateForecasts.put("4", newValue);
                }
                else if(formulaUtils.checkIfOk("5",dateForecasts) && formulaUtils.checkIfOk("37",dateForecasts)
                        && !formulaUtils.checkIfCalculated(dateForecasts.get("5"))
                        && !formulaUtils.checkIfCalculated(dateForecasts.get("37"))){

                    double finalValue = dateForecasts.get("5").getValue() / dateForecasts.get("37").getValue();
                    DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
                    dateForecasts.put("4", newValue);
                }
            }
        }
    }


    private void domUtilizationFormula (LinkedHashMap<String, DaoForecastValue> dateForecasts, String commodity){

        // if different from soybean
        if(!commodity.equals("6")) {
            if (formulaUtils.checkIfOk("14", dateForecasts) && formulaUtils.checkIfOk("13", dateForecasts) && formulaUtils.checkIfOk("15", dateForecasts)) {
                double finalValue = dateForecasts.get("13").getValue() + dateForecasts.get("14").getValue() + dateForecasts.get("15").getValue();
                DaoForecastValue newValue = new DaoForecastValue(null, "C", finalValue);
                dateForecasts.put("20", newValue);
            }
        }
        else {
            if (formulaUtils.checkIfOk("14", dateForecasts) && formulaUtils.checkIfOk("13", dateForecasts) && formulaUtils.checkIfOk("15", dateForecasts)
                    && formulaUtils.checkIfOk("36", dateForecasts)) {
                double finalValue = dateForecasts.get("13").getValue() + dateForecasts.get("14").getValue() + dateForecasts.get("15").getValue()
                        +dateForecasts.get("36").getValue();
                DaoForecastValue newValue = new DaoForecastValue(null, "C", finalValue);
                dateForecasts.put("20", newValue);
            }
        }

    }


    private void totalUtilizationFormula (LinkedHashMap<String, DaoForecastValue> dateForecasts) {

        if(formulaUtils.checkIfOk("20",dateForecasts) && formulaUtils.checkIfOk("10",dateForecasts) && formulaUtils.checkIfOk("16",dateForecasts)){
            double finalValue = dateForecasts.get("20").getValue() + dateForecasts.get("10").getValue()  + dateForecasts.get("16").getValue();
            DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
            dateForecasts.put("35", newValue);
        }

    }


    private void unbalancedFormula (LinkedHashMap<String, DaoForecastValue> dateForecasts) {

        if(formulaUtils.checkIfOk("19",dateForecasts) && formulaUtils.checkIfOk("35",dateForecasts) ){
            double finalValue = dateForecasts.get("19").getValue() - dateForecasts.get("35").getValue();
            DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
            dateForecasts.put("999", newValue);
        }

    }


    private void perCapFoodUseFormula (LinkedHashMap<String, DaoForecastValue> dateForecasts) {

        if(formulaUtils.checkIfOk("14",dateForecasts) && formulaUtils.checkIfOk("1",dateForecasts) ){
            double finalValue = dateForecasts.get("14").getValue() * 1000 / dateForecasts.get("1").getValue();
            DaoForecastValue newValue = new DaoForecastValue(null,"C",finalValue);
            dateForecasts.put("25", newValue);
        }

    }

}
