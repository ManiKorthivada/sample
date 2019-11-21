package ahm.content.service.core.models;

import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class RecipeModelTest {


    private RecipeModel recipeModel = new RecipeModel();

    Resource resource;

    @Before
    public void setup() throws Exception {
        resource = Mockito.mock(Resource.class);
        PrivateAccessor.setField(recipeModel, "resource", resource);
    }

    @Test
    public void test() throws Exception {
        Resource parent = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parent);
        Mockito.when(parent.getName()).thenReturn("parentName");
        Resource videoModelResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getChild("root/responsivegrid/recipe")).thenReturn(videoModelResource);
        VideoComponent videoComponent = Mockito.mock(VideoComponent.class);
        Mockito.when(videoModelResource.adaptTo(VideoComponent.class)).thenReturn(videoComponent);
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        recipeModel.invokepost();
        Assert.assertEquals("parentName", recipeModel.getName());
    }

    @Test
    public void test_resource_null() throws Exception {
        Resource parent = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parent);
        Mockito.when(parent.getName()).thenReturn("");
        Resource videoModelResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getChild("root/responsivegrid/recipe")).thenReturn(null);
        ValueMap values = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(values);
        recipeModel.invokepost();
        Assert.assertEquals("", recipeModel.getName());
    }
}
