package z_project3.Config;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Social Media API")
                        .version("1.0")
                        .description("Bot + Post + Comment + Redis Virality System"));
    }
}
 //for swagger(api and third party testing)
//http://localhost:8080/swagger-ui/index.html