package ahm.content.service.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ContentVariationServlet.class)
public class ContentVariationServletTest {

    @InjectMocks
    private ContentVariationServlet contentVariationServlet;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private ResourceResolverFactory resourceResolverFactory;

    @Before
    public void setUp() throws Exception {
        contentVariationServlet = new ContentVariationServlet();
        PrivateAccessor.setField(contentVariationServlet, "resolverFactory", resourceResolverFactory);
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
    }

    @Test
    public void doGet_test() throws ServletException, Exception {

        Mockito.when(request.getParameter("path")).thenReturn("/content/path.html");
        Mockito.when(request.getParameter("language")).thenReturn("en");
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(request.getResourceResolver()).thenReturn(resourceResolver);
        PageManager pageManager = Mockito.mock(PageManager.class);
        Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        Page page = Mockito.mock(Page.class);
        Mockito.when(pageManager.getPage("/content/path.html")).thenReturn(page);
        Mockito.when(resourceResolverFactory.getServiceResourceResolver(any())).thenReturn(resourceResolver);
        Resource pageResource = Mockito.mock(Resource.class);
        Mockito.when(resourceResolver.getResource("/content/content-en/jcr:content")).thenReturn(pageResource);
        Node myNode = Mockito.mock(Node.class);
        Mockito.when(pageResource.adaptTo(Node.class)).thenReturn(myNode);
        Session session = Mockito.mock(Session.class);
        Mockito.when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
        contentVariationServlet.doGet(request,response);
        Mockito.verify(session,times(1)).save();
        Mockito.verify(session,times(1)).logout();
    }
}
