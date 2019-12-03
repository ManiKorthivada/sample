package ahm.content.service.core.services.impl;

import java.util.*;

import javax.jcr.Session;

import ahm.content.service.core.models.RevealCardComponent;
import ahm.content.service.core.models.RevealCardModel;
import ahm.content.service.core.utils.AHMUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.factory.ModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
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
@Component(
        service = WidgetService.class,
        immediate = true
)

public class WidgetServiceImpl implements WidgetService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ModelFactory modelFactory;

    @Override
    public String GetWidgetById(UUID widgetId, ResourceResolver resourceResolver) {

        log.info("Start retrieving widget for : " + widgetId);

        String widget = null;

        final Map<String, String> map = new HashMap<String, String>();

        map.put(AHMJsonServiceConstants.QB_PARAM_PATH, AHMJsonServiceConstants.XF_WIDGETS_PATH);
        map.put(AHMJsonServiceConstants.QB_PARAM_PROPERTY, AHMJsonServiceConstants.SLING_ALIAS);
        map.put(AHMJsonServiceConstants.QB_PARAM_PROPERTY_VALUE, widgetId.toString());
        map.put("type", "cq:PageContent");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
        SearchResult result = query.getResult();
        Iterator<Resource> resources = result.getResources();

        if (!resources.hasNext()) {
            log.info("Widget {} cannot be found.", widgetId);

            return widget;
        }

        Resource resource = resources.next();
        ValueMap resourceValues = resource.getValueMap();
        String resourceType = resourceValues.get(AHMJsonServiceConstants.SLING_RT, String.class);

        if (resourceType == null) {

            log.info("No resource type found for Widget {}.", widgetId);
            return widget;
        }

        if (resourceType.equalsIgnoreCase(AHMJsonServiceConstants.XF_RT)) {

            Resource parentResource = resource.getParent();
            resource = parentResource.getChild("master/jcr:content");
        }

        try {
            widget = modelFactory.exportModelForResource(resource, ExporterConstants.SLING_MODEL_EXPORTER_NAME, String.class, new HashMap<String, String>());

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        log.info("End retrieving widget for : " + widgetId + ". Output : " + widget);
        return widget;
    }

    @Override
    public String GetOriginalRenditionById(UUID widgetId, ResourceResolver resourceResolver, String renditionParam) throws Exception {
        String string = GetWidgetById(widgetId, resourceResolver);
        if (StringUtils.isNotBlank(string)) {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray imageJson = new JSONArray();
            JSONArray renditionJson = new JSONArray();
            JSONArray contentDetails = jsonObject.getJSONArray("ContentDetails");
            for (int index = 0; index < contentDetails.length(); index++) {
                JSONObject componentObject = contentDetails.getJSONObject(index);
                String imagePath = componentObject.getString("ImagePath");
                String externalizerConfigUrl = AHMUtil.getExternalUrl(resourceResolver, "/");
                imagePath = imagePath.replace(externalizerConfigUrl, "/content/dam/");
                imageJson.put(imagePath);
                Resource imageResource = resourceResolver.getResource(imagePath);
                renditionJson = generateRenditionsArray(imageResource);
                componentObject.put("imageRenditions",renditionJson);
            }
            if (StringUtils.isNotBlank(renditionParam) && StringUtils.equalsIgnoreCase(renditionParam, "original")) {
                return imageJson.toString();
            } else if (StringUtils.isNotBlank(renditionParam) && StringUtils.equalsIgnoreCase(renditionParam, "renditions")) {
                return jsonObject.toString();
            }
        }
        return null;
    }

    private JSONArray generateRenditionsArray(Resource imageResource) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        if (null != imageResource) {
            Resource renditionResource = imageResource.getChild("jcr:content/renditions");
            if (renditionResource != null) {
                Iterator<Resource> iterator = renditionResource.listChildren();
                JSONObject jsonObject = new JSONObject();
                while (iterator.hasNext()) {
                    Resource eachImageResource = iterator.next();
                    ResourceResolver resourceResolver = imageResource.getResourceResolver();
                    jsonObject.put(eachImageResource.getName(), AHMUtil.getExternalUrl(resourceResolver,resourceResolver.map(eachImageResource.getPath())));
                }
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray;
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