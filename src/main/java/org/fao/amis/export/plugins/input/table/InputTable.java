package org.fao.amis.export.plugins.input.table;

import org.fao.amis.export.core.dto.data.CoreData;
import org.fao.amis.export.core.dto.data.CoreTableData;
import org.fao.amis.export.core.input.plugin.Input;
import org.fao.fenix.commons.msd.dto.data.dataset.Resource;

import java.util.Map;

public class InputTable extends Input {

    Resource resource;
    Map<String, Object> config;

    @Override
    public void init(Map<String, Object> config, Resource resource) {
        this.resource = resource; this.config = config;
    }

    @Override
    public CoreData getResource() {
        return new CoreTableData(resource.getMetadata(), resource.getData().iterator());
    }
}
