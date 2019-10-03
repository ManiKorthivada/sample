package ahm.content.service.core.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

public interface VariationListingService {
    String getVariationList(SlingHttpServletRequest request, SlingHttpServletResponse response);
}
