package config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class LoadURLs {
    public List<String> getURLs() {
        List<String> list = new ArrayList<>();
        try {
            list = Files.readAllLines(new File("src/test/resources/URLs").toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.getMessage();
        }
        return list;
    }
}
