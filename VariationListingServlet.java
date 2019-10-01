package ahm.content.service.core.servlets;


import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Component(service = Servlet.class,

        property = {

                "description=Variation servlet",

                "sling.servlet.resourceTypes=variationlist/servlet",
                "sling.servlet.extensions=json"

        })


public class VariationListingServlet extends SlingAllMethodsServlet {

    /**
     * Servlet to create new variations
     */

    Logger logger = LoggerFactory.getLogger(this.getClass());


    private static final long serialVersionUID = 2L;


    @Reference
    ResourceResolverFactory resolverFactory;


    @Override

    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/json");

        PageManager pageManager = req.getResourceResolver().adaptTo(PageManager.class);

        String path = req.getParameter("path");

        final String parentPath = ResourceUtil.getParent(path);
        Page parentPage = pageManager.getPage(parentPath);

        Iterator<Page> childPages = parentPage.listChildren();
        JSONArray jsonArray = new JSONArray();

        try {
            while (childPages.hasNext()) {
                Page childPage = childPages.next();
                String name = childPage.getName();

                if (!StringUtils.equals(name, "master")) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("variationName", name);
                    jsonObject.put("variationPath", childPage.getPath()+".html");
                    jsonArray.put(jsonObject);
                }
            }
            resp.getWriter().print(jsonArray.toString());
        } catch (JSONException e) {
            logger.error("Error while fetching the variations",e);
        }

    }
}

 

