package br.com.victorhugoof.filmestexoit.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ResourceReader {

    @Autowired
    private ResourceLoader resourceLoader;

    public String getResourceContents(String resource) throws IOException {
        try (var in = getResource(resource)) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        }
    }

    private InputStream getResource(String location) throws IOException {
        try {
            return resourceLoader.getResource(location).getInputStream();
        } catch (FileNotFoundException fe) {
            log.debug(fe.getMessage(), fe);
        }

        return new FileInputStream(location);
    }

}
