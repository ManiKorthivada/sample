package ahm.content.service.core.utils;

import com.day.cq.commons.Externalizer;
import org.apache.sling.api.resource.ResourceResolver;

public class AHMUtil {

    public static String getExternalUrl(ResourceResolver resourceResolver, String url) {
        Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
        return externalizer.externalLink(resourceResolver, Externalizer.LOCAL, url);
    }
}
