package ahm.content.service.core.servlets;

import ahm.content.service.core.models.HtmlModel;
import ahm.content.service.core.models.VideoModel;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * This is the Servlet Class for AnswerPathDropdown in Answer Component
 * This also has Jackson Sling Model Exporter
 *
 * @author Active Health Management
 */
@Component(name = "DAM Expose Servlet", immediate = true, service = Servlet.class, property = {
        "sling.servlet.resourceTypes=ahm/servlet",
        "sling.servlet.extensions=json"})
public class ExportServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        final PrintWriter out = response.getWriter();
        final ResourceResolver resolver = request.getResourceResolver();
        String currentIdPath = getPathfromId(request, resolver);
        logger.debug("Current Path of the ID parameter {}", currentIdPath);
        Resource jcrResource = resolver.getResource(currentIdPath + "/jcr:content");
        if (null != jcrResource) {
            ValueMap valueMap = jcrResource.getValueMap();
            String template = valueMap.get("cq:template", String.class);
            logger.debug("Current template path {}", template);
            String resourcePath = getResourcePath(request, resolver, template, currentIdPath);
            Resource resource = resolver.getResource(resourcePath).getParent();
            JSONArray jsonArray = new JSONArray();
            if (resource.hasChildren()) {
                Iterator<Resource> childResources = resource.listChildren();
                getFinalJsonArray(template, jsonArray, childResources);
            }
            out.print(jsonArray);
        }
    }

    private void getFinalJsonArray(String template, JSONArray jsonArray, Iterator<Resource> childResources) {
        while (childResources.hasNext()) {
            Resource child = childResources.next();
            ValueMap valueMap1 = child.getValueMap();
            String resourcetype = valueMap1.get("sling:resourceType", String.class);
            JSONObject jsonObject = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                if ("dgtl-content/components/content/html".equalsIgnoreCase(resourcetype)) {
                    logger.debug("Html Resource {}", template);
                    HtmlModel htmlModel = child.adaptTo(HtmlModel.class);
                    jsonObject = new JSONObject(objectMapper.writeValueAsString(htmlModel));
                } else if ("dgtl-content/components/content/video".equalsIgnoreCase(resourcetype)) {
                    logger.debug("Video Resource {}", template);
                    VideoModel videoModel = child.adaptTo(VideoModel.class);
                    jsonObject = new JSONObject(objectMapper.writeValueAsString(videoModel));
                }
            } catch (Exception e) {

            }
            jsonArray.put(jsonObject);
        }
    }

    private String getResourcePath(SlingHttpServletRequest request, ResourceResolver resolver, String template, String currentIdPath) {
        final Map<String, String> map = new HashMap<String, String>();
        String value = StringUtils.EMPTY;
        value = getResourceValue(template, value);
        map.put("type", "nt:unstructured");
        map.put("path", currentIdPath);
        map.put("property", "sling:resourceType");
        map.put("property.value", value);
        map.put("p.limit", "-1");
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resolver.adaptTo(Session.class));
        SearchResult result = query.getResult();
        for (final Hit hit : result.getHits()) {
            try {
                return hit.getPath();
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.EMPTY;
    }

    private String getResourceValue(String template, String value) {
        if ("/conf/ahm/settings/wcm/templates/video-xf-template".equalsIgnoreCase(template)) {
            value = "dgtl-content/components/content/video";
        }
        if ("/conf/ahm/settings/wcm/templates/html-xf-template".equalsIgnoreCase(template)) {
            value = "dgtl-content/components/content/html";
        }
        return value;
    }

    private String getPathfromId(SlingHttpServletRequest request, ResourceResolver resolver) {
        String id = request.getParameter("widgetId");
        final Map<String, String> map = new HashMap<String, String>();
        map.put("type", "cq:Page");
        map.put("path", "/content/experience-fragments");
        map.put("property", "jcr:content/sling:alias");
        map.put("property.value", id);
        map.put("p.limit", "-1");
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resolver.adaptTo(Session.class));
        SearchResult result = query.getResult();
        for (final Hit hit : result.getHits()) {
            try {
                return hit.getPath();
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.EMPTY;
    }
}
