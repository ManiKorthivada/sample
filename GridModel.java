package com.aetna.ahm.core.models.qr;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.sightly.WCMUsePojo;
import com.google.common.collect.Iterators;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Iterator;


/**
 * The Class ConfigDataUse.
 */
@Model(adaptables = {SlingHttpServletRequest.class,Resource.class})
public class GridModel {


    @Inject
    SlingHttpServletRequest request;

    int noOfComponents = 0;

    boolean isVideoComponent = false;

    /*
     * (non-Javadoc)
     *
     * @see com.adobe.cq.sightly.WCMUsePojo#activate()
     */
    @PostConstruct
    public void init() throws Exception {
        Resource resource = request.getResource();
        if("ahm/components/structure/xfpagewidget".equalsIgnoreCase(resource.getResourceType())){
            resource = resource.getChild("root");
        }
        Iterator<Resource> iterator = resource.listChildren();
        while (iterator.hasNext()){
            Resource componentResource = iterator.next();
            ValueMap valueMap = componentResource.getValueMap();
            String resourceType = valueMap.get("sling:resourceType",String.class);
            if("ahm/components/content/video".equalsIgnoreCase(resourceType)){
                isVideoComponent = true;
            }
        }
        iterator = resource.listChildren();
        noOfComponents = Iterators.size(iterator);
    }

    public int getNoOfComponents() {
        return noOfComponents;
    }

    public boolean isVideoComponent() {
        return isVideoComponent;
    }
}