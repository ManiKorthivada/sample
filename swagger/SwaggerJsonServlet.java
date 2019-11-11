package ahm.content.service.core.servlets;


import ahm.content.service.core.services.SwaggerConfigServiceConsumer;
import ahm.content.service.core.services.SwaggerJsonService;
import ahm.content.service.core.services.VariationListingService;
import ahm.content.service.core.utils.SwaggerUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;


@Component(service = Servlet.class,

        property = {

                "description=Swagger servlet",

                "sling.servlet.resourceTypes=swaggerjson/servlet",
                "sling.servlet.extensions=json"

        })


public class SwaggerJsonServlet extends SlingAllMethodsServlet {

    /**
     * Servlet to create new variations
     */

    Logger log = LoggerFactory.getLogger(this.getClass());


    private static final long serialVersionUID = 2L;

    @Reference
    SwaggerConfigServiceConsumer swaggerConfigServiceConsumer;


    @Override

    public void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        log.info("Page is loaded");
        List<SwaggerJsonService> configurationList;
        configurationList = swaggerConfigServiceConsumer.getConfigurationList();
        log.info("Service: {}", configurationList);
        JSONObject finalSwaggerJson = new JSONObject();
        try {
            SwaggerUtil.setDefaultJsonValues(finalSwaggerJson);
            finalSwaggerJson.put("info", SwaggerUtil.setInfoObject());
            finalSwaggerJson.put("schemes", SwaggerUtil.setSchemaArray());

            JSONObject pathsJson = new JSONObject();
            finalSwaggerJson.put("paths", pathsJson);
            for (int i = 0; i < configurationList.size(); i++) {
                SwaggerJsonService swaggerJsonService = configurationList.get(i);
                SwaggerUtil.setEachMethodJson(pathsJson,swaggerJsonService);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resp.getWriter().print(finalSwaggerJson.toString());
    }


}

 

