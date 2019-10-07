package com.sony.sie.shop.foundation.core.servlets;


import com.sony.sie.shop.foundation.core.services.VariationListingService;
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

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(VariationListingServlet.class)
public class VariationListingServletTest {

    @InjectMocks
    private VariationListingServlet variationListingServlet;

    @Mock
    VariationListingService variationListingService;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        variationListingServlet = new VariationListingServlet();
        PrivateAccessor.setField(variationListingServlet, "variationListingService", variationListingService);
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
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
