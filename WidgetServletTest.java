package ahm.content.service.core.servlets;


import ahm.content.service.core.models.HtmlComponent;
import ahm.content.service.core.services.WidgetService;
import ahm.content.service.core.services.impl.WidgetServiceImpl;
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

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class WidgetServletTest {


    private WidgetServlet widgetServlet;
    WidgetService widgetService;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        widgetServlet = new WidgetServlet();
        widgetService = mock(WidgetServiceImpl.class);
        Field builderField = WidgetServlet.class.getDeclaredField("widgetService");
        builderField.setAccessible(true);
        builderField.set(widgetServlet, widgetService);
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

    @Test
    public void doGet_Else() throws Exception {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        ResourceResolver resolver = mock(ResourceResolver.class);
        when(request.getResourceResolver()).thenReturn(resolver);
        RequestPathInfo requestPathInfo = mock(RequestPathInfo.class);
        when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
        when(requestPathInfo.getSuffix()).thenReturn("/cb87a8fb-0b78-4e97-b620-b8355d43689b");
        UUID uuid = UUID.fromString("cb87a8fb-0b78-4e97-b620-b8355d43689b");
        when(widgetService.GetWidgetById(uuid,resolver)).thenReturn("");
        widgetServlet.doGet(request,response);
        verify(printWriter,times(0)).print("response");
        verify(response,times(1)).sendError(HttpServletResponse.SC_NOT_FOUND,"Widget cannot be found.");
    }
}
