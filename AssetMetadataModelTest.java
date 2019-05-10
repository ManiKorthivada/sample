package com.aetna.ahm.core.models.qr;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AssetMetadataModel.class)
public class AssetMetadataModelTest {

    private AssetMetadataModel assetMetadataModel;

    @Test
    public void test() throws Exception{
        assetMetadataModel = Mockito.spy(new AssetMetadataModel());
        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
        MemberModifier.field(AssetMetadataModel.class, "videopath").set(assetMetadataModel, "videopath");
        MemberModifier.field(AssetMetadataModel.class, "resolver").set(assetMetadataModel, resolver);
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resolver.getResource("videopath/jcr:content/metadata")).thenReturn(resource);
        ValueMap valueMap = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(valueMap);
        Mockito.when(valueMap.get("videoCC")).thenReturn("ccMockVideo");
        assetMetadataModel.init();
        Assert.assertEquals("ccMockVideo",assetMetadataModel.getVideoCCVal());
    }
}
