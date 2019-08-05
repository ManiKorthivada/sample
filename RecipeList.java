package ahm.content.service.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;


@Model(
        adaptables = {Resource.class},
        adapters = ComponentExporter.class,
        resourceType = {"/apps/dgtl-content/components/structure/recipexfpagewidget"},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
		)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
     
        extensions = ExporterConstants.SLING_MODEL_EXTENSION,
        options = {
                @ExporterOption(name = "SerializationFeature.WRAP_ROOT_VALUE", value = "true")
        	}
		)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonRootName(value = "Recipe")

public class RecipeList extends BaseModel implements ComponentExporter {
	   private static Logger LOG = LoggerFactory.getLogger(VideoModel.class);
    @SlingObject
    Resource resource;
     
    @JsonProperty("Video")
    private RecipeModel recipeModel;
    

    
    @PostConstruct
    protected void invokepost()  {

        recipeModel= new RecipeModel();
         Iterator<Resource> iteratorExp = resource.getChild("root/responsivegrid/").listChildren();
         
         while (iteratorExp.hasNext()) {
             Resource childResource = iteratorExp.next();
             RecipeModel map = childResource.adaptTo(RecipeModel.class);
        }
    }

	@Override
    @JsonIgnore
    public String getExportedType() {
       
        return null;
    }
}
 