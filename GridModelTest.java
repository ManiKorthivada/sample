package ahm.content.service.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GridModel.class})
public class GridModelTest {

    @InjectMocks
    private GridModel gridModel;

    SlingHttpServletRequest request;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        request = Mockito.mock(SlingHttpServletRequest.class);
        MemberModifier.field(GridModel.class,"request").set(gridModel,request);
    }

    @Test
    public void test() throws Exception{
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(request.getResource()).thenReturn(resource);
        Mockito.when(resource.getResourceType()).thenReturn("dgtl-content/components/structure/xfpagewidget");
        Mockito.when(resource.getChild("root")).thenReturn(resource);
        Iterator<Resource> iterator = Mockito.mock(Iterator.class);
        Mockito.when(resource.listChildren()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        gridModel.init();
        Assert.assertEquals(1,gridModel.getNoOfComponents());
    }
}
