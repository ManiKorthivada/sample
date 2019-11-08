package ahm.content.service.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;

public class RevealCardComponentTest {


    private RevealCardComponent revealCardComponent = new RevealCardComponent();

    Resource resource;

    @Before
    public void setup() throws Exception {
        resource = Mockito.mock(Resource.class);
        Field builderField = RevealCardComponent.class.getDeclaredField("resource");
        builderField.setAccessible(true);
        builderField.set(revealCardComponent, resource);
        Field builderField1 = RevealCardComponent.class.getDeclaredField("cardTitle");
        builderField1.setAccessible(true);
        builderField1.set(revealCardComponent, "Title");
        Field builderField2 = RevealCardComponent.class.getDeclaredField("cardMessage");
        builderField2.setAccessible(true);
        builderField2.set(revealCardComponent, "Message");
        Field builderField3 = RevealCardComponent.class.getDeclaredField("sortOrder");
        builderField3.setAccessible(true);
        builderField3.set(revealCardComponent, 2);
    }
    @Test
    public void test() throws Exception{
        Assert.assertEquals("Title",revealCardComponent.getCardTitle());
        Assert.assertEquals("Message",revealCardComponent.getCardMessage());
        Assert.assertEquals(2,revealCardComponent.getSortOrder());
    }
}
