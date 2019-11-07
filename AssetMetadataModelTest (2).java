package ahm.content.service.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class AssetMetadataModelTest {


    private AssetMetadataModel assetMetadataModel = new AssetMetadataModel();

    ResourceResolver resolver;

    @Before
    public void setup() throws Exception {
        resolver = Mockito.mock(ResourceResolver.class);
        Field builderField = AssetMetadataModel.class.getDeclaredField("resolver");
        builderField.setAccessible(true);
        builderField.set(assetMetadataModel, resolver);
        Field builderField1 = AssetMetadataModel.class.getDeclaredField("videopath");
        builderField1.setAccessible(true);
        builderField1.set(assetMetadataModel, "videopath");
        Field builderField2 = AssetMetadataModel.class.getDeclaredField("title");
        builderField2.setAccessible(true);
        builderField2.set(assetMetadataModel, "title");
    }

    @Test
    public void test() throws Exception{
        Resource imageResource = Mockito.mock(Resource.class);
        Mockito.when(resolver.getResource("videopath")).thenReturn(imageResource);
        Mockito.when(imageResource.getName()).thenReturn("name");
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resolver.getResource("videopath/jcr:content/metadata")).thenReturn(resource);
        ValueMap valueMap = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(valueMap);
        Mockito.when(valueMap.get("dclosedcaption")).thenReturn("ccVideo");
        assetMetadataModel.init();
        Assert.assertEquals("name",assetMetadataModel.getTitle());
        Assert.assertEquals("ccVideo",assetMetadataModel.getVideoCCVal());
    }
}
