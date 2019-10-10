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

import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BaseModel.class})
public class BaseModelTest {

    @InjectMocks
    private BaseModel baseModel;

    Resource resource;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        resource = Mockito.mock(Resource.class);
        MemberModifier.field(BaseModel.class,"resource").set(baseModel,resource);
    }

    @Test
    public void test() throws Exception{
        Resource parent = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parent);
        Mockito.when(parent.getName()).thenReturn("parentName");
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("jcr:title","tile");
        objectMap.put("jcr:description","desc");
        objectMap.put("sling:alias","123456");
        Set<Map.Entry<String, Object>> set = new HashSet<>();
        Iterator<Map.Entry<String, Object>> iterator = objectMap.entrySet().iterator();
        while (iterator.hasNext()){
            set.add(iterator.next());
        }
        Mockito.when(values.entrySet()).thenReturn(set);
        baseModel.Initialize();
        Assert.assertEquals("desc",baseModel.getDescription());
        Assert.assertEquals("123456",baseModel.getId());
        Assert.assertEquals("parentName",baseModel.getName());
        Assert.assertEquals("tile",baseModel.getTitle());
        Assert.assertEquals(0,baseModel.getTag().length);
    }
}
