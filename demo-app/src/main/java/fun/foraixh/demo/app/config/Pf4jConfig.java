package fun.foraixh.demo.app.config;

import fun.foraixh.demo.app.extensions.PluginRequestMappingManager;
import fun.foraixh.demo.app.extensions.SpringPluginManager;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @Author foraixh
 * @Date 2022-07-24 18:25:00
 * @Version 1.0
 * @Usage usage
 */
@Configuration
@Slf4j
@EnableWebMvc
public class Pf4jConfig {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public Pf4jConfig(
        RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Bean
    public PluginRequestMappingManager pluginRequestMappingManager() {
        return new PluginRequestMappingManager(requestMappingHandlerMapping);
    }

    @Primary
    @Bean
    public PluginManager springPluginManager() {
        PluginManager pluginManager = new SpringPluginManager(
            Path.of("D:\\0-opensource-projects\\pf4j-demo\\demo-app\\src\\main\\resources\\plugins"));
        return pluginManager;
    }

    @Bean
    public PluginManager pluginManager() {
        PluginManager pluginManager = new DefaultPluginManager(
            Path.of("D:\\0-opensource-projects\\pf4j-demo\\demo-app\\src\\main\\resources\\plugins"));
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        return pluginManager;
    }

}
