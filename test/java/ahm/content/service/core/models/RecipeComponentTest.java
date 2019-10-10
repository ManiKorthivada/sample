package ahm.content.service.core.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RecipeComponent.class})
public class RecipeComponentTest {

    @InjectMocks
    private RecipeComponent recipeComponent;



    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        MemberModifier.field(RecipeComponent.class,"HtmlContent").set(recipeComponent,"HTML Content");
        String[] categories = {"category1","category2"};
        MemberModifier.field(RecipeComponent.class,"categories").set(recipeComponent,categories);
    }

    @Test
    public void test() throws Exception{
        Assert.assertEquals("HTML Content",recipeComponent.getHtmlContent());
        Assert.assertEquals(2,recipeComponent.getCategories().length);
    }
}
