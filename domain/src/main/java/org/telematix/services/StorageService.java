package org.telematix.services;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
  private final Resource resource;

    public StorageService(@Value("${images.folder}") String imagesPath) throws IOException {
        this.resource = new ClassPathResource(imagesPath);
    }


}
