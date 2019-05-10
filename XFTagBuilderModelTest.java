package com.aetna.ahm.core.models.qr;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(XFTagBuilderModel.class)
public class XFTagBuilderModelTest {

    private XFTagBuilderModel xfTagBuilderModel;

    @Test
    public void test() throws Exception{
        xfTagBuilderModel = Mockito.spy(new XFTagBuilderModel());
        Page currentPage = Mockito.mock(Page.class);
        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
        String[] tags = {"tag1","tag2"};
        MemberModifier.field(XFTagBuilderModel.class, "currentPage").set(xfTagBuilderModel, currentPage);
        MemberModifier.field(XFTagBuilderModel.class, "resolver").set(xfTagBuilderModel, resolver);
        MemberModifier.field(XFTagBuilderModel.class, "tags").set(xfTagBuilderModel, tags);
        xfTagBuilderModel.init();
        Assert.assertEquals(2,xfTagBuilderModel.getTagList().size());
    }
}
