package ahm.content.service.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

//import com.aetna.ahm.core.beans.RevealCardBean;
import ahm.content.service.core.constants.AHMJsonServiceConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
//import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.File;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.Map.Entry;


@Model(
        adaptables = {Resource.class},
        adapters = ComponentExporter.class,
        resourceType = {"/apps/dgtl-content/components/structure/htmlxfpagewidget"},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION,
        options = {
                @ExporterOption(name = "SerializationFeature.WRAP_ROOT_VALUE", value = "true")
        }
)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonRootName(value = "Html")


public class HtmlModel extends BaseModel implements ComponentExporter {
    private static Logger LOG = LoggerFactory.getLogger(HtmlModel.class);

    @Override
    public String getWidgetType() {
        return "HTML";
    }

    @Inject
    @Via(value = "root/responsivegrid/html", type = ChildResource.class)
    @JsonProperty("Html Content")
    private String HtmlContent;

    @PostConstruct
    protected void invokepost() {
        super.Initialize();
    }

    @Override
    @JsonIgnore
    public String getExportedType() {

        return null;
    }
}
