package org.fao.amis.export.core.output.factory;


import org.fao.amis.export.core.dto.PluginConfig;
import org.fao.amis.export.core.output.plugin.Output;
import org.fao.amis.export.core.utils.configuration.ConfiguratorURL;
import org.fao.amis.export.core.utils.reader.PropertiesReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OutputFactory {

    private static OutputFactory outputFactory;


    public static OutputFactory getInstance() throws Exception {

        if (outputFactory == null)
        {
            outputFactory = new OutputFactory();
        }

        return outputFactory;
    }


    private Map<String, Class<Output>> pluginsClass = new HashMap<>();

    private OutputFactory() throws Exception {
        }


    //logic
    public Output getPlugin(PluginConfig config) throws Exception {
        String inputPluginsURL = ConfiguratorURL.getInstance().getOutputProperties();
        Properties pluginsClassName = PropertiesReader.getInstance().getProperties(inputPluginsURL);

        for (Map.Entry<Object, Object> entry : pluginsClassName.entrySet())
            pluginsClass.put((String)entry.getKey(), (Class<Output>)Class.forName((String)entry.getValue()));

        Output plugin = pluginsClass.get(config.getPlugin()).newInstance();
        plugin.init(config.getConfig());
        return plugin;
    }

}
