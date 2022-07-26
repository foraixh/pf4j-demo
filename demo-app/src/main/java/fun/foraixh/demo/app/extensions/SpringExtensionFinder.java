package fun.foraixh.demo.app.extensions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.AbstractExtensionFinder;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.pf4j.processor.ExtensionStorage;

/**
 * @Author foraixh
 * @Date 2022-07-24 18:38:00
 * @Version 1.0
 * @Usage usage
 */
@Slf4j
public class SpringExtensionFinder extends AbstractExtensionFinder {
    public static final String EXTENSIONS_RESOURCE = "META-INF/extensions.idx";

    public SpringExtensionFinder(SpringPluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public Map<String, Set<String>> readClasspathStorages() {
        log.debug("Don't need to read extensions storages from classpath");
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Set<String>> readPluginsStorages() {
        log.debug("Reading extensions storages from plugins");
        Map<String, Set<String>> result = new LinkedHashMap<>();

        List<PluginWrapper> plugins = pluginManager.getPlugins();
        for (PluginWrapper plugin : plugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            log.debug("Reading extensions storage from plugin '{}'", pluginId);
            Set<String> bucket = new HashSet<>();

            try {
                log.debug("Read '{}'", EXTENSIONS_RESOURCE);
                ClassLoader pluginClassLoader = plugin.getPluginClassLoader();
                try (InputStream resourceStream = pluginClassLoader.getResourceAsStream(EXTENSIONS_RESOURCE)) {
                    if (resourceStream == null) {
                        log.debug("Cannot find '{}'", EXTENSIONS_RESOURCE);
                    } else {
                        collectExtensions(resourceStream, bucket);
                    }
                }

                debugExtensions(bucket);

                result.put(pluginId, bucket);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        return result;
    }

    private void collectExtensions(InputStream inputStream, Set<String> bucket) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            ExtensionStorage.read(reader, bucket);
        }
    }
}
