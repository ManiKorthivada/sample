package ahm.content.service.core.servlets;

import ahm.content.service.core.services.WidgetService;

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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


/**
 * This also has Jackson Sling Model Exporter
 *
 * @author Active Health Management
 */
@Component(name = "DAM Expose Servlet", immediate = true, service = Servlet.class, property = {
        "sling.servlet.resourceTypes=ahm/servlet",
        "sling.servlet.extensions=json"})
public class WidgetServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private WidgetService widgetService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        final PrintWriter out = response.getWriter();
        final ResourceResolver resolver = request.getResourceResolver();

        String id = request.getParameter("widgetId");
        UUID uid = UUID.fromString(id);

        try {
            String result = widgetService.GetWidgetById(uid, resolver);
            if(StringUtils.isNotBlank(result)){
                out.print(result);
            }else{
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (Exception e) {
            logger.error("Exception in the Wigetservlet while pulling the widget information {}",e);
        }
    }
}