package ahm.content.service.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
@PrepareForTest({TagModel.class})
public class TagModelTest {

    @InjectMocks
    private TagModel tagModel;

    ResourceResolver resourceResolver;

    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        resourceResolver = Mockito.mock(ResourceResolver.class);
        MemberModifier.field(TagModel.class,"resourceResolver").set(tagModel,resourceResolver);
        MemberModifier.field(TagModel.class,"item").set(tagModel,"Tags");
    }

    @Test
    public void test() throws Exception{
        TagManager tagManager = Mockito.mock(TagManager.class);
        Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
        Tag newTag = Mockito.mock(Tag.class);
        Mockito.when(tagManager.resolve("Tags")).thenReturn(newTag);
        Mockito.when(newTag.getTitle()).thenReturn("tagTitle");
        tagModel.init();
        Assert.assertEquals("tagTitle",tagModel.getTagTitle());
    }
}
