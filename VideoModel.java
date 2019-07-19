package ahm.content.service.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import java.util.*;

@Model(
        adaptables = {Resource.class},
        adapters = ComponentExporter.class,
        resourceType = "dgtl-content/components/content/video",
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
@JsonRootName(value = "video")
public class VideoModel implements ComponentExporter {
    private static Logger LOG = LoggerFactory.getLogger(VideoModel.class);


    @Inject
    @JsonProperty("VideoType")
    private String videotype;

    @Inject
    @JsonProperty("VideoUrl")
    private String videoPathEmmi;

    @Inject
    @JsonProperty("FilePath")
    private String videoPathUpload;

    @Inject
    @JsonProperty("VideoId")
    private String videoid;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("VideoWidgetId")
    private String videoWidgetId;


    @SlingObject
    private Resource resource;

    Resource imageResource;
    private ValueMap metadataValueMap;

    @SlingObject
    ResourceResolver resourceResolver;


    @PostConstruct
    protected void invokePost() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page currentPage = pageManager.getPage(resource.getParent().getParent().getParent().getPath());
        ValueMap pageProperties = currentPage.getProperties();
        name = pageProperties.get("jcr:title", String.class);
        title = pageProperties.get("jcr:title", String.class);
        videoWidgetId = currentPage.getParent().getProperties().get("id", String.class);
        description = pageProperties.get("jcr:description", String.class);
        if ("emmi".equalsIgnoreCase(videotype)) {
            imageResource = resourceResolver.getResource(videoPathEmmi);
        } else {
            imageResource = resourceResolver.getResource(videoPathUpload);
        }
        if (null != imageResource) {
            Resource metadataResource = imageResource.getChild("jcr:content/metadata");
            if (metadataResource != null) {
                metadataValueMap = metadataResource.getValueMap();
            }
        }
    }

    public List<Map<String, String>> getRenditions() {
        List<Map<String, String>> list = new ArrayList<>();
        if (null != imageResource) {
            Resource rendtionResource = imageResource.getChild("jcr:content/renditions");
            if (rendtionResource != null) {
                Iterator<Resource> iterator = rendtionResource.listChildren();
                Map map = new HashMap();
                while (iterator.hasNext()) {
                    Resource eachImageResource = iterator.next();
                    map.put(eachImageResource.getName(), eachImageResource.getPath());
                }
                list.add(map);
            }
        }
        return list;
    }

    public String getVideotype() {
        return videotype;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoPathEmmi() {
        return videoPathEmmi;
    }

    public String getVideoPathUpload() {
        return videoPathUpload;
    }

    public String getVideoid() {
        return videoid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    @JsonIgnore
    public String getExportedType() {
        return null;
    }
}
