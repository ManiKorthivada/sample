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

@RunWith(PowerMockRunner.class)
@PrepareForTest({RevealCardComponent.class})
public class RevealCardComponentTest {

    @InjectMocks
    private RevealCardComponent revealCardComponent;

    Resource resource;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        resource = Mockito.mock(Resource.class);
        MemberModifier.field(RevealCardComponent.class,"resource").set(revealCardComponent,resource);
        MemberModifier.field(RevealCardComponent.class,"cardTitle").set(revealCardComponent,"Title");
        MemberModifier.field(RevealCardComponent.class,"cardMessage").set(revealCardComponent,"Message");
        MemberModifier.field(RevealCardComponent.class,"sortOrder").set(revealCardComponent,2);
    }
    @Test
    public void test() throws Exception{
        Assert.assertEquals("Title",revealCardComponent.getCardTitle());
        Assert.assertEquals("Message",revealCardComponent.getCardMessage());
        Assert.assertEquals(2,revealCardComponent.getSortOrder());
    }
}
