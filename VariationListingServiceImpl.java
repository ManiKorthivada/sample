package ahm.content.service.core.services.impl;

import ahm.content.service.core.services.VariationListingService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

@Component(
        service = VariationListingService.class,
        immediate = true
)
public class VariationListingServiceImpl implements VariationListingService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getVariationList(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);

        String path = request.getParameter("path");

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
                    jsonObject.put("variationPath", "/editor.html"+childPage.getPath() + ".html");
                    jsonArray.put(jsonObject);
                }
            }
            return jsonArray.toString();
        } catch (JSONException e) {
            logger.error("Error while fetching the variations", e);
        }
        return null;
    }
}
