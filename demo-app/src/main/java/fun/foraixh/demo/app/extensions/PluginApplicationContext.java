package fun.foraixh.demo.app.extensions;

import fun.foraixh.definition.Greeting;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.StopWatch;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @Author foraixh
 * @Date 2022-07-24 19:08:00
 * @Version 1.0
 * @Usage 各插件的应用上下文
 */
@Slf4j
public class PluginApplicationContext {
    private final Map<String, ApplicationContext> registry = new ConcurrentHashMap<>();

    protected final SpringPluginManager springPluginManager;

    public PluginApplicationContext(SpringPluginManager springPluginManager) {
        this.springPluginManager = springPluginManager;
    }

    public ApplicationContext createApplicationContext(String pluginId) {
        if (registry.containsKey(pluginId)) {
            return registry.get(pluginId);
        }

        PluginWrapper plugin = springPluginManager.getPlugin(pluginId);
        ClassLoader pluginClassLoader = plugin.getPluginClassLoader();

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
            plugin.getDescriptor().getPluginClass()
                .substring(0, plugin.getDescriptor().getPluginClass().lastIndexOf("."))
        );
        // AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        log.info("Create applicationContext for '{}'", pluginId);

        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader(pluginClassLoader);
        applicationContext.setResourceLoader(defaultResourceLoader);
        // applicationContext.setClassLoader(pluginClassLoader);
        //
        // applicationContext.scan(plugin.getDescriptor().getPluginClass()
        //     .substring(0, plugin.getDescriptor().getPluginClass().lastIndexOf(".")));
        // applicationContext.refresh();
        log.info("ApplicationContext created, has beans = " + Arrays.toString(applicationContext.getBeanDefinitionNames()));

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

        log.info("registerAnnotationConfigProcessors");
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        registry.put(pluginId, applicationContext);

        return applicationContext;
    }

    public ApplicationContext getApplicationContext(String pluginId) {
        return registry.get(pluginId);
    }
}
