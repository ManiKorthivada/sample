package ahm.content.service.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TagModelTest {

    private TagModel tagModel;

    ResourceResolver resourceResolver;

    @Before
    public void setup() throws Exception {
        resourceResolver = Mockito.mock(ResourceResolver.class);
        tagModel = new TagModel();
        PrivateAccessor.setField(tagModel, "resourceResolver", resourceResolver);
        PrivateAccessor.setField(tagModel, "item", "Tags");
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
