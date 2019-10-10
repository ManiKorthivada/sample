package ahm.content.service.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({VideoComponent.class})
public class VideoComponentTest {

    @InjectMocks
    private VideoComponent videoComponent;

    Resource resource;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        resource = Mockito.mock(Resource.class);
        MemberModifier.field(VideoComponent.class,"resource").set(videoComponent,resource);
    }

    @Test
    public void test() throws Exception{
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        Mockito.when(values.get("videotype", String.class)).thenReturn("external");
        Mockito.when(values.get("videoPathExternal",String.class)).thenReturn("/pathExternal");
        videoComponent.invokepost();
        Assert.assertEquals("external",videoComponent.getVideoType());
        Assert.assertEquals("/pathExternal",videoComponent.getVideoUrl());

        Mockito.when(values.get("videotype", String.class)).thenReturn("upload");
        Mockito.when(values.get("videoPathUpload",String.class)).thenReturn("/pathUpload");
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resource.getResourceResolver()).thenReturn(resourceResolver);
        Resource videoResource = Mockito.mock(Resource.class);
        Mockito.when(resourceResolver.getResource("/pathUpload/jcr:content/metadata")).thenReturn(videoResource);
        Mockito.when(videoResource.getValueMap()).thenReturn(values);
        Mockito.when(values.get("dclosedcaption",String.class)).thenReturn("videoCC");
        videoComponent.invokepost();
        Assert.assertEquals("/pathUpload",videoComponent.getVideoUrl());
        Assert.assertEquals("upload",videoComponent.getVideoType());
    }
}
