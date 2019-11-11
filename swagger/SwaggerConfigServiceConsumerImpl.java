package ahm.content.service.core.services.impl;

import ahm.content.service.core.services.SwaggerConfigServiceConsumer;
import ahm.content.service.core.services.SwaggerJsonService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, service = SwaggerConfigServiceConsumer.class, enabled = true)
public class SwaggerConfigServiceConsumerImpl implements SwaggerConfigServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerConfigServiceConsumer.class);
    private List configurationList;

    @Reference(name = "configurationFactory", cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected synchronized void bindConfigurationFactory(final SwaggerJsonService config) {
        if (configurationList == null) {
            configurationList = new ArrayList();
        }
        configurationList.add(config);
    }

    protected synchronized void unbindConfigurationFactory(final SwaggerJsonService config) {
        LOGGER.info("unbindConfigurationFactory: {}", config.getApiName());
        configurationList.remove(config);
    }

    public void setConfigurationList(List configurationList) {
        this.configurationList = configurationList;
    }

    @Override
    public List getConfigurationList() {
        return configurationList;
    }
}
