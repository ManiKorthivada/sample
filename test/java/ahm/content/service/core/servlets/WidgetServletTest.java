package ahm.content.service.core.servlets;


import ahm.content.service.core.services.WidgetService;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.PrintWriter;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WidgetServlet.class)
public class WidgetServletTest {

    @InjectMocks
    private WidgetServlet widgetServlet;

    @Mock
    WidgetService widgetService;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        widgetServlet = new WidgetServlet();
        PrivateAccessor.setField(widgetServlet, "widgetService", widgetService);
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
    }

    @Test
    public void doGet_test() throws Exception {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        ResourceResolver resolver = mock(ResourceResolver.class);
        when(request.getResourceResolver()).thenReturn(resolver);
        RequestPathInfo requestPathInfo = mock(RequestPathInfo.class);
        when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
        when(requestPathInfo.getSuffix()).thenReturn("/cb87a8fb-0b78-4e97-b620-b8355d43689b");
        UUID uuid = UUID.fromString("cb87a8fb-0b78-4e97-b620-b8355d43689b");
        when(widgetService.GetWidgetById(uuid,resolver)).thenReturn("response");
        widgetServlet.doGet(request,response);
        verify(printWriter,times(1)).print("response");
    }
}
