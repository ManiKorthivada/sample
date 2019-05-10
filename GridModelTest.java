package com.aetna.ahm.core.models.qr;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;


@RunWith(PowerMockRunner.class)
@PrepareForTest(GridModel.class)
public class GridModelTest {


    @InjectMocks
    GridModel gridModel;

    @Mock
    SlingHttpServletRequest request;

    @Test
    public void test() throws Exception{
        Resource resource =  Mockito.mock(Resource.class);
        Mockito.when(request.getResource()).thenReturn(resource);
        Iterator<Resource> iterator = Mockito.mock(Iterator.class);
        Mockito.when(resource.listChildren()).thenReturn(iterator);
        gridModel.getNoOfComponents();
    }
}
