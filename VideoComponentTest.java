package ahm.content.service.core.models;

import com.day.cq.commons.Externalizer;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;


public class VideoComponentTest {


    private VideoComponent videoComponent = new VideoComponent();

    Resource resource;

    @Before
    public void setup() throws Exception {
        resource = Mockito.mock(Resource.class);
        Field builderField = VideoComponent.class.getDeclaredField("resource");
        builderField.setAccessible(true);
        builderField.set(videoComponent, resource);
    }

    @Test
    public void test() throws Exception{
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resource.getResourceResolver()).thenReturn(resourceResolver);
        Mockito.when(values.get("videotype", String.class)).thenReturn("external");
        Mockito.when(values.get("videoPath",String.class)).thenReturn("/pathExternal");
        videoComponent.invokepost();
        Assert.assertEquals("external",videoComponent.getVideoType());
        Assert.assertEquals("/pathExternal",videoComponent.getVideoUrl());

        Mockito.when(values.get("videotype", String.class)).thenReturn("upload");
        Mockito.when(values.get("videoPath",String.class)).thenReturn("/pathUpload");
        Mockito.when(resource.getResourceResolver()).thenReturn(resourceResolver);
        Resource videoResource = Mockito.mock(Resource.class);
        Mockito.when(resourceResolver.getResource("/pathUpload/jcr:content/metadata")).thenReturn(videoResource);
        Mockito.when(videoResource.getValueMap()).thenReturn(values);
        Mockito.when(values.get("dclosedcaption",String.class)).thenReturn("videoCC");
        Mockito.when(resourceResolver.map("videoCC")).thenReturn("videoCC");
        Mockito.when(resourceResolver.map("/pathUpload")).thenReturn("/pathUpload");
        Externalizer externalizer = Mockito.mock(Externalizer.class);
        Mockito.when(resourceResolver.adaptTo(Externalizer.class)).thenReturn(externalizer);
        Mockito.when(externalizer.externalLink(resourceResolver, Externalizer.LOCAL, "videoCC")).thenReturn("videoCC");
        Mockito.when(externalizer.externalLink(resourceResolver, Externalizer.LOCAL, "/pathUpload")).thenReturn("/pathUpload");
        videoComponent.invokepost();
        Assert.assertEquals("/pathUpload",videoComponent.getVideoUrl());
        Assert.assertEquals("upload",videoComponent.getVideoType());
    }
}
