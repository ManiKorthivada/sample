package ahm.content.service.core.models;

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
@PrepareForTest({HtmlComponent.class})
public class HtmlComponentTest {

    @InjectMocks
    private HtmlComponent htmlComponent;



    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        MemberModifier.field(HtmlComponent.class,"HtmlContent").set(htmlComponent,"HTML Content");
    }

    @Test
    public void test() throws Exception{
        Assert.assertEquals("HTML Content",htmlComponent.HtmlContent());
    }
}
