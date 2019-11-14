package ahm.content.service.core.servlets;


import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.sling.api.resource.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;


@Component(service=Servlet.class,

property={

"description=Variation servlet",

"sling.servlet.resourceTypes=variation/servlet",
"sling.servlet.extensions=json"

})

 

public class ContentVariationServlet extends SlingAllMethodsServlet {

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
        
        String path = req.getParameter("path");
        String lan = req.getParameter("language");
       

        PageManager pageManager = req.getResourceResolver().adaptTo(PageManager.class);
        final String parentPath = ResourceUtil.getParent(path);
        final String name = ResourceUtil.getName(parentPath);
        String destination = parentPath+"/"+name+"-" +lan;
        String beforeName = "";
        boolean shallow = true;
        boolean resolveConflict = false;
        boolean autoSave = true;
        
        //Creating a login session to update the properties.

        Map<String, Object> param = new HashMap<String, Object>();  
        param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
        ResourceResolver resourceResolver=null;

        
        try {
        	Page currentPage = pageManager.getPage(path);
        	
        	// Creating variation.
        	
            pageManager.copy(currentPage,
                    destination,
                    beforeName,
                    shallow,
                    resolveConflict,
            		  autoSave);
                      
            logger.info("Content variation successfully created with path - " + destination); 
            
            resourceResolver = resolverFactory.getServiceResourceResolver(param);
            Resource pageResource = resourceResolver.getResource(parentPath+"/"+name+"-" +lan+ "/jcr:content");
            Node myNode = pageResource.adaptTo(Node.class);

        	 //Setting properties.
 
            myNode.setProperty("jcr:language", lan);
            Session session = resourceResolver.adaptTo(Session.class);
            session.save();
            session.logout();

        }
        catch (IllegalArgumentException | LoginException | RepositoryException | WCMException e) {
            logger.error("Variation exists",e);
            resp.sendError(HttpServletResponse.SC_CONFLICT,e.getMessage());
        }
    }
}

 

