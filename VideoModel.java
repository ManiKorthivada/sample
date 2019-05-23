package com.aetna.ahm.core.models.qr;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.aetna.ahm.core.constants.AHMJsonServiceConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.*;

/**
 * This is the Sling Model Class for r4/components/content/answer
 * This also has Jackson Sling Model Exporter and implements ComponentExporter interface
 *
 * @author Active Health Management
 */
@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {VideoModel.class, ComponentExporter.class},
        resourceType = {"/apps/ahm/components/content/video"},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        selector = AHMJsonServiceConstants.SLING_MODEL_EXPORTER_SELECTOR,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonRootName(value = "video")

public class VideoModel implements ComponentExporter {

    @ValueMapValue
    @JsonProperty("videoPath")
    private String videoPath;

    private Resource imageResource;
    private ValueMap metadataValueMap;

    @SlingObject
    private ResourceResolver resourceResolver;

    String title;

    @PostConstruct
    public void invokepost() {
        imageResource = resourceResolver.getResource(videoPath);

    }

    public String getTitle() {
        title = imageResource.getName();
        return title;
    }


    @Override
    public String getExportedType() {
        // TODO Auto-generated method stub
        return "ahm/components/content/video";
    }
}
