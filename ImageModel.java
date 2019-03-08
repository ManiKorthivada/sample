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
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
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
        adapters = {ComponentExporter.class},
        resourceType = {"ahm/components/content/image"},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ImageModel implements ComponentExporter {

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @JsonProperty("imagePath")
    private String imagePath;

    private Resource imageResource;
    private ValueMap metadataValueMap;

    @SlingObject
    private ResourceResolver resourceResolver;

    String title;

    String description;

    @PostConstruct
    public void invokepost() {
        imageResource = resourceResolver.getResource(imagePath);
        Resource metadataResource = imageResource.getChild("jcr:content/metadata");
        if (metadataResource != null) {
            metadataValueMap = metadataResource.getValueMap();
        }
        if (null != metadataValueMap) {
            description = metadataValueMap.get("dc:description", String.class);
        }
        if (null != metadataValueMap) {
            title = metadataValueMap.get("jcr:title", String.class);
        }
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public List<Map<String, String>> getRenditions() {
        Resource rendtionResource = imageResource.getChild("jcr:content/renditions");
        List<Map<String, String>> list = new ArrayList<>();
        if (rendtionResource != null) {
            Iterator<Resource> iterator = rendtionResource.listChildren();
            Map map = new HashMap();
            while (iterator.hasNext()){
                Resource eachImageResource = iterator.next();
                map.put(eachImageResource.getName(), eachImageResource.getPath());
            }
            list.add(map);
        }
        return list;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String getExportedType() {
        // TODO Auto-generated method stub
        return "ahm/components/content/image";
    }
}
