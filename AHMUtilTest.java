package ahm.content.service.core.utils;

import com.day.cq.commons.Externalizer;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class AHMUtilTest {

    @Test
    public void test() {
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        String url = "/content/page.html";
        Externalizer externalizer = Mockito.mock(Externalizer.class);
        Mockito.when(resourceResolver.adaptTo(Externalizer.class)).thenReturn(externalizer);
        Mockito.when(externalizer.externalLink(resourceResolver, Externalizer.LOCAL, url)).thenReturn("locahost:4502//content/page.html");
        String newUrl = AHMUtil.getExternalUrl(resourceResolver, url);
        Assert.assertEquals("locahost:4502//content/page.html", newUrl);
    }
}
