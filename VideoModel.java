package com.aetna.ahm.core.models.qr;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import java.util.*;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = ComponentExporter.class,
        resourceType = "/apps/ahm/components/content/video",
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
@JsonRootName(value = "recipe")
public class VideoModel implements ComponentExporter {
    private static Logger LOG = LoggerFactory.getLogger(VideoModel.class);


    @ScriptVariable
    protected ValueMap pageProperties;

    @SlingObject
    ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    @ValueMapValue
    @JsonProperty("VideoUrl")
    private String videoPathEmmi;

    @ValueMapValue
    @JsonProperty("FilePath")
    private String videoPathUpload;

    @ValueMapValue
    @JsonProperty("VideoType")
    private String videotype;

    @ValueMapValue
    @JsonProperty("VideoId")
    private String videoid;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    private String imagePath;
    Resource imageResource;
    private ValueMap metadataValueMap;

    @PostConstruct
    protected void invokePost() {
        name = pageProperties.get("jcr:title", String.class);
        title = pageProperties.get("jcr:title", String.class);
        description = pageProperties.get("jcr:description", String.class);
        if ("emmi".equalsIgnoreCase(videotype)) {
            imageResource = resourceResolver.getResource(videoPathEmmi);
        } else {
            imageResource = resourceResolver.getResource(videoPathUpload);
        }
    }

    public List<Map<String, String>> getRenditions() {
        List<Map<String, String>> list = new ArrayList<>();
        if (null != imageResource && imageResource.hasChildren()) {
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

    @Override
    @JsonIgnore
    public String getExportedType() {
        return resource.getResourceType();
    }

    public String getVideoPathEmmi() {
        return videoPathEmmi;
    }

    public String getVideoPathUpload() {
        return videoPathUpload;
    }

    public String getVideotype() {
        return videotype;
    }

    public String getVideoid() {
        return videoid;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
