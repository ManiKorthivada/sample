package ahm.content.service.core.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Configurations for preparing the Swagger JSON")
public @interface SwaggerConfig {

    @AttributeDefinition(name = "API Name", description = "API Name")
    String getApiName() default "Sample API";

    @AttributeDefinition(name = "API Description", description = "API Description")
    String getApiDescription() default "Sample API Desccription";

    @AttributeDefinition(
            name = "API Responses",
            description = "API Responses",
            type = AttributeType.STRING
    )
    String[] apiResponses() default {"200", "400","404","500"};
}
