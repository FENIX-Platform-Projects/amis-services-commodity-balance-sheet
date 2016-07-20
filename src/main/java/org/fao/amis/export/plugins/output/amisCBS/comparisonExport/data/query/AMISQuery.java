package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.query;

import org.apache.log4j.Logger;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.configuration.URLGetter;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.utils.dataUtils.DataUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;


public class AMISQuery {

    private static final Logger LOGGER = Logger.getLogger(AMISQuery.class);

    private static  int[] MAIZE_AND_WHEAT_FOOD,  RICE_FOOD,  SOYBEANS_FOOD, COMMODITIES ;

    private static  String URL_NATIONAL, URL_INTERNATION, URL_OTHERS;

    private HashMap< Integer, LinkedHashMap<Integer, String>> foodBalanceElements;

    private HashMap< Integer, LinkedHashMap<Integer, String>> ityElements;

    private HashMap< Integer, LinkedHashMap<Integer, String>> otherElements;

    private DataUtils dataUtils;

    private URLGetter urlGetter;

    public AMISQuery(){
        this.urlGetter = new URLGetter();
        this.MAIZE_AND_WHEAT_FOOD = this.urlGetter.getMaizeAndWheatCodes();
        this.RICE_FOOD = this.urlGetter.getRiceCodes();
        this.SOYBEANS_FOOD = this.urlGetter.getSoybeanCodes();
        this.COMMODITIES = this.urlGetter.getCommodityCodes();

        this.URL_NATIONAL = this.urlGetter.getNationalProperties();
        this.URL_INTERNATION = this.urlGetter.getInternationalProperties();
        this.URL_OTHERS = this.urlGetter.getOthersProperties();
    }

    public  void init() throws IOException {
        try {

            initMaps( "national");
            initMaps("international");
            initMaps("others");

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void initMaps(String type) throws IOException {



         if(type.equals("national")){
             dataUtils = new DataUtils();
             Properties prop = new Properties();
             this.foodBalanceElements = new HashMap<Integer, LinkedHashMap<Integer, String>>();
             InputStream inputStream = getClass().getClassLoader().getResourceAsStream(URL_NATIONAL);
             prop.load(inputStream);

             dataUtils.fillMap(this.foodBalanceElements, COMMODITIES, MAIZE_AND_WHEAT_FOOD, RICE_FOOD, SOYBEANS_FOOD, prop);
             LOGGER.warn("*****************************************");
             LOGGER.warn("finished fill map");

             LOGGER.debug( this.foodBalanceElements.toString());
         }



        else if(type.equals("international")){
             dataUtils = new DataUtils();

             Properties prop = new Properties();
             this.ityElements = new HashMap<Integer, LinkedHashMap<Integer, String>>();
             InputStream inputStream = getClass().getClassLoader().getResourceAsStream(URL_INTERNATION);
             prop.load(inputStream);

             dataUtils.fillMap(this.ityElements,COMMODITIES,MAIZE_AND_WHEAT_FOOD,RICE_FOOD,SOYBEANS_FOOD, prop);


         }

        else if(type.equals("others")){
             dataUtils = new DataUtils();
             Properties prop = new Properties();
             this.otherElements = new HashMap<Integer, LinkedHashMap<Integer, String>>();
             InputStream inputStream = getClass().getClassLoader().getResourceAsStream(URL_OTHERS);

             prop.load(inputStream);

             dataUtils.fillMap(this.otherElements,COMMODITIES,MAIZE_AND_WHEAT_FOOD,RICE_FOOD,SOYBEANS_FOOD, prop);
         }
    }

    public HashMap<Integer, LinkedHashMap<Integer, String>> getFoodBalanceElements() {
        return foodBalanceElements;
    }

    public HashMap<Integer, LinkedHashMap<Integer, String>> getItyElements() {
        return ityElements;
    }

    public HashMap<Integer, LinkedHashMap<Integer, String>> getOtherElements() {
        return otherElements;
    }

}
