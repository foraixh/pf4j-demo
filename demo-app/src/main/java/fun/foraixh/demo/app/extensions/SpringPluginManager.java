package fun.foraixh.demo.app.extensions;

import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ExtensionFactory;
import org.pf4j.ExtensionFinder;
import org.pf4j.PluginDependency;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginState;
import org.pf4j.PluginStateEvent;
import org.pf4j.PluginWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author foraixh
 * @Date 2022-07-24 18:28:00
 * @Version 1.0
 * @Usage usage
 */
@Slf4j
public class SpringPluginManager extends DefaultPluginManager implements ApplicationContextAware, InitializingBean {
    private PluginApplicationContext pluginApplicationContext;
    // TODO 从这里开始
    // private PluginRequestMappingManager requestMappingManager;

    public SpringPluginManager() {
    }

    public SpringPluginManager(Path... pluginsRoots) {
        super(pluginsRoots);
    }

    public SpringPluginManager(List<Path> pluginsRoots) {
        super(pluginsRoots);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        pluginApplicationContext = new PluginApplicationContext(this);
    }

    @Override
    protected ExtensionFactory createExtensionFactory() {
        return new SpringExtensionFactory(applicationContext);
    }

    @Override
    protected ExtensionFinder createExtensionFinder() {
        return new SpringExtensionFinder(this);
    }

    @Override
    public void startPlugins() {
        for (PluginWrapper pluginWrapper : resolvedPlugins) {
            PluginState pluginState = pluginWrapper.getPluginState();
            if ((PluginState.DISABLED != pluginState) && (PluginState.STARTED != pluginState)) {
                try {
                    log.info("Start plugin '{}'", getPluginLabel(pluginWrapper.getDescriptor()));

                    // DESC 注册plugin到applicationContext中，SpringExtensionFactory可以获取到
                    registerBeanWithPlugin(pluginWrapper);

                    pluginWrapper.getPlugin().start();
                    pluginWrapper.setPluginState(PluginState.STARTED);
                    pluginWrapper.setFailedException(null);
                    startedPlugins.add(pluginWrapper);
                } catch (Exception | LinkageError e) {
                    pluginWrapper.setPluginState(PluginState.FAILED);
                    pluginWrapper.setFailedException(e);
                    log.error("Unable to start plugin '{}'", getPluginLabel(pluginWrapper.getDescriptor()), e);
                } finally {
                    firePluginStateEvent(new PluginStateEvent(this, pluginWrapper, pluginState));
                }
            }
        }
    }

    @Override
    public PluginState startPlugin(String pluginId) {
        checkPluginId(pluginId);

        PluginWrapper pluginWrapper = getPlugin(pluginId);
        PluginDescriptor pluginDescriptor = pluginWrapper.getDescriptor();
        PluginState pluginState = pluginWrapper.getPluginState();
        if (PluginState.STARTED == pluginState) {
            log.debug("Already started plugin '{}'", getPluginLabel(pluginDescriptor));
            return PluginState.STARTED;
        }

        if (!resolvedPlugins.contains(pluginWrapper)) {
            log.warn("Cannot start an unresolved plugin '{}'", getPluginLabel(pluginDescriptor));
            return pluginState;
        }

        if (PluginState.DISABLED == pluginState) {
            // automatically enable plugin on manual plugin start
            if (!enablePlugin(pluginId)) {
                return pluginState;
            }
        }

        for (PluginDependency dependency : pluginDescriptor.getDependencies()) {
            // start dependency only if it marked as required (non optional) or if it optional and loaded
            if (!dependency.isOptional() || plugins.containsKey(dependency.getPluginId())) {
                startPlugin(dependency.getPluginId());
            }
        }

        // DESC 注册plugin到applicationContext中，SpringExtensionFactory可以获取到
        registerBeanWithPlugin(pluginWrapper);

        log.info("Start plugin '{}'", getPluginLabel(pluginDescriptor));
        pluginWrapper.getPlugin().start();
        pluginWrapper.setPluginState(PluginState.STARTED);
        startedPlugins.add(pluginWrapper);

        firePluginStateEvent(new PluginStateEvent(this, pluginWrapper, pluginState));

        return pluginWrapper.getPluginState();
    }

    private void registerBeanWithPlugin(PluginWrapper pluginWrapper) {
        pluginApplicationContext.createApplicationContext(pluginWrapper.getPluginId());
    }
}
