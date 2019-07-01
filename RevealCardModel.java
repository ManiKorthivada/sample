package com.kohls.platform.models;

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

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = ComponentExporter.class,
        resourceType = "/apps/ahm/components/content/revealcard",
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
public class RevealCardModel implements ComponentExporter {
    private static Logger LOG = LoggerFactory.getLogger(RevealCardModel.class);


    @ScriptVariable
    protected ValueMap pageProperties;

    @SlingObject
    ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    @ValueMapValue
    @JsonProperty("CardTitle")
    private String cardTitle;

    @ValueMapValue
    @JsonProperty("Message")
    private String cardMessage;

    @ValueMapValue
    @JsonProperty("SortOrder")
    private String sortOrder;

    @ValueMapValue
    @JsonProperty("ImagePath")
    private String cardImage;


    @JsonProperty("Name")
    private String name;

    @JsonProperty("ImageName")
    private String imageName;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    private Resource imageResource;

    @PostConstruct
    protected void invokePost() {
        name = pageProperties.get("jcr:title", String.class);
        title = pageProperties.get("jcr:title", String.class);
        description = pageProperties.get("jcr:description", String.class);
        Resource imageResource = resourceResolver.getResource(cardImage);
        if (null != imageResource) {
            imageName = imageResource.getName();
        }
    }

    @Override
    @JsonIgnore
    public String getExportedType() {
        return resource.getResourceType();
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardMessage() {
        return cardMessage;
    }

    public String getCardImage() {
        return cardImage;
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

    public String getImageName() {
        return imageName;
    }
}
