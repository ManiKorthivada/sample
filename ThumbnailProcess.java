package ahm.content.service.core.workflows;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.adobe.cq.gfx.Gfx;
import com.adobe.cq.gfx.Plan;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.api.renditions.RenditionMaker;
import com.day.cq.dam.api.renditions.RenditionTemplate;
import com.day.cq.dam.api.thumbnail.ThumbnailConfig;
import com.day.cq.dam.commons.process.AbstractAssetWorkflowProcess;
import com.day.cq.dam.commons.util.AssetUpdate;
import com.day.cq.dam.commons.util.AssetUpdate.Check;
import com.day.cq.dam.commons.util.AssetUpdateMonitor;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.dam.core.process.CreateThumbnailProcess;
import com.day.cq.dam.core.process.CreateThumbnailProcess.Config;
import com.day.cq.dam.core.process.CreateWebEnabledImageProcess;
import com.day.cq.dam.core.process.UpdateFolderThumbnailProcess;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
    private AssetUpdateMonitor monitor;
    private CreateThumbnailProcess thumbnailCreator = new CreateThumbnailProcess();
    private CreateWebEnabledImageProcess webEnabledImageCreator = new CreateWebEnabledImageProcess();
    private UpdateFolderThumbnailProcess folderThumbnailUpdater = new UpdateFolderThumbnailProcess();
    private static final String DAM_SCENE7FILE = "dam:scene7File";

    @Reference
    private Gfx gfx;

    public ThumbnailProcess() {
    }

    public void execute(final WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaData) throws WorkflowException {
        Asset asset = null;
        try {
            String wfPayload = workItem.getWorkflowData().getPayload().toString();
            Resource resource = this.getResourceResolver(workflowSession.getSession()).getResource(wfPayload);
            if (null != resource) {
                asset = DamUtil.resolveToAsset(resource);
            }
            if (asset != null) {
                try {
                    Node assetNode = (Node)asset.adaptTo(Node.class);
                    Node contentNode = assetNode.getNode("jcr:content");
                    String scene7File = asset.getMetadata("dam:scene7File") != null ? asset.getMetadata("dam:scene7File").toString() : "";
                    boolean isScene7Video = DamUtil.isVideo(asset) && !StringUtils.isEmpty(scene7File);
                    if (isScene7Video) {
                        log.info("Skip to create static thumbnails/webImage for a scene7 processed video.");
                    } else {
                        if (!contentNode.hasProperty("dam:manualThumbnail") || !contentNode.getProperty("dam:manualThumbnail").getBoolean()) {
                            Config createThumbnailConfig = this.thumbnailCreator.parseConfig(metaData);
                            createThumbnails(asset, createThumbnailConfig, this.renditionMaker,workItem);
                        }

                        com.day.cq.dam.core.process.CreateWebEnabledImageProcess.Config createWebEnabledImageConfig = this.webEnabledImageCreator.parseConfig(metaData);

                        try {
                            this.webEnabledImageCreator.createWebEnabledImage(workItem, createWebEnabledImageConfig, asset, this.renditionMaker);
                        } catch (RepositoryException var18) {
                            throw new WorkflowException(var18);
                        }
                    }

                    if (!contentNode.hasProperty("dam:manualThumbnail") || !contentNode.getProperty("dam:manualThumbnail").getBoolean()) {
                        try {
                            //this.folderThumbnailUpdater.updateFolderThumbnail(asset, assetNode, this.folderPreviewUpdater);
                        } catch (Exception var17) {
                            //update.error(var17);
                            log.error("Error while updating folder thumbnail of asset ", asset.getPath(), var17);
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

    public void createThumbnails(Asset asset, CreateThumbnailProcess.Config config, RenditionMaker renditionMaker, WorkItem workItem) {
        if (this.handleAsset(asset, config)) {
            asset.setBatchMode(true);
            RenditionTemplate[] templates = createRenditionTemplates(asset, config.thumbnails, renditionMaker,workItem);
            renditionMaker.generateRenditions(asset, templates);
        }
    }

    public RenditionTemplate[] createRenditionTemplates(Asset asset, ThumbnailConfig[] thumbnails, RenditionMaker renditionMaker, WorkItem workItem) {
        ArrayList<RenditionTemplate> list = new ArrayList();

        for(int i = 0; i < thumbnails.length; ++i) {
            ThumbnailConfig thumb = thumbnails[i];
            String workflowTitle = workItem.getWorkflow().getWorkflowModel().getTitle();
            String ext = getExtension(asset.getMimeType());
            String thName = getRenditionName(thumb.getWidth(),thumb.getHeight(),workflowTitle,ext);
            ahm.content.service.core.workflows.ThumbnailProcess.CustomTemplate customTemplate = (ahm.content.service.core.workflows.ThumbnailProcess.CustomTemplate) renditionMaker.createThumbnailTemplate(asset, thumb.getWidth(), thumb.getHeight(), thumb.doCenter());
            customTemplate.renditionName = thName;
            customTemplate.mimeType = asset.getMimeType();
            list.add(customTemplate);
        }

        return list.toArray(new ahm.content.service.core.workflows.ThumbnailProcess.CustomTemplate[list.size()]);
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
        String renditionName = "intel.web." + maxWidth + "." + maxHeight + "." + ext;
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
}
