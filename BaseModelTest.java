package ahm.content.service.core.models;

import ahm.content.service.core.constants.AHMJsonServiceConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.*;

public class BaseModelTest {


    private BaseModel baseModel = new BaseModel();

    Resource resource;

    @Before
    public void setup() throws Exception {
        resource = Mockito.mock(Resource.class);
        Field builderField = BaseModel.class.getDeclaredField("resource");
        builderField.setAccessible(true);
        builderField.set(baseModel, resource);
    }

    @Test
    public void test() throws Exception{
        Resource parent = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parent);
        Mockito.when(parent.getName()).thenReturn("master");
        Mockito.when(parent.getParent()).thenReturn(parent);
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("jcr:title","tile");
        objectMap.put("jcr:description","desc");
        objectMap.put("sling:alias","123456");
        String[] tag = {"tag1"};
        objectMap.put(AHMJsonServiceConstants.CQ_TAGS,tag);
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resource.getResourceResolver()).thenReturn(resourceResolver);
        TagManager tagManager = Mockito.mock(TagManager.class);
        Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
        Tag newTag = Mockito.mock(Tag.class);
        Mockito.when(tagManager.resolve("tag1")).thenReturn(newTag);
        Set<Map.Entry<String, Object>> set = new HashSet<>();
        Iterator<Map.Entry<String, Object>> iterator = objectMap.entrySet().iterator();
        while (iterator.hasNext()){
            set.add(iterator.next());
        }
        Mockito.when(values.entrySet()).thenReturn(set);
        baseModel.Initialize();
        Assert.assertEquals("desc",baseModel.getDescription());
        Assert.assertEquals("123456",baseModel.getId());
        Assert.assertEquals("master",baseModel.getName());
        Assert.assertEquals("tile",baseModel.getTitle());
        Assert.assertEquals(1,baseModel.getTag().length);
    }

    @Test
    public void test_empty_values() throws Exception{
        Resource parent = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parent);
        Mockito.when(parent.getName()).thenReturn("sample");
        Mockito.when(parent.getParent()).thenReturn(parent);
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        Map<String,Object> objectMap = new HashMap<>();
        String[] tag = {};
        objectMap.put(AHMJsonServiceConstants.CQ_TAGS,tag);
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resource.getResourceResolver()).thenReturn(resourceResolver);
        TagManager tagManager = Mockito.mock(TagManager.class);
        Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
        Tag newTag = Mockito.mock(Tag.class);
        Mockito.when(tagManager.resolve("tag1")).thenReturn(newTag);
        Set<Map.Entry<String, Object>> set = new HashSet<>();
        Iterator<Map.Entry<String, Object>> iterator = objectMap.entrySet().iterator();
        while (iterator.hasNext()){
            set.add(iterator.next());
        }
        Mockito.when(values.entrySet()).thenReturn(set);
        baseModel.Initialize();
        Assert.assertNull(baseModel.getDescription());
        Assert.assertNull(baseModel.getId());
        Assert.assertEquals("sample",baseModel.getName());
        Assert.assertNull(baseModel.getTitle());
        Assert.assertEquals(0,baseModel.getTag().length);
    }
}
