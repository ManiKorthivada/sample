package ahm.content.service.core.utils;

import com.day.cq.commons.Externalizer;
import org.apache.sling.api.resource.ResourceResolver;

public class AHMUtil {

    public static String getPublishExternalUrl(ResourceResolver resourceResolver,String url){
        Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
        return externalizer.publishLink(resourceResolver,url);
    }

    public static String getAuthorExternalUrl(ResourceResolver resourceResolver,String url){
        Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
        return externalizer.authorLink(resourceResolver,url);
    }
}
