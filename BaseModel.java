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
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
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
@JsonRootName(value = "BaseModel")


public class BaseModel implements ComponentExporter {
    private static Logger LOG = LoggerFactory.getLogger(BaseModel.class);
    @SlingObject
    Resource resource;

    @SlingObject
    ResourceResolver resourceResolver;

    @JsonProperty("Id")
    private String id;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }


    @JsonProperty("Title")
    private String title;


    @JsonProperty("Description")
    private String description;

    @JsonProperty("Tags")
    private String[] tag;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("XfName")
    private String xfname;

    public String getXfname() {
        return xfname;
    }


    @PostConstruct
    protected void invokepost() {


        name = resource.getName();
        xfname = resource.getParent().getName();
        if ("master".equalsIgnoreCase(name)) {
            name = StringUtils.EMPTY;
        } else {
            name = xfname;
            xfname = StringUtils.EMPTY;
        }
        Resource xfResource = resource.getParent().getChild("jcr:content");

        ModifiableValueMap mapxf = xfResource.adaptTo(ModifiableValueMap.class);

        for (Entry<String, Object> entry : mapxf.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();


            if (key.equals(AHMJsonServiceConstants.JCR_TITLE)) {
                title = (String) value;
            } else if (key.equals(AHMJsonServiceConstants.JCR_DESC)) {
                description = (String) value;
            } else if (key.equals(AHMJsonServiceConstants.CQ_TAGS)) {
                tag = (String[]) value;
            }
            if (key.equals(AHMJsonServiceConstants.SLING_ALIAS)) {
                id = (String) value;
            }

        }
    }


    @Override
    @JsonIgnore
    public String getExportedType() {
        // TODO Auto-generated method stub
        return null;
    }
}
 