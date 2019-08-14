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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map.Entry;

@Model(adaptables = {Resource.class }, 
       adapters = ComponentExporter.class, 
       resourceType = "/apps/dgtl-content/components/content/video", 
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION, options = {
		@ExporterOption(name = "SerializationFeature.WRAP_ROOT_VALUE", value = "true") })

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonRootName(value = "video")

public class VideoComponent implements ComponentExporter {
	private static Logger LOG = LoggerFactory.getLogger(VideoComponent.class);

	@JsonProperty("VideoType")
	private String videotype;

	@JsonProperty("VideoPath")
	private String videoPath;



	@Inject
	@JsonProperty("VideoId")
	private String videoid;

	@SlingObject
	private Resource resource;

	@PostConstruct
	protected void invokepost() {

		ValueMap values = resource.getValueMap();

		for (Entry<String, Object> entry : values.entrySet()) {

			String key = entry.getKey();
			Object value = entry.getValue();

			if (key.equals("videotype")) {
				videotype = (String) value;
			}
		}
		if(videotype.equalsIgnoreCase("external")){
			videoPath = values.get("videoPathExternal",String.class);
		}else{
			videoPath = values.get("videoPathUpload",String.class);
		}
	}

	public String getVideotype() {
		return videotype;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public String getVideoid() {
		return videoid;
	}

	@Override
	@JsonIgnore
	public String getExportedType() {
		return null;
	}
}