import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class I18nCheck {
    public static void main(String[] args) throws IOException {
        Path dir = Paths.get("core/src/main/resources/i18n");
        Map<String, Properties> locales = new LinkedHashMap<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "messages*.properties")) {
            for (Path file : stream) {
                Properties props = new Properties();
                try (InputStream in = Files.newInputStream(file)) {
                    props.load(in);
                }
                String name = file.getFileName().toString();
                String locale = name.replaceFirst("messages", "").replace(".properties", "");
                if (locale.startsWith("_")) {
                    locale = locale.substring(1);
                }
                if (locale.isEmpty()) {
                    locale = "default";
                }
                locales.put(locale, props);
            }
        }

        if (!locales.containsKey("default")) {
            System.err.println("Base messages.properties not found.");
            System.exit(1);
        }

        Set<String> baseKeys = locales.get("default").stringPropertyNames();
        boolean failed = false;
        for (Map.Entry<String, Properties> entry : locales.entrySet()) {
            String locale = entry.getKey();
            if ("default".equals(locale)) {
                continue;
            }
            Set<String> keys = entry.getValue().stringPropertyNames();
            Set<String> missing = new TreeSet<>(baseKeys);
            missing.removeAll(keys);
            Set<String> extra = new TreeSet<>(keys);
            extra.removeAll(baseKeys);
            if (!missing.isEmpty() || !extra.isEmpty()) {
                failed = true;
                System.out.println("Locale " + locale + ":");
                if (!missing.isEmpty()) {
                    System.out.println("  Missing keys: " + String.join(", ", missing));
                }
                if (!extra.isEmpty()) {
                    System.out.println("  Extra keys: " + String.join(", ", extra));
                }
            }
        }
        if (failed) {
            System.exit(1);
        }
    }
}
