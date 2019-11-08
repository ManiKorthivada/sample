package ahm.content.service.core.servlets;


import ahm.content.service.core.models.HtmlComponent;
import ahm.content.service.core.services.VariationListingService;
import ahm.content.service.core.services.impl.VariationListingServiceImpl;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;


public class VariationListingServletTest {


    private VariationListingServlet variationListingServlet;


    VariationListingService variationListingService;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        variationListingServlet = new VariationListingServlet();
        variationListingService = mock(VariationListingServiceImpl.class);
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);

        Field builderField = VariationListingServlet.class.getDeclaredField("variationListingService");
        builderField.setAccessible(true);
        builderField.set(variationListingServlet, variationListingService);
    }

    @Test
    public void doGet_test() throws ServletException, IOException, JSONException {
        when(variationListingService.getVariationList(request, response)).thenReturn("response");
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        variationListingServlet.doGet(request, response);
        verify(printWriter,times(1)).print("response");
    }
}
