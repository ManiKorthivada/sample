package com.sony.sie.shop.foundation.core.services;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.sony.sie.shop.foundation.core.services.impl.VariationListingServiceImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Iterator;

public class VariationListingServiceImplTest {

    @Test
    public void test(){
        SlingHttpServletRequest request = Mockito.mock(SlingHttpServletRequest.class);
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(request.getResourceResolver()).thenReturn(resourceResolver);
        PageManager pageManager = Mockito.mock(PageManager.class);
        Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        Mockito.when(request.getParameter("path")).thenReturn("/content/parent/child");
        Page parentPage = Mockito.mock(Page.class);
        Mockito.when(pageManager.getPage("/content/parent")).thenReturn(parentPage);
        Iterator<Page> childPages = Mockito.mock(Iterator.class);
        Mockito.when(parentPage.listChildren()).thenReturn(childPages);
        Mockito.when(childPages.hasNext()).thenReturn(true).thenReturn(false);
        Page childPage = Mockito.mock(Page.class);
        Mockito.when(childPages.next()).thenReturn(childPage);
        Mockito.when(childPage.getName()).thenReturn("name");
        Mockito.when(childPage.getPath()).thenReturn("/path");
        VariationListingService variationListingService = new VariationListingServiceImpl();
        String json = variationListingService.getVariationList(request,response);
        Assert.assertEquals("[{\"variationName\":\"name\",\"variationPath\":\"/path.html\"}]",json);
    }

}
