package ahm.content.service.core.services.impl;

import ahm.content.service.core.configurations.SwaggerConfig;
import ahm.content.service.core.services.SwaggerJsonService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = SwaggerJsonService.class, immediate = true)
@Designate(ocd = SwaggerConfig.class, factory = true)
public class SwaggerJsonServiceImpl implements SwaggerJsonService {

    private static final Logger log = LoggerFactory.getLogger(SwaggerJsonService.class);
    private SwaggerConfig config;

    @Activate
    protected void activate(SwaggerConfig config) {
        log.info("In activate");
        this.config = config;
    }

    @Override
    public String getApiName() {
        return config.getApiName();
    }

    @Override
    public String getApiDescription() {
        return config.getApiDescription();
    }

    @Override
    public String[] getApiResponses() {
        return config.apiResponses();
    }


}
