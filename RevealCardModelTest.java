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

import java.util.Iterator;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RevealCardModel.class})
public class RevealCardModelTest {

    @InjectMocks
    private RevealCardModel revealCardModel;

    Resource resource;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        resource = Mockito.mock(Resource.class);
        MemberModifier.field(RevealCardModel.class,"resource").set(revealCardModel,resource);
    }

    @Test
    public void test() throws Exception{
        Resource parent = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parent);
        Mockito.when(parent.getName()).thenReturn("parentName");
        Resource revealModelResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getChild("root/responsivegrid/")).thenReturn(revealModelResource);
        Iterator<Resource> iteratorExp = Mockito.mock(Iterator.class);
        Mockito.when(revealModelResource.listChildren()).thenReturn(iteratorExp);
        Mockito.when(iteratorExp.hasNext()).thenReturn(true).thenReturn(false);
        Resource childResource =Mockito.mock(Resource.class);
        Mockito.when(iteratorExp.next()).thenReturn(childResource);
        RevealCardComponent map = Mockito.mock(RevealCardComponent.class);
        Mockito.when(childResource.adaptTo(RevealCardComponent.class)).thenReturn(map);
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        revealCardModel.invokepost();
        Assert.assertEquals("Reveal Card",revealCardModel.getWidgetType());

    }
}
