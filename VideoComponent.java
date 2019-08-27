package ahm.content.service.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(
		adaptables = {Resource.class },
		adapters = ComponentExporter.class,
		resourceType = "/apps/dgtl-content/components/content/video",
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
@JsonRootName(value = "video")

public class VideoComponent implements ComponentExporter {

	@JsonProperty("VideoType")
	private String videoType;

	@JsonProperty("VideoUrl")
	private String videoUrl;

	@JsonProperty("VideoCC")
	private String videoCC;

	@SlingObject
	private Resource resource;

	@PostConstruct
	protected void invokepost() {

		ValueMap values = resource.getValueMap();
		videoType = values.get("videotype", String.class);

		if (videoType.equalsIgnoreCase("external")) {
			videoUrl = values.get("videoPathExternal",String.class);
		} else {
			videoUrl = values.get("videoPathUpload",String.class);
			Resource videoResource = resource.getResourceResolver().getResource(videoUrl+"/jcr:content/metadata");
			if(null!=videoResource){
				videoCC = videoResource.getValueMap().get("videoCC",String.class);
			}
		}
	}

	public String getVideoType() {
		return videoType;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	@Override
	@JsonIgnore
	public String getExportedType() {
		return null;
	}
}