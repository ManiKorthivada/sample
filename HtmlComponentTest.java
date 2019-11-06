package ahm.content.service.core.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class HtmlComponentTest {

    private HtmlComponent htmlComponent = new HtmlComponent();



    @Before
    public void setup() throws Exception{
        String HtmlContent = "HTML Content";
        Field builderField = HtmlComponent.class.getDeclaredField("HtmlContent");
        builderField.setAccessible(true);
        builderField.set(htmlComponent, HtmlContent);
    }

    @Test
    public void test() throws Exception{
        Assert.assertEquals("HTML Content",htmlComponent.HtmlContent());
    }
}
