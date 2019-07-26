package ahm.content.service.core.services.impl;

import java.util.*;

import javax.jcr.Session;

import com.day.cq.search.result.Hit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.factory.ModelFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

import ahm.content.service.core.constants.AHMJsonServiceConstants;
import ahm.content.service.core.services.WidgetService;

/**
 * Widget Service Implementation.
 */
@Component(service = WidgetService.class,
        immediate = true)
public class WidgetServiceImpl implements WidgetService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Session session;

    //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ModelFactory modelFactory;

    @Override
    public String GetWidgetById(String widgetId, ResourceResolver resourceResolver) throws Exception {

        log.info("Start retrieving widget for : " + widgetId);

        String widget = StringUtils.EMPTY;

        final Map<String, String> map = new HashMap<String, String>();

        map.put(AHMJsonServiceConstants.QB_PARAM_PATH, AHMJsonServiceConstants.XF_WIDGETS_PATH);
        map.put("property", "jcr:content/sling:alias");
        map.put("property.value", widgetId.toString());
        map.put("type", "cq:Page");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));

        SearchResult result = query.getResult();


        Iterator<Hit> hitIterator = result.getHits().iterator();

        if (hitIterator.hasNext()) {

            Resource parentResource = resourceResolver.getResource(hitIterator.next().getPath());
            Resource rootResource = parentResource.getChild("jcr:content/root");

            if (rootResource.hasChildren()) {

                Iterator<Resource> childResources = rootResource.listChildren();

                while (childResources.hasNext()) {

                    Resource masterWidgetResource = childResources.next();

                    try {
                        widget = modelFactory.exportModelForResource(masterWidgetResource, ExporterConstants.SLING_MODEL_EXPORTER_NAME, String.class, new HashMap<String, String>());

                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage());
                    }
                }
            }
        } else {
            log.error("Widget {} cannot be found.", widgetId);
        }

        log.info("End retrieving widget for : " + widgetId + ". Output : " + widget);

        return widget;
    }

    @Activate
    protected void activate() {
        log.info("WidgetService started.");
    }

    @Deactivate
    protected void deactivate() {
        log.info("WidgetService ended.");
    }
}
