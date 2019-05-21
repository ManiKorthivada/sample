package com.aetna.ahm.core.models.qr;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.Iterators;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Iterator;


/**
 * The Class ConfigDataUse.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class UpdateDescriptionModel {


    @Inject
    SlingHttpServletRequest request;

    @SlingObject
    Resource resource;

    /*
     * (non-Javadoc)
     *
     * @see com.adobe.cq.sightly.WCMUsePojo#activate()
     */
    @PostConstruct
    public void init() throws Exception {
        Page currentPage = resource.getParent().adaptTo(Page.class);
        Resource jcrResource = currentPage.getParent().getContentResource();
        ValueMap parentValueMap = jcrResource.getValueMap();
        Iterator<Page> iterator = currentPage.getParent().listChildren();
        if (iterator.hasNext()) {
            Page childResource1 = iterator.next();
            ModifiableValueMap childValueMap = childResource1.getContentResource().adaptTo(ModifiableValueMap.class);
            if (!childValueMap.containsKey("jcr:description")) {
                childValueMap.put("jcr:description", parentValueMap.get("jcr:description", String.class));
                childResource1.getContentResource().getResourceResolver().commit();
            }
        }
    }
}