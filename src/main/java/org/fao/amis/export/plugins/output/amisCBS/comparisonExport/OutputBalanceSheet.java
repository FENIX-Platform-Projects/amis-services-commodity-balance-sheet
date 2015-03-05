package org.fao.amis.export.plugins.output.amisCBS.comparisonExport;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.fao.amis.export.core.dto.CoreOutputHeader;
import org.fao.amis.export.core.dto.CoreOutputType;
import org.fao.amis.export.core.dto.data.CoreData;
import org.fao.amis.export.core.output.plugin.Output;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.Factory.DataFactory;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.excel.creation.handlerCreation.HandlerExcelCreation;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.externalServices.BeanNationalYear;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OutputBalanceSheet   extends Output {

    private static final Logger LOGGER = Logger.getLogger(OutputBalanceSheet.class);

    private Map<String, Object> config;
    private CoreData resource;
    HSSFWorkbook wb;
    private BeanNationalYear bean;

    @Override
    public void init(Map<String, Object> config) { this.config = config; }

    @Override
    public void process(CoreData resource) throws Exception {

        LinkedHashMap tmp = ((LinkedHashMap)config.get("filterData"));
        ArrayList<Object[]> ss  = Lists.newArrayList(resource.getData());
        wb = createSheet(ss,tmp );
    }



    @Override
    public CoreOutputHeader getHeader() throws Exception {

        CoreOutputHeader coreOutputHeader = new CoreOutputHeader();
        coreOutputHeader.setName(((config.get("fileName") != null)? config.get("fileName").toString(): "fenixExport.xlsx"));
        coreOutputHeader.setSize(100);
        coreOutputHeader.setType(CoreOutputType.xlsx);
        return coreOutputHeader;
    }

    @Override
    public void write(OutputStream outputStream) throws Exception {

        wb.write(outputStream);
        outputStream.close();
     //   wb.dispose();

    }

    private HSSFWorkbook createSheet( ArrayList<Object[]> data, LinkedHashMap filterData){


        DataFactory dataFactory = new DataFactory(data, filterData);
        HandlerExcelCreation excelController = new HandlerExcelCreation();

        this.bean = new BeanNationalYear();
        bean.execute();
        return excelController.init(dataFactory.getForecastIstance(), dataFactory.getAMISQueryIstance(), dataFactory.getDataCreatorIstance());
    }


}
