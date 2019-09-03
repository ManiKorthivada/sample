package ahm.content.service.core.models;

import ahm.content.service.core.beans.AssetBean;
import ahm.content.service.core.beans.RenditionBean;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(
        adaptables = {Resource.class},
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
@JsonRootName(value = "asset")

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
            videoUrl = values.get("videoPathExternal", String.class);
        } else {
            videoUrl = values.get("videoPathUpload", String.class);
            Resource videoResource = resource.getResourceResolver().getResource(videoUrl + "/jcr:content/metadata");
            if (null != videoResource) {
                videoCC = videoResource.getValueMap().get("videoCC", String.class);
            }
        }
    }

    private List<RenditionBean> getRenditions() {
        Resource videoResource = resource.getResourceResolver().getResource(videoUrl);
        if (null != videoResource) {
            Asset asset = videoResource.adaptTo(Asset.class);
            List<RenditionBean> renditionBeanList = new ArrayList<>();
            List<Rendition> renditions = asset.getRenditions();
            Iterator<Rendition> renditionIterator = renditions.iterator();
            while (renditionIterator.hasNext()) {
                Rendition rendition = renditionIterator.next();
                RenditionBean renditionBean = new RenditionBean();
                renditionBean.setPath(rendition.getPath());
                renditionBean.setFormat(rendition.getMimeType());
                renditionBean.setSize(rendition.getSize());
                renditionBeanList.add(renditionBean);
            }
            return renditionBeanList;
        }
        return null;
    }

    public AssetBean getAsset() {
        AssetBean assetBean = new AssetBean();
        Resource videoResource = resource.getResourceResolver().getResource(videoUrl);
        if (null != videoResource) {
            Asset asset = videoResource.adaptTo(Asset.class);
            assetBean.setImagePath(asset.getOriginal().getPath());
            assetBean.setRenditions(getRenditions());
        }
        return assetBean;
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