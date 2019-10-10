package ahm.content.service.core.models;

import com.day.cq.dam.api.Asset;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
@PrepareForTest({AssetMetadataModel.class})
public class AssetMetadataModelTest {

    @InjectMocks
    private AssetMetadataModel assetMetadataModel;

    ResourceResolver resolver;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        resolver = Mockito.mock(ResourceResolver.class);
        MemberModifier.field(AssetMetadataModel.class,"title").set(assetMetadataModel,"title");
        MemberModifier.field(AssetMetadataModel.class,"videopath").set(assetMetadataModel,"videopath");
        MemberModifier.field(AssetMetadataModel.class,"resolver").set(assetMetadataModel,resolver);
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
