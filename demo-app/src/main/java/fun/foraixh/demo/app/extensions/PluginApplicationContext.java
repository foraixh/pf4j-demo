package fun.foraixh.demo.app.extensions;

import fun.foraixh.definition.Greeting;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Controller;
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
        log.info("Create applicationContext for '{}'", pluginId);

        applicationContext.setParent(springPluginManager.getApplicationContext());
        applicationContext.setClassLoader(pluginClassLoader);

        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader(pluginClassLoader);
        applicationContext.setResourceLoader(defaultResourceLoader);

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        log.info("registerAnnotationConfigProcessors");
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        Set<Class<?>> candidateComponents = findCandidateComponents(pluginId);
        for (Class<?> component : candidateComponents) {
            log.debug("Register a plugin component class [{}] to context", component);
            AnnotatedBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(component);
            AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDefinition);

            applicationContext.registerBean(component, beanDefinition);
        }

        registerBean(applicationContext, pluginId);
        log.info("ApplicationContext created, has beans = " + Arrays.toString(applicationContext.getBeanDefinitionNames()));

        registry.put(pluginId, applicationContext);

        return applicationContext;
    }



    private Set<Class<?>> findCandidateComponents(String pluginId) {
        Set<String> extensionClassNames = springPluginManager.getExtensionClassNames(pluginId);

        // add extensions for each started plugin
        PluginWrapper plugin = springPluginManager.getPlugin(pluginId);
        log.debug("Registering extensions of the plugin '{}' as beans", pluginId);
        Set<Class<?>> candidateComponents = new HashSet<>();
        for (String extensionClassName : extensionClassNames) {
            log.debug("Load extension class '{}'", extensionClassName);
            try {
                Class<?> extensionClass =
                    plugin.getPluginClassLoader().loadClass(extensionClassName);

                candidateComponents.add(extensionClass);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        }

        return candidateComponents;
    }

    public void registerBean(ApplicationContext applicationContext, String pluginId) {

    }

    public ApplicationContext getApplicationContext(String pluginId) {
        return registry.get(pluginId);
    }
}
