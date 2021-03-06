package ahm.content.service.core.models;

import org.apache.sling.api.resource.Resource;
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
@PrepareForTest({HtmlModel.class})
public class HtmlModelTest {

    @InjectMocks
    private HtmlModel htmlModel;

    Resource resource;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        resource = Mockito.mock(Resource.class);
        MemberModifier.field(HtmlModel.class,"resource").set(htmlModel,resource);
    }

    @Test
    public void test() throws Exception{
        Resource parent = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parent);
        Mockito.when(parent.getName()).thenReturn("parentName");
        Resource videoModelResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getChild("root/responsivegrid/html")).thenReturn(videoModelResource);
        VideoComponent videoComponent = Mockito.mock(VideoComponent.class);
        Mockito.when(videoModelResource.adaptTo(VideoComponent.class)).thenReturn(videoComponent);
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        htmlModel.invokepost();
        Assert.assertEquals("parentName",htmlModel.getName());
    }
}
