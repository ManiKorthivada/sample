package ahm.content.service.core.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class RecipeComponentTest {


    private RecipeComponent recipeComponent = new RecipeComponent();



    @Before
    public void setup() throws Exception {
        Field builderField = RecipeComponent.class.getDeclaredField("HtmlContent");
        builderField.setAccessible(true);
        builderField.set(recipeComponent, "HTML Content");
        String[] categories = {"category1","category2"};
        Field builderField1 = RecipeComponent.class.getDeclaredField("categories");
        builderField1.setAccessible(true);
        builderField1.set(recipeComponent, categories);
    }

    @Test
    public void test() throws Exception{
        Assert.assertEquals("HTML Content",recipeComponent.getHtmlContent());
        Assert.assertEquals(2,recipeComponent.getCategories().length);
    }
}
