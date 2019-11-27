package ahm.content.service.core.servlets;

import ahm.content.service.core.services.WidgetService;

import com.day.cq.search.QueryBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component(
        name = "DAM Expose Servlet",
        immediate = true,
        service = Servlet.class,
        property = {
                "sling.servlet.resourceTypes=digital/servlet",
                "sling.servlet.extensions=json"
        }
)

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
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        final PrintWriter out = response.getWriter();
        final ResourceResolver resolver = request.getResourceResolver();

        String id = request.getRequestPathInfo().getSuffix();
        if (StringUtils.startsWith(id, "/")) {

            id = StringUtils.substring(id, 1);
        }
        String[] suffixParam = id.split("/");
        UUID uid = null;
        String result = StringUtils.EMPTY;
        if (suffixParam.length > 1) {
            String renditionParam = suffixParam[1];
            uid = UUID.fromString(suffixParam[0]);
            try {
                result = widgetService.GetOriginalRenditionById(uid, resolver, renditionParam);
            } catch (Exception e) {
                logger.error("Exception in the Wigetservlet while pulling the widget information {}", e);
            }
        } else {
            try {
                uid = UUID.fromString(id);
                result = widgetService.GetWidgetById(uid, resolver).toString();
            } catch (Exception e) {
                logger.error("Exception in the Wigetservlet while pulling the widget information {}", e);
            }
        }
        if (StringUtils.isNotBlank(result)) {
            out.print(result);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Widget cannot be found.");
        }
    }
}