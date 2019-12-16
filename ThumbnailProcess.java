package ahm.content.service.core.workflows;


import com.adobe.cq.gfx.Gfx;
import com.adobe.cq.gfx.Instructions;
import com.adobe.cq.gfx.Layer;
import com.adobe.cq.gfx.Plan;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.api.renditions.RenditionMaker;
import com.day.cq.dam.api.renditions.RenditionTemplate;
import com.day.cq.dam.api.thumbnail.ThumbnailConfig;
import com.day.cq.dam.commons.process.AbstractAssetWorkflowProcess;
import com.day.cq.dam.commons.util.AssetUpdateMonitor;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.dam.commons.util.OrientationUtil;
import com.day.cq.dam.core.process.CreateThumbnailProcess;
import com.day.cq.dam.core.process.CreateThumbnailProcess.Config;
import com.day.cq.dam.core.process.CreateWebEnabledImageProcess;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                Constants.SERVICE_DESCRIPTION
                        + "=Asset New Workflow",
                "process.label"
                        + "=Asset New Workflow"
        }
)
public class ThumbnailProcess extends AbstractAssetWorkflowProcess {
    private static final Logger log = LoggerFactory.getLogger(ahm.content.service.core.workflows.ThumbnailProcess.class);
    @Reference
    private RenditionMaker renditionMaker;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    private AssetUpdateMonitor monitor;

    @Reference
    private MimeTypeService mimeTypeService;

    private CreateThumbnailProcess thumbnailCreator = new CreateThumbnailProcess();
    private CreateWebEnabledImageProcess webEnabledImageCreator = new CreateWebEnabledImageProcess();

    @Reference
    private Gfx gfx;

    public ThumbnailProcess() {
    }

    public void execute(final WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaData) throws WorkflowException {
        Asset asset = null;
        try {
            String wfPayload = workItem.getWorkflowData().getPayload().toString();
            ResourceResolver resourceResolver = this.getResourceResolver(workflowSession.getSession());
            Resource resource = null;
            if (null != resourceResolver) {
                resource = resourceResolver.getResource(wfPayload);
            }
            if (null != resource) {
                asset = DamUtil.resolveToAsset(resource);
            }
            if (asset != null) {
                try {
                    Node assetNode = (Node) asset.adaptTo(Node.class);
                    Node contentNode = assetNode.getNode("jcr:content");
                    String scene7File = asset.getMetadata("dam:scene7File") != null ? asset.getMetadata("dam:scene7File").toString() : "";
                    boolean isScene7Video = DamUtil.isVideo(asset) && !StringUtils.isEmpty(scene7File);
                    if (isScene7Video) {
                        log.info("Skip to create static thumbnails/webImage for a scene7 processed video.");
                    } else {
                        if (!contentNode.hasProperty("dam:manualThumbnail") || !contentNode.getProperty("dam:manualThumbnail").getBoolean()) {
                            Config createThumbnailConfig = this.thumbnailCreator.parseConfig(metaData);
                            createThumbnails(asset, createThumbnailConfig, this.renditionMaker, workItem);
                        }

                        com.day.cq.dam.core.process.CreateWebEnabledImageProcess.Config createWebEnabledImageConfig = this.webEnabledImageCreator.parseConfig(metaData);

                        try {
                            this.createWebEnabledImage(workItem, createWebEnabledImageConfig, asset, this.renditionMaker);
                        } catch (RepositoryException var18) {
                            throw new WorkflowException(var18);
                        }
                    }
                } catch (RepositoryException var19) {
                    throw new WorkflowException(var19);
                }
            }
        } finally {
            //update.done();
        }

    }

    public void createWebEnabledImage(WorkItem workItem, CreateWebEnabledImageProcess.Config config, Asset asset, RenditionMaker renditionMaker) throws RepositoryException {
        if (this.handleAsset1(asset, config)) {
            asset.setBatchMode(true);
            if (this.isWebThumbnailStale(asset, config.width, config.height)) {
                RenditionTemplate template = renditionMaker.createWebRenditionTemplate(asset, config.width, config.height, config.quality, config.mimeType, config.mimeTypesToKeep);
                renditionMaker.generateRenditions(asset, new RenditionTemplate[]{template});
            }
        }

        Node assetNode = (Node)asset.adaptTo(Node.class);
        Node content = assetNode.getNode("jcr:content");
        String resolvedUser = (String)workItem.getWorkflowData().getMetaDataMap().get("userId", String.class);
        Rendition rendition = asset.getRendition("original");
        if (rendition != null) {
            String lastModified = (String)rendition.getProperties().get("jcr:lastModifiedBy");
            if (StringUtils.isNotBlank(lastModified)) {
                resolvedUser = lastModified;
            }
        }

        content.setProperty("jcr:lastModifiedBy", resolvedUser);
        content.setProperty("jcr:lastModified", Calendar.getInstance());
    }

    protected boolean handleAsset1(Asset asset, CreateWebEnabledImageProcess.Config config) {
        if (asset != null && config.skipMimeTypes != null) {
            String mimeType = asset.getMimeType();
            if (mimeType == null) {
                return true;
            } else {
                String[] var4 = config.skipMimeTypes;
                int var5 = var4.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String val = var4[var6];
                    if (mimeType.matches(val)) {
                        log.debug(this.getClass().getName() + " skipped for MIME type: " + mimeType);
                        return false;
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    private static boolean isWebThumbnailStale(Asset asset, int width, int height) {
        String pngName = "ahm.web." + String.valueOf(width) + "." + height + ".png";
        String jpegName = "ahm.web." + String.valueOf(width) + "." + height + ".jpeg";
        return isThumbnailStale(asset, pngName) && isThumbnailStale(asset, jpegName);
    }

    private static boolean isNotNull(Rendition rendition, Rendition rendition2) {
        return rendition != null && rendition.getResourceMetadata() != null && rendition2 != null && rendition2.getResourceMetadata() != null;
    }

    static boolean isThumbnailStale(Asset asset, String thumb) {
        Rendition rendition = asset.getRendition(thumb);
        Rendition original = asset.getOriginal();
        return !isNotNull(rendition, original) || rendition.getResourceMetadata().getModificationTime() <= original.getResourceMetadata().getModificationTime();
    }

    public void createThumbnails(Asset asset, CreateThumbnailProcess.Config config, RenditionMaker renditionMaker, WorkItem workItem) {
        if (this.handleAsset(asset, config)) {
            asset.setBatchMode(true);
            RenditionTemplate[] templates = createRenditionTemplates(asset, config.thumbnails, renditionMaker, workItem);
            renditionMaker.generateRenditions(asset, templates);
        }
    }

    public RenditionTemplate[] createRenditionTemplates(Asset asset, ThumbnailConfig[] thumbnails, RenditionMaker renditionMaker, WorkItem workItem) {
        ArrayList<RenditionTemplate> list = new ArrayList();

        for (int i = 0; i < thumbnails.length; ++i) {
            ThumbnailConfig thumb = thumbnails[i];


            RenditionTemplate customTemplate = createThumbnailTemplate(asset, thumb.getWidth(), thumb.getHeight(), thumb.doCenter(),workItem);
            list.add(customTemplate);
        }

        return list.toArray(new ahm.content.service.core.workflows.ThumbnailProcess.CustomTemplate[list.size()]);
    }
    private RenditionTemplate createThumbnailTemplate(Asset asset, int width, int height, boolean doCenter,WorkItem workItem) {
        ahm.content.service.core.workflows.ThumbnailProcess.CustomTemplate template = new ahm.content.service.core.workflows.ThumbnailProcess.CustomTemplate();

        final Rendition rendition = asset.getOriginal();

        String workflowTitle = workItem.getWorkflow().getWorkflowModel().getTitle();
        String ext = getExtension(asset.getMimeType());

        template.renditionName = getRenditionName(width, height, workflowTitle, ext);
        template.mimeType = asset.getMimeType();
        template.plan = gfx.createPlan();

        template.plan.layer(0).set("src", rendition.getPath());

        applyOrientation(OrientationUtil.getOrientation(asset), template.plan.layer(0));

        applyThumbnail(width, height, doCenter, template.mimeType, template.plan);

        return template;
    }

    private static void applyThumbnail(int width, int height, boolean doCenter, String mimeType, Plan plan) {
        Instructions global = plan.view();

        global.set("wid", width);
        global.set("hei", height);
        global.set("rszfast", Boolean.FALSE);

        global.set("fit", doCenter ? "fit,1" : "constrain,1");

        String fmt = org.apache.commons.lang3.StringUtils.substringAfter(mimeType, "/");

        if ("png".equals(fmt) || "gif".equals(fmt) || "tif".equals(fmt)) {
            fmt = fmt + "-alpha";
        }

        global.set("fmt", fmt);
    }

    private static void applyOrientation(short exifOrientation, Layer layer) {
        switch (exifOrientation) {
            case OrientationUtil.ORIENTATION_MIRROR_HORIZONTAL:
                layer.set("flip", "lr");
                break;
            case OrientationUtil.ORIENTATION_ROTATE_180:
                layer.set("rotate", 180);
                break;
            case OrientationUtil.ORIENTATION_MIRROR_VERTICAL:
                layer.set("flip", "ud");
                break;
            case OrientationUtil.ORIENTATION_MIRROR_HORIZONTAL_ROTATE_270_CW:
                layer.set("flip", "lr");
                layer.set("rotate", 270);
                break;
            case OrientationUtil.ORIENTATION_ROTATE_90_CW:
                layer.set("rotate", 90);
                break;
            case OrientationUtil.ORIENTATION_MIRROR_HORIZONTAL_ROTATE_90_CW:
                layer.set("flip", "lr");
                layer.set("rotate", 90);
                break;
            case OrientationUtil.ORIENTATION_ROTATE_270_CW:
                layer.set("rotate", 270);
                break;
            default:
                break;
        }
    }

    protected class CustomTemplate implements RenditionTemplate {
        Plan plan;
        String renditionName;
        String mimeType;

        public Rendition apply(Asset asset) {
            InputStream stream = null;
            try {
                stream = gfx.render(plan, asset.adaptTo(Resource.class).getResourceResolver());
                if (stream != null) {
                    return asset.addRendition(renditionName, stream, mimeType);
                }
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    log.error("IOException in generateJpegRenditions class");
                }
            }
            return null;
        }
    }

    public String getExtension(String mimetype) {
        return mimeTypeService.getExtension(mimetype);
    }

    protected static String getRenditionName(int maxWidth, int maxHeight, String workflowTitle, String ext) {
        String renditionName = "ahm.web." + maxWidth + "." + maxHeight + "." + ext;
        return renditionName;
    }

    protected boolean handleAsset(Asset asset, CreateThumbnailProcess.Config config) {
        if (asset != null && config.skipMimeTypes != null) {
            String mimeType = asset.getMimeType();
            if (mimeType == null) {
                return true;
            } else {
                String[] var4 = config.skipMimeTypes;
                int var5 = var4.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    String val = var4[var6];
                    if (mimeType.matches(val)) {
                        log.debug(this.getClass().getName() + " skipped for MIME type: " + mimeType);
                        return false;
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    protected void bindRenditionMaker(RenditionMaker var1) {
        this.renditionMaker = var1;
    }

    protected void unbindRenditionMaker(RenditionMaker var1) {
        if (this.renditionMaker == var1) {
            this.renditionMaker = null;
        }

    }

    protected void bindMonitor(AssetUpdateMonitor var1) {
        this.monitor = var1;
    }

    protected void unbindMonitor(AssetUpdateMonitor var1) {
        if (this.monitor == var1) {
            this.monitor = null;
        }

    }

    /**
     * Method to create the resourceResolver object using workflowSession
     *
     * @param session
     */
    protected ResourceResolver getResourceResolver(Session session) {
        try {
            return resourceResolverFactory.getResourceResolver(
                    Collections.<String, Object>singletonMap(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, session));
        } catch (LoginException e) {
            log.error("Error while getting resource resolver ", e);
        }
        return null;
    }
}
