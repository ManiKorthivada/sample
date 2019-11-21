package ahm.content.service.core.models;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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

public class SortOrderTest {


    private SortOrder spySortOrder = new SortOrder();

    Resource resource;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        MemberModifier.field(SortOrder.class,"cardImage").set(spySortOrder,"/content/dam/imagepath.jpeg");
        MemberModifier.field(SortOrder.class,"imageName").set(spySortOrder,"imageNew");
        resource = Mockito.mock(Resource.class);
        MemberModifier.field(SortOrder.class,"resource").set(spySortOrder,resource);
    }

    @Test
    public void test() throws Exception{
        Resource parentResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parentResource);
        Iterator<Resource> iterator = Mockito.mock(Iterator.class);
        Mockito.when(parentResource.listChildren()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        Resource childResource = Mockito.mock(Resource.class);
        Mockito.when(iterator.next()).thenReturn(childResource);
        Mockito.when(childResource.getName()).thenReturn("imageNew");
        Mockito.when(resource.getName()).thenReturn("imageNew");
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(childResource.getResourceResolver()).thenReturn(resourceResolver);
        Mockito.when(resource.getResourceResolver()).thenReturn(resourceResolver);
        Resource imageResource = Mockito.mock(Resource.class);
        Mockito.when(resourceResolver.getResource("/content/dam/imagepath.jpeg")).thenReturn(imageResource);
        Mockito.when(imageResource.getName()).thenReturn("imageNewName");
        ModifiableValueMap map1 = Mockito.mock(ModifiableValueMap.class);
        Mockito.when(childResource.adaptTo(ModifiableValueMap.class)).thenReturn(map1);

        Resource answersResource = Mockito.mock(Resource.class);
        Mockito.when(childResource.getChild("answers")).thenReturn(answersResource);
        Iterator<Resource> iterator1 = Mockito.mock(Iterator.class);
        Mockito.when(answersResource.listChildren()).thenReturn(iterator1);
        Mockito.when(iterator1.hasNext()).thenReturn(true).thenReturn(false);
        Resource answerResource = Mockito.mock(Resource.class);
        Mockito.when(iterator1.next()).thenReturn(answerResource);
        ModifiableValueMap map = Mockito.mock(ModifiableValueMap.class);
        Mockito.when(answerResource.adaptTo(ModifiableValueMap.class)).thenReturn(map);
        ResourceResolver resourceResolver1 = Mockito.mock(ResourceResolver.class);
        Mockito.when(answerResource.getResourceResolver()).thenReturn(resourceResolver1);

        spySortOrder.invokepost();
        Assert.assertEquals("imageNewName",spySortOrder.getImageName());
        Assert.assertEquals("/content/dam/imagepath.jpeg",spySortOrder.getCardImage());
        Mockito.verify(resourceResolver,Mockito.times(1)).commit();
        Mockito.verify(resourceResolver1,Mockito.times(1)).commit();
    }

    @Test
    public void test_differentNames() throws Exception{
        Resource parentResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parentResource);
        Iterator<Resource> iterator = Mockito.mock(Iterator.class);
        Mockito.when(parentResource.listChildren()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        Resource childResource = Mockito.mock(Resource.class);
        Mockito.when(iterator.next()).thenReturn(childResource);
        Mockito.when(childResource.getName()).thenReturn("imageNew1");
        Mockito.when(resource.getName()).thenReturn("imageNew");
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resource.getResourceResolver()).thenReturn(resourceResolver);
        Resource imageResource = Mockito.mock(Resource.class);
        Mockito.when(resourceResolver.getResource("/content/dam/imagepath.jpeg")).thenReturn(imageResource);

        spySortOrder.invokepost();
        Assert.assertNull(spySortOrder.getImageName());
    }
}
