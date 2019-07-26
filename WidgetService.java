package ahm.content.service.core.services;

import java.util.UUID;

import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONArray;

/**
 * Service to provide Widget JSON
 */
public interface WidgetService {

    String GetWidgetById(String widgetId, ResourceResolver resourceResolver) throws Exception;
}
