package com.aetna.ahm.core.models.qr;

import com.adobe.cq.sightly.WCMUsePojo;
import com.google.common.collect.Iterators;
import org.apache.sling.api.resource.Resource;

import java.util.Iterator;


/**
 * The Class ConfigDataUse.
 */
public class GridModel extends WCMUsePojo {


    int noOfComponents = 0;

    /*
     * (non-Javadoc)
     *
     * @see com.adobe.cq.sightly.WCMUsePojo#activate()
     */
    @Override
    public void activate() throws Exception {
        Resource resource = getResource();
        Iterator<Resource> iterator = resource.listChildren();
        noOfComponents = Iterators.size(iterator);
    }

    public int getNoOfComponents() {
        return noOfComponents;
    }
}