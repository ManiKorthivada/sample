package ahm.content.service.core.models;

import junitx.util.PrivateAccessor;
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

public class HtmlModelTest {


    private HtmlModel htmlModel;

    Resource resource;

    @Before
    public void setup() throws Exception {
        resource = Mockito.mock(Resource.class);
        htmlModel = new HtmlModel();
        PrivateAccessor.setField(htmlModel, "resource", resource);
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

    @Test
    public void test_null_resource() throws Exception{
        Resource parent = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parent);
        Mockito.when(parent.getName()).thenReturn("");
        Resource videoModelResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getChild("root/responsivegrid/html")).thenReturn(null);
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        htmlModel.invokepost();
        Assert.assertEquals("",htmlModel.getName());
    }
}
