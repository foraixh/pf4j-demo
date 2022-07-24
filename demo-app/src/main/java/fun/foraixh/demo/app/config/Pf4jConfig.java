package fun.foraixh.demo.app.config;

import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author foraixh
 * @Date 2022-07-24 18:25:00
 * @Version 1.0
 * @Usage usage
 */
@Configuration
@Slf4j
public class Pf4jConfig {
    @Bean
    public PluginManager pluginManager() {
        PluginManager pluginManager = new DefaultPluginManager(
            Path.of("D:\\0-opensource-projects\\pf4j-demo\\demo-app\\src\\main\\resources\\plugins"));
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        return pluginManager;
    }

}
