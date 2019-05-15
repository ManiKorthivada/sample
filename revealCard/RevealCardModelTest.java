package com.aetna.ahm.core.models.qr;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RevealCardModel.class)
public class RevealCardModelTest {

    private RevealCardModel revealCardModel;

    @Test
    public void test() throws Exception {
        revealCardModel = Mockito.spy(new RevealCardModel());
        Resource resource = Mockito.mock(Resource.class);
        MemberModifier.field(RevealCardModel.class, "resource").set(revealCardModel, resource);
        MemberModifier.field(RevealCardModel.class, "cardImagePath").set(revealCardModel, "imagePath");
        MemberModifier.field(RevealCardModel.class, "cardTitle").set(revealCardModel, "cardTitle");
        MemberModifier.field(RevealCardModel.class, "cardMessage").set(revealCardModel, "cardMessage");
        Resource parentResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parentResource);
        Iterator<Resource> iterator = Mockito.mock(Iterator.class);
        Mockito.when(parentResource.listChildren()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        Resource childResource = Mockito.mock(Resource.class);
        Mockito.when(iterator.next()).thenReturn(childResource);
        Mockito.when(childResource.getName()).thenReturn("name");
        Mockito.when(resource.getName()).thenReturn("name");
        ModifiableValueMap map = Mockito.mock(ModifiableValueMap.class);
        Mockito.when(childResource.adaptTo(ModifiableValueMap.class)).thenReturn(map);
        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(childResource.getResourceResolver()).thenReturn(resolver);
        Mockito.when(resource.getResourceResolver()).thenReturn(resolver);
        Resource imageResource = Mockito.mock(Resource.class);
        Mockito.when(resolver.getResource("imagePath")).thenReturn(imageResource);
        Mockito.when(imageResource.getName()).thenReturn("imageName");
        revealCardModel.invokepost();
        Assert.assertEquals("imageName", revealCardModel.getImageName());
        Assert.assertEquals("imagePath", revealCardModel.getCardImagePath());
        Assert.assertEquals("cardTitle", revealCardModel.getCardTitle());
        Assert.assertEquals("cardMessage", revealCardModel.getCardMessage());
    }
}
