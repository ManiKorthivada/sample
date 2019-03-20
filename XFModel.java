package com.aetna.ahm.core.models.qr;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.export.json.SlingModelFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

/**
 * This is the Sling Model Class for r4/components/content/answer
 * This also has Jackson Sling Model Exporter and implements ComponentExporter interface
 *
 * @author Active Health Management
 */
@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ComponentExporter.class,ContainerExporter.class},
        resourceType = {"cq/experience-fragments/editor/components/experiencefragment"},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class XFModel implements ContainerExporter {

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @JsonProperty("fragmentPath")
    private String fragmentPath;

    private Resource imageResource;
    private ValueMap metadataValueMap;

    @SlingObject
    private ResourceResolver resourceResolver;

    private Map<String, ComponentExporter> childModels = null;
    @Inject
    private SlingModelFilter slingModelFilter;

    @Inject
    private ModelFactory modelFactory;

    @Self
    private SlingHttpServletRequest request;

    @PostConstruct
    public void invokepost() {
        imageResource = resourceResolver.getResource(fragmentPath);
    }
    @Override
    public String getExportedType() {
        // TODO Auto-generated method stub
        return "cq/experience-fragments/editor/components/experiencefragment";
    }

    @Nonnull
    @Override
    public Map<String, ? extends ComponentExporter> getExportedItems() {
        if (childModels == null) {
            childModels = getChildModels(request, ComponentExporter.class);
        }

        return childModels;
    }

    @Nonnull
    private <T> Map<String, T> getChildModels(@Nonnull SlingHttpServletRequest slingRequest,
                                              @Nonnull Class<T> modelClass) {
        Map<String, T> itemWrappers = new LinkedHashMap<>();

        for (final Resource child : slingModelFilter.filterChildResources(imageResource.getChildren())) {
            itemWrappers.put(child.getName(), modelFactory.getModelFromWrappedRequest(slingRequest, child, modelClass));
        }

        return  itemWrappers;
    }

    @Nonnull
    @Override
    @JsonIgnore
    public String[] getExportedItemsOrder() {
        return new String[0];
    }
}
