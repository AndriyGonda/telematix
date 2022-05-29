package org.telematix.services;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    public StorageService(@Value("${images.folder}") String imagesPath) throws IOException {
        Resource resource = new ClassPathResource(imagesPath);
    }


}
