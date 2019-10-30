package ahm.content.service.core.services;

import ahm.content.service.core.constants.AHMJsonServiceConstants;
import ahm.content.service.core.models.RecipeModel;
import ahm.content.service.core.services.impl.WidgetServiceImpl;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WidgetService.class})
public class WidgetServiceImplTest {

    @InjectMocks
    WidgetServiceImpl widgetService;

    private ResourceResolverFactory resolverFactory;
    private QueryBuilder queryBuilder;
    private ModelFactory modelFactory;


    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException {
        resolverFactory = Mockito.mock(ResourceResolverFactory.class);
        queryBuilder = Mockito.mock(QueryBuilder.class);
        modelFactory = Mockito.mock(ModelFactory.class);
        MemberModifier.field(WidgetServiceImpl.class,"resolverFactory").set(widgetService,resolverFactory);
        MemberModifier.field(WidgetServiceImpl.class,"queryBuilder").set(widgetService,queryBuilder);
        MemberModifier.field(WidgetServiceImpl.class,"modelFactory").set(widgetService,modelFactory);
    }

    @Test
    public void test() throws Exception{
        ResourceResolver resourceResolver = Mockito.mock(ResourceResolver.class);
        UUID uuid =UUID.fromString("cb87a8fb-0b78-4e97-b620-b8355d43689b");
        Query query =Mockito.mock(Query.class);
        Mockito.when(queryBuilder.createQuery(Matchers.any(), Matchers.any())).thenReturn(query);
        SearchResult result =Mockito.mock(SearchResult.class);
        Mockito.when(query.getResult()).thenReturn(result);
        Iterator<Resource> resources = Mockito.mock(Iterator.class);
        Mockito.when(result.getResources()).thenReturn(resources);
        Mockito.when(resources.hasNext()).thenReturn(true);
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resources.next()).thenReturn(resource);
        ValueMap resourceValues = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(resourceValues);
        Mockito.when(resourceValues.get(AHMJsonServiceConstants.SLING_RT, String.class)).thenReturn(AHMJsonServiceConstants.XF_RT);
        Resource parentResource = Mockito.mock(Resource.class);
        Mockito.when(resource.getParent()).thenReturn(parentResource);
        Mockito.when(parentResource.getChild("master/jcr:content")).thenReturn(resource);
        Mockito.when(modelFactory.exportModelForResource(resource, ExporterConstants.SLING_MODEL_EXPORTER_NAME, String.class, new HashMap<String, String>())).thenReturn("widget");

        Assert.assertEquals("widget",widgetService.GetWidgetById(uuid,resourceResolver));
    }
}
