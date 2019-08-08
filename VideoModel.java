package ahm.content.service.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;

import org.apache.sling.models.annotations.via.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;


@Model(
        adaptables = {Resource.class},
        adapters = ComponentExporter.class,
        resourceType = {"/apps/dgtl-content/components/structure/videoxfpagewidget"},
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
    private static Logger log = LoggerFactory.getLogger(VideoModel.class);

    @Override
    public String getWidgetType() {
        return "Video";
    }

    @Inject
    @Via(value = "root/responsivegrid/video", type = ChildResource.class)
    @JsonProperty("VideoType")
    private String videotype;

    @Inject
    @Via(value = "root/responsivegrid/video", type = ChildResource.class)
    @JsonProperty("ExternalVideoUrl")
    private String videoPathExternal;

    @Inject
    @Via(value = "root/responsivegrid/video", type = ChildResource.class)
    @JsonProperty("VideoPath")
    private String videoPathUpload;

    @Inject
    @Via(value = "root/responsivegrid/video", type = ChildResource.class)
    @JsonProperty("VideoId")
    private String videoid;

    @PostConstruct
    protected void invokepost() {
        super.Initialize();
        Resource videoModelResource = resource.getChild("root/responsivegrid/video");

        ModifiableValueMap map1 = videoModelResource.adaptTo(ModifiableValueMap.class);
        for (Map.Entry<String, Object> entry : map1.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();


            if (key.equals("VideoType")) {
                videotype = (String) value;
            } else if (key.equals("VideoPath")) {
                videoPathUpload = (String) value;
            }
        }
    }

    public String getVideotype() {
        return videotype;
    }

    public String getVideoPathExternal() {
        return videoPathExternal;
    }

    public String getVideoPathUpload() {
        return videoPathUpload;
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
