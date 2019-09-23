package ahm.content.service.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import ahm.content.service.core.constants.AHMJsonServiceConstants;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
/**
 * This is the Sling Model Class for dgtl-content/components/structure/videoxfpagewidget
 * This also has Jackson Sling Model Exporter and implements ComponentExporter interface
 * 
 * @author Active Health Management (Aetna Digital)
 */
@Model(
		adaptables = {Resource.class},
        adapters = ComponentExporter.class,
        resourceType = {AHMJsonServiceConstants.VIDEOXF_RT},
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
@JsonRootName(value = "Video")

public class VideoModel extends BaseModel implements ComponentExporter {

    @Override
    public String getWidgetType() {
        return "Video";
    }
    
    @JsonProperty("ContentDetails")
    private VideoComponent videoComponent;

    @PostConstruct
    protected void invokepost() {

        super.Initialize();
        Resource videoModelResource = resource.getChild("root/responsivegrid/video");
        
        if (videoModelResource != null) {
        	videoComponent = videoModelResource.adaptTo(VideoComponent.class);
        }
    }

    @Override
    @JsonIgnore
    public String getExportedType() {
        return null;
    }
}