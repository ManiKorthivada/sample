package ahm.content.service.core.services;

import java.util.UUID;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * Service to provide Widget JSON
 */
public interface WidgetService {

    String GetWidgetById(UUID widgetId, ResourceResolver resourceResolver) throws Exception;

    String GetOriginalRenditionById(UUID widgetId, ResourceResolver resourceResolver, String renditionParam) throws Exception;

    String GetRenditionById(UUID widgetId, ResourceResolver resourceResolver, String renditionParam) throws Exception;
}