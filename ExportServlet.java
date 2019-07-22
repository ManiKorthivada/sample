package ahm.content.service.core.servlets;

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
import org.json.JSONException;
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
        Resource resource = resolver.getResource(currentIdPath + "/master/jcr:content/root");
        JSONArray jsonArray = new JSONArray();
        if (resource.hasChildren()) {
            Iterator<Resource> childResources = resource.listChildren();
            while (childResources.hasNext()) {
                Resource child = childResources.next();
                VideoModel videoModel = child.adaptTo(VideoModel.class);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JSONObject jsonObject = new JSONObject(objectMapper.writeValueAsString(videoModel));
                    jsonArray.put(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        out.print(jsonArray);
    }

    private String getPathfromId(SlingHttpServletRequest request, ResourceResolver resolver) {
        String id = request.getParameter("widgetId");
        final Map<String, String> map = new HashMap<String, String>();
        map.put("type", "cq:Page");
        map.put("path", "/content/experience-fragments");
        map.put("property", "jcr:content/id");
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
