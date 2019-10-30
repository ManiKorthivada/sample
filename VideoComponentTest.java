package ahm.content.service.core.models;

import ahm.content.service.core.utils.AHMUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({VideoComponent.class, AHMUtil.class})
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
        PowerMockito.mockStatic(AHMUtil.class);
        Mockito.when(resourceResolver.map("videoCC")).thenReturn("videoCC");
        Mockito.when(resourceResolver.map("/pathUpload")).thenReturn("/pathUpload");
        PowerMockito.when(AHMUtil.getExternalUrl(resourceResolver,"videoCC")).thenReturn("videoCC");
        PowerMockito.when(AHMUtil.getExternalUrl(resourceResolver,"/pathUpload")).thenReturn("/pathUpload");
        videoComponent.invokepost();
        Assert.assertEquals("/pathUpload",videoComponent.getVideoUrl());
        Assert.assertEquals("upload",videoComponent.getVideoType());
    }
}
