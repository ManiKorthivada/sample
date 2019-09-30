package ahm.content.service.core.models;

import ahm.content.service.core.constants.AHMJsonServiceConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.Map.Entry;

public class BaseModel {

    @SlingObject
    protected Resource resource;

    @JsonProperty("Id")
    private String id;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Tags")
    private String[] tag;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("ContentType")
    private String widgetType;

    public String getWidgetType() {
        return widgetType;
    }

    protected void Initialize() {

        Resource parent = resource.getParent();
        name = parent.getName();
        tag = new String[0];

        if (name.equalsIgnoreCase("master")) {
            name = parent.getParent().getName();
        }

        ValueMap values = resource.getValueMap();

        for (Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equals(AHMJsonServiceConstants.JCR_TITLE)) {
                title = (String) value;
            } else if (key.equals(AHMJsonServiceConstants.JCR_DESC)) {
                description = (String) value;
            } else if (key.equals(AHMJsonServiceConstants.CQ_TAGS)) {
                tag = (String[]) value;
            }

            if (key.equals(AHMJsonServiceConstants.SLING_ALIAS)) {
                id = (String) value;
            }
        }
    }
}