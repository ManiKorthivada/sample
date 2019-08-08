package ahm.content.service.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
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

public class RecipeModel extends BaseModel implements ComponentExporter {
    private static Logger LOG = LoggerFactory.getLogger(RecipeModel.class);

    @Override
    public String getWidgetType() {
        return "Recipe";
    }


    @Inject @Via(value = "root/responsivegrid/recipe",type= ChildResource.class)
    @JsonProperty("Html")
    private String HtmlContent;

    @Inject @Via(value = "root/responsivegrid/recipe",type= ChildResource.class)
    @JsonProperty("Categories")
    private String[] categories;


    @PostConstruct
    protected void invokepost() {
        super.Initialize();
    }


    @Override
    @JsonIgnore
    public String getExportedType() {
        return null;
    }
}
