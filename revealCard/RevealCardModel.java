package com.aetna.ahm.core.models.qr;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.aetna.ahm.core.beans.RevealCardBean;
import com.aetna.ahm.core.constants.AHMJsonServiceConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is the Sling Model Class for r4/components/content/answer
 * This also has Jackson Sling Model Exporter and implements ComponentExporter interface
 *
 * @author Active Health Management
 */
@Model(
        adaptables = {Resource.class},
        adapters = ComponentExporter.class,
        resourceType = {"ahm/components/content/revealcard"},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        selector = AHMJsonServiceConstants.SLING_MODEL_EXPORTER_SELECTOR,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION,
        options = {
                @ExporterOption(name = "SerializationFeature.WRAP_ROOT_VALUE", value = "true")
        }
)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonRootName(value = "RevealCard")

public class RevealCardModel implements ComponentExporter {

    @SlingObject
    Resource resource;

    @Inject
    @JsonProperty("cardTitle")
    private String cardTitle;

    @Inject
    @JsonProperty("cardMessage")
    private String cardMessage;

    @Inject
    @JsonProperty("cardImagePath")
    private String cardImagePath;

    @JsonProperty("ImageName")
    private String imageName;

    @PostConstruct
    public void invokepost() throws Exception {
        Resource parentResource = resource.getParent();
        Iterator<Resource> iterator = parentResource.listChildren();
        int count = 0;
        while (iterator.hasNext()) {
            Resource childResource = iterator.next();
            count++;
            if (childResource.getName().equalsIgnoreCase(resource.getName())) {
                ModifiableValueMap map = childResource.adaptTo(ModifiableValueMap.class);
                map.put("sortOrder", count);
                childResource.getResourceResolver().commit();
                break;
            }
        }
        if (StringUtils.isNotEmpty(cardImagePath)) {
            Resource imageResource = resource.getResourceResolver().getResource(cardImagePath);
            imageName = imageResource.getName();
        }

    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardMessage() {
        return cardMessage;
    }

    public String getCardImagePath() {
        return cardImagePath;
    }

    public String getImageName() {
        return imageName;
    }

    @Override
    @JsonIgnore
    public String getExportedType() {
        // TODO Auto-generated method stub
        return null;
    }
}