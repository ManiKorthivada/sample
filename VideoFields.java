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
        resourceType = {"/apps/dgtl-content/components/structure/videoxfpagewidget"},
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
@JsonRootName(value = "Video")

public class VideoFields extends BaseModel implements ComponentExporter {
    private static Logger LOG = LoggerFactory.getLogger(VideoModel.class);
    @SlingObject
    Resource resource;

    @JsonProperty("Video")
    private VideoModel videoModel;


    @PostConstruct
    protected void invokepost() {

        super.resource = resource.getParent();
        super.invokepost();
        videoModel = new VideoModel();
        Iterator<Resource> iteratorExp = resource.getChild("root/responsivegrid/").listChildren();

        while (iteratorExp.hasNext()) {
            Resource childResource = iteratorExp.next();
            videoModel = childResource.adaptTo(VideoModel.class);
        }
    }

    @Override
    @JsonIgnore
    public String getExportedType() {

        return null;
    }
}
 