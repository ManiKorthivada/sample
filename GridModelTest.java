package ahm.content.service.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Iterator;

public class GridModelTest {


    private GridModel gridModel = new GridModel();

    SlingHttpServletRequest request;

    @Before
    public void setup() throws Exception {
        request = Mockito.mock(SlingHttpServletRequest.class);
        Field builderField = GridModel.class.getDeclaredField("request");
        builderField.setAccessible(true);
        builderField.set(gridModel, request);
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
