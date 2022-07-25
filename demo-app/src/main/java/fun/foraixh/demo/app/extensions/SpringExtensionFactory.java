package fun.foraixh.demo.app.extensions;

import java.util.Optional;
import org.pf4j.ExtensionFactory;
import org.pf4j.Plugin;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.context.ApplicationContext;

/**
 * @Author foraixh
 * @Date 2022-07-24 18:32:00
 * @Version 1.0
 * @Usage usage
 */
public class SpringExtensionFactory implements ExtensionFactory {
    private SpringPluginManager springPluginManager;

    public SpringExtensionFactory(SpringPluginManager springPluginManager) {
        this.springPluginManager = springPluginManager;
    }

    @Override
    public <T> T create(Class<T> extensionClass) {
        PluginWrapper pluginWrapper = this.springPluginManager.whichPlugin(extensionClass);
        if (pluginWrapper == null) {
            throw new RuntimeException("there is no pluginWrapper for extensionClass: " +
                extensionClass.getSimpleName());
        }

        return springPluginManager.getPluginApplicationContext()
            .getApplicationContext(pluginWrapper.getPluginId()).getBean(extensionClass);

    }
}
