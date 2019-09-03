package ahm.content.service.core.beans;

import java.util.List;

public class AssetBean {

    String imagePath;
    List<RenditionBean> renditions;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<RenditionBean> getRenditions() {
        return renditions;
    }

    public void setRenditions(List<RenditionBean> renditions) {
        this.renditions = renditions;
    }
}
